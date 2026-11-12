# -*- coding: utf-8 -*-
"""Validacion estadistica para la revision del paper (modelos gratuitos).
Genera analysis_results_free/statistical_validation.txt
"""
import itertools
import io
import numpy as np
import pandas as pd
from pathlib import Path
from scipy import stats
from statsmodels.stats.contingency_tables import mcnemar
from statsmodels.stats.proportion import proportion_confint

BASE = Path(__file__).resolve().parent.parent
df = pd.read_csv(BASE / 'dataset_free_models.csv')
df['tests_passed_ratio'] = pd.to_numeric(df['tests_passed_ratio'], errors='coerce').fillna(0.0)
df['success'] = (df['tests_passed_ratio'] == 1.0).astype(int)
df['compiles'] = (df['compiles_or_runs'] == 'YES').astype(int)
df['loc'] = pd.to_numeric(df['loc'], errors='coerce')
df['version_key'] = df['target_version'].astype(str)

out = io.StringIO()
def p(*a):
    print(*a); print(*a, file=out)

def wilson(k, n):
    lo, hi = proportion_confint(k, n, method='wilson')
    return lo * 100, hi * 100

def cohen_h(p1, p2):
    return 2 * np.arcsin(np.sqrt(p1)) - 2 * np.arcsin(np.sqrt(p2))

models = sorted(df.ai_model_name.unique())
wide = df.pivot_table(index='prompt_id', columns='ai_model_name', values='success', aggfunc='first')

p("== 1. TASA DE EXITO POR MODELO (IC Wilson 95%) ==")
for m in models:
    k, n = int(df[df.ai_model_name == m].success.sum()), int((df.ai_model_name == m).sum())
    lo, hi = wilson(k, n)
    p(f"{m}: {k}/{n} = {100*k/n:.2f}%  IC95% [{lo:.1f}, {hi:.1f}]")

p("\n== 2. CHI-CUADRADO GLOBAL (3 modelos, independiente) ==")
ct = pd.crosstab(df.ai_model_name, df.success)
chi2, pv, dof, _ = stats.chi2_contingency(ct)
p(f"chi2={chi2:.3f}, gl={dof}, p={pv:.4f}")
try:
    from statsmodels.stats.contingency_tables import cochrans_q
    q = cochrans_q(wide.dropna().values)
    p(f"Q de Cochran (datos pareados): Q={q.statistic:.3f}, p={q.pvalue:.4f}")
except Exception as e:
    p("Cochran Q no disponible:", e)

p("\n== 3. COMPARACIONES PAREADAS (McNemar exacto, Holm-Bonferroni, OR y h de Cohen) ==")
rows = []
for a, b in itertools.combinations(models, 2):
    x, y = wide[a], wide[b]
    n11 = int(((x == 1) & (y == 1)).sum()); n10 = int(((x == 1) & (y == 0)).sum())
    n01 = int(((x == 0) & (y == 1)).sum()); n00 = int(((x == 0) & (y == 0)).sum())
    r = mcnemar([[n11, n10], [n01, n00]], exact=True)
    h = cohen_h(x.mean(), y.mean())
    orr = (n10 / n01) if n01 else float('inf')
    rows.append([a, b, n10, n01, r.pvalue, orr, h, x.mean() - y.mean()])
order = np.argsort([r[4] for r in rows])
m_ = len(rows)
adj = {}
run = 0
for rank, i in enumerate(order):
    run = max(run, min(1, rows[i][4] * (m_ - rank)))
    adj[i] = run
for i, r in enumerate(rows):
    p(f"{r[0]} vs {r[1]}: discordantes {r[2]}/{r[3]}, dif={100*r[7]:.2f} pp, "
      f"p(McNemar)={r[4]:.4f}, p(Holm)={adj[i]:.4f}, OR={r[5]:.2f}, h={r[6]:.3f}")

p("\n== 4. POR LENGUAJE (exito %, n=63 por celda) + Fisher/Chi2 entre modelos ==")
for lang, g in df.groupby('language'):
    parts = []
    for m in models:
        s = g[g.ai_model_name == m].success
        parts.append(f"{m}: {100*s.mean():.1f}% (n={len(s)})")
    c = pd.crosstab(g.ai_model_name, g.success)
    _, pv, _, _ = stats.chi2_contingency(c)
    p(f"{lang}: " + " | ".join(parts) + f" | chi2 p={pv:.4f}")

p("\n== 5. POR VERSION (solo prompts con version; exito % agregado de 3 modelos y por modelo) ==")
for lang, g in df.groupby('language'):
    for v, gv in g.groupby('version_key'):
        parts = [f"{m}: {100*gv[gv.ai_model_name==m].success.mean():.1f}%" for m in models]
        p(f"{lang} {v:8s} n={len(gv):3d} total={100*gv.success.mean():5.1f}% | " + " | ".join(parts))

p("\n== 6. POR TIPO DE PROMPT ==")
for t, g in df.groupby('prompt_type'):
    parts = [f"{m}: {100*g[g.ai_model_name==m].success.mean():.1f}%" for m in models]
    p(f"{t:20s} n={len(g):3d} total={100*g.success.mean():5.1f}% | " + " | ".join(parts))

p("\n== 7. POR PARADIGMA (solo version_constrained) ==")
vc = df[df.prompt_type == 'version_constrained']
for pr, g in vc.groupby('paradigm_requested'):
    parts = [f"{m}: {100*g[g.ai_model_name==m].success.mean():.1f}%" for m in models]
    p(f"{pr:10s} n={len(g):3d} total={100*g.success.mean():5.1f}% | " + " | ".join(parts))
c = pd.crosstab(vc.paradigm_requested, vc.success)
_, pv, _, _ = stats.chi2_contingency(c)
p(f"Chi2 paradigma vs exito: p={pv:.4f}")

p("\n== 8. REGRESION LOGISTICA (version_constrained): exito ~ modelo + lenguaje + paradigma + version ordinal ==")
try:
    import statsmodels.formula.api as smf
    vc = vc.copy()
    vc['vidx'] = vc.groupby('language')['version_key'].transform(lambda s: s.map({v: i for i, v in enumerate(sorted(s.unique(), key=lambda z: float(z.replace('-> ', '').split()[0]) if z[0].isdigit() else 0))}))
    mod = smf.logit('success ~ C(ai_model_name) + C(language) + C(paradigm_requested) + vidx', data=vc).fit(disp=0)
    p(mod.summary2().tables[1].round(4).to_string())
except Exception as e:
    p("Regresion no disponible:", e)

p("\n== 9. PRUEBA DE LOC (solo exitosos): Kruskal-Wallis + Mann-Whitney ==")
ok = df[df.success == 1]
groups = [ok[ok.ai_model_name == m]['loc'].dropna() for m in models]
for m, gg in zip(models, groups):
    p(f"{m}: media={gg.mean():.1f}, mediana={gg.median():.1f}, sd={gg.std():.1f}, n={len(gg)}")
h, pv = stats.kruskal(*groups)
p(f"Kruskal-Wallis H={h:.3f}, p={pv:.5f}")
for (m1, g1), (m2, g2) in itertools.combinations(zip(models, groups), 2):
    u, pv = stats.mannwhitneyu(g1, g2, alternative='two-sided')
    p(f"  {m1} vs {m2}: U={u:.0f}, p={pv:.5f}, r_rb={1 - 2*u/(len(g1)*len(g2)):.3f}")

p("\n== 10. COMPILACION / FALLOS ==")
for m in models:
    g = df[df.ai_model_name == m]
    p(f"{m}: compila/ejecuta={100*g.compiles.mean():.1f}%, "
      f"ratio medio de tests={g.tests_passed_ratio.mean():.3f}, "
      f"fallo por compilacion={(g.compiles==0).sum()}, fallo logico={((g.compiles==1)&(g.success==0)).sum()}")

(BASE / 'analysis_results_free').mkdir(exist_ok=True)
(BASE / 'analysis_results_free' / 'statistical_validation.txt').write_text(out.getvalue(), encoding='utf-8')

