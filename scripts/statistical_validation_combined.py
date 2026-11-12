# -*- coding: utf-8 -*-
"""Validacion estadistica comparativa global (Free vs Paid - 6 modelos, 1134 filas).
Genera analysis_results_combined/statistical_validation.txt
"""
import itertools
import io
import numpy as np
import pandas as pd
from pathlib import Path
from scipy import stats
from statsmodels.stats.contingency_tables import mcnemar, cochrans_q
from statsmodels.stats.proportion import proportion_confint

BASE = Path(__file__).resolve().parent.parent
if (BASE / 'dataset_free_models.csv').exists() and (BASE / 'dataset_paid_models.csv').exists():
    pd.concat([pd.read_csv(BASE / 'dataset_free_models.csv'), pd.read_csv(BASE / 'dataset_paid_models.csv')], ignore_index=True).to_csv(BASE / 'dataset_all_models.csv', index=False)
df = pd.read_csv(BASE / 'dataset_all_models.csv')
df['tests_passed_ratio'] = pd.to_numeric(df['tests_passed_ratio'], errors='coerce').fillna(0.0)
df['success'] = (df['tests_passed_ratio'] == 1.0).astype(int)
df['compiles'] = (df['compiles_or_runs'] == 'YES').astype(int)
df['loc'] = pd.to_numeric(df['loc'], errors='coerce')
df['version_key'] = df['target_version'].astype(str)
df['problem_id'] = df['prompt_id'].apply(lambda s: s.split('_')[0])

PROVIDER_MAP = {
    'ChatGPT Free': 'OpenAI',
    'ChatGPT Plus': 'OpenAI',
    'Gemini Free': 'Google',
    'Gemini Pro': 'Google',
    'Claude Free': 'Anthropic',
    'Claude Pro': 'Anthropic',
}
DISPLAY_MAP = {
    'ChatGPT Free': 'ChatGPT Free (GPT-4o mini)',
    'ChatGPT Plus': 'ChatGPT Plus (GPT-6)',
    'Gemini Free': 'Gemini Free (3.5 Flash)',
    'Gemini Pro': 'Gemini Pro (3.1 Pro)',
    'Claude Free': 'Claude Free (Sonnet 5)',
    'Claude Pro': 'Claude Pro (Opus 5.5)',
}
df['provider'] = df['ai_model_name'].map(PROVIDER_MAP)

out = io.StringIO()
def p(*a):
    print(*a); print(*a, file=out)

def wilson(k, n):
    lo, hi = proportion_confint(k, n, method='wilson')
    return lo * 100, hi * 100

def cohen_h(p1, p2):
    return 2 * np.arcsin(np.sqrt(p1)) - 2 * np.arcsin(np.sqrt(p2))

models_order = ['ChatGPT Free', 'ChatGPT Plus', 'Gemini Free', 'Gemini Pro', 'Claude Free', 'Claude Pro']
wide = df.pivot_table(index='prompt_id', columns='ai_model_name', values='success', aggfunc='first')[models_order]

p("=========================================================================")
p("VALIDACION ESTADISTICA GLOBAL: MODELOS GRATUITOS VS. DE PAGO (N = 1134)")
p("=========================================================================\n")

p("== 1. TASA DE EXITO POR MODELO Y POR TIER (IC Wilson 95%) ==")
for m in models_order:
    sub = df[df.ai_model_name == m]
    k, n = int(sub.success.sum()), len(sub)
    lo, hi = wilson(k, n)
    p(f"{DISPLAY_MAP[m]:30s}: {k:3d}/{n} = {100*k/n:6.2f}%  IC95% [{lo:5.1f}%, {hi:5.1f}%]")

p("-" * 73)
for tier_key, tier_label in [('free', 'TIER GRATUITO (Agregado)'), ('paid', 'TIER DE PAGO (Agregado)')]:
    sub = df[df.ai_tier == tier_key]
    k, n = int(sub.success.sum()), len(sub)
    lo, hi = wilson(k, n)
    p(f"{tier_label:30s}: {k:3d}/{n} = {100*k/n:6.2f}%  IC95% [{lo:5.1f}%, {hi:5.1f}%]")

k_all, n_all = int(df.success.sum()), len(df)
lo_all, hi_all = wilson(k_all, n_all)
p(f"{'CORPUS TOTAL (6 Modelos)':30s}: {k_all:3d}/{n_all} = {100*k_all/n_all:6.2f}%  IC95% [{lo_all:5.1f}%, {hi_all:5.1f}%]")

p("\n== 2. CONTRASTES GLOBALES (6 Modelos y Free vs. Paid) ==")
ct6 = pd.crosstab(df.ai_model_name, df.success)
chi2_6, pv_6, dof_6, _ = stats.chi2_contingency(ct6)
q6 = cochrans_q(wide.dropna().values)
p(f"6 Modelos - Chi-cuadrado independiente: chi2={chi2_6:.3f}, gl={dof_6}, p={pv_6:.4f}")
p(f"6 Modelos - Q de Cochran (bloque pareado n=189): Q={q6.statistic:.3f}, gl={q6.df}, p={q6.pvalue:.6f}")

# Free vs Paid global (paired by prompt_id x provider -> 567 pairs)
df_free = df[df.ai_tier == 'free'].set_index(['provider', 'prompt_id'])['success']
df_paid = df[df.ai_tier == 'paid'].set_index(['provider', 'prompt_id'])['success']
paired_tier = pd.DataFrame({'free': df_free, 'paid': df_paid}).dropna()
n11 = int(((paired_tier.free == 1) & (paired_tier.paid == 1)).sum())
n10 = int(((paired_tier.free == 1) & (paired_tier.paid == 0)).sum())
n01 = int(((paired_tier.free == 0) & (paired_tier.paid == 1)).sum())
n00 = int(((paired_tier.free == 0) & (paired_tier.paid == 0)).sum())
mc_tier = mcnemar([[n11, n10], [n01, n00]], exact=True)
h_tier = cohen_h(paired_tier.paid.mean(), paired_tier.free.mean())
p(f"Free vs Paid Global (567 pares pareados prompt x proveedor): Free={100*paired_tier.free.mean():.2f}% vs Paid={100*paired_tier.paid.mean():.2f}%, "
  f"discordantes (Free_ok/Paid_fail={n10}, Free_fail/Paid_ok={n01}), p(McNemar)={mc_tier.pvalue:.4f}, h={h_tier:.3f}")

p("\n== 3. COMPARACION INTRA-PROVEEDOR: FREE VS. PAID (McNemar exacto, Holm-Bonferroni, OR, h de Cohen) ==")
intra_pairs = [
    ('OpenAI', 'ChatGPT Free', 'ChatGPT Plus'),
    ('Google', 'Gemini Free', 'Gemini Pro'),
    ('Anthropic', 'Claude Free', 'Claude Pro'),
]
rows_intra = []
for prov, m_free, m_paid in intra_pairs:
    x, y = wide[m_free], wide[m_paid]
    n11 = int(((x == 1) & (y == 1)).sum()); n10 = int(((x == 1) & (y == 0)).sum())
    n01 = int(((x == 0) & (y == 1)).sum()); n00 = int(((x == 0) & (y == 0)).sum())
    r = mcnemar([[n11, n10], [n01, n00]], exact=True)
    h = cohen_h(y.mean(), x.mean())
    orr = (n01 / n10) if n10 else float('inf')
    rows_intra.append([prov, m_free, m_paid, n10, n01, r.pvalue, orr, h, y.mean() - x.mean()])

order = np.argsort([r[5] for r in rows_intra])
m_ = len(rows_intra)
adj = {}
run = 0
for rank, i in enumerate(order):
    run = max(run, min(1.0, rows_intra[i][5] * (m_ - rank)))
    adj[i] = run
for i, r in enumerate(rows_intra):
    p(f"{r[0]:10s} ({r[1]} -> {r[2]}): dif={100*r[8]:+6.2f} pp (Free_only={r[3]}, Paid_only={r[4]}), "
      f"p(McNemar)={r[5]:.4f}, p(Holm)={adj[i]:.4f}, OR(Paid/Free)={r[6]:.2f}, h={r[7]:+.3f}")

p("\n== 4. POR LENGUAJE Y MODELO (exito %, n=63 por celda) ==")
for lang, g in df.groupby('language'):
    parts = []
    for m in models_order:
        s = g[g.ai_model_name == m].success
        parts.append(f"{m}: {100*s.mean():.1f}%")
    s_free = g[g.ai_tier == 'free'].success.mean() * 100
    s_paid = g[g.ai_tier == 'paid'].success.mean() * 100
    c = pd.crosstab(g.ai_model_name, g.success)
    _, pv, _, _ = stats.chi2_contingency(c)
    p(f"{lang:7s} [Free={s_free:5.1f}% | Paid={s_paid:5.1f}% | chi2_6mod p={pv:.4f}]: " + " | ".join(parts))

p("\n== 5. POR VERSION (Free vs Paid y desglose por modelo) ==")
for lang, g in df.groupby('language'):
    for v, gv in g.groupby('version_key'):
        s_free = 100 * gv[gv.ai_tier == 'free'].success.mean()
        s_paid = 100 * gv[gv.ai_tier == 'paid'].success.mean()
        s_tot = 100 * gv.success.mean()
        parts = [f"{m}: {100*gv[gv.ai_model_name==m].success.mean():5.1f}%" for m in models_order]
        p(f"{lang:6s} {v:12s} (n={len(gv):3d}) Total={s_tot:5.1f}% [Free={s_free:5.1f}% vs Paid={s_paid:5.1f}%] | " + " | ".join(parts))

p("\n== 6. POR TIPO DE PROMPT (Free vs Paid y por modelo) ==")
for t, g in df.groupby('prompt_type'):
    s_free = 100 * g[g.ai_tier == 'free'].success.mean()
    s_paid = 100 * g[g.ai_tier == 'paid'].success.mean()
    s_tot = 100 * g.success.mean()
    parts = [f"{m}: {100*g[g.ai_model_name==m].success.mean():5.1f}%" for m in models_order]
    p(f"{t:20s} (n={len(g):3d}) Total={s_tot:5.1f}% [Free={s_free:5.1f}% vs Paid={s_paid:5.1f}%] | " + " | ".join(parts))

p("\n== 7. POR PARADIGMA (solo version_constrained, n=756) ==")
vc = df[df.prompt_type == 'version_constrained']
for pr, g in vc.groupby('paradigm_requested'):
    s_free = 100 * g[g.ai_tier == 'free'].success.mean()
    s_paid = 100 * g[g.ai_tier == 'paid'].success.mean()
    s_tot = 100 * g.success.mean()
    parts = [f"{m}: {100*g[g.ai_model_name==m].success.mean():5.1f}%" for m in models_order]
    p(f"{pr:10s} (n={len(g):3d}) Total={s_tot:5.1f}% [Free={s_free:5.1f}% vs Paid={s_paid:5.1f}%] | " + " | ".join(parts))
c = pd.crosstab(vc.paradigm_requested, vc.success)
_, pv, _, _ = stats.chi2_contingency(c)
p(f"Chi2 paradigma vs exito (global 6 modelos): p={pv:.4f}")

p("\n== 8. POR PROBLEMA ALGORITMICO (P01-P07, n=162 por problema) ==")
for pid, g in df.groupby('problem_id'):
    pname = g['problem_name'].iloc[0]
    s_free = 100 * g[g.ai_tier == 'free'].success.mean()
    s_paid = 100 * g[g.ai_tier == 'paid'].success.mean()
    s_tot = 100 * g.success.mean()
    by_lang = [f"{l}: {100*g[g.language==l].success.mean():5.1f}%" for l in ['Java', 'Python', 'Scala']]
    p(f"{pid} ({pname:22s}) Total={s_tot:5.1f}% [Free={s_free:5.1f}% vs Paid={s_paid:5.1f}%] | " + " | ".join(by_lang))

p("\n== 9. REGRESION LOGISTICA MULTIFACTORIAL (Corpus completo N=1134) ==")
try:
    import statsmodels.formula.api as smf
    mod_all = smf.logit('success ~ C(ai_tier) + C(provider) + C(language) + C(prompt_type)', data=df).fit(disp=0)
    p(mod_all.summary2().tables[1].round(4).to_string())
except Exception as e:
    p("Regresion global no disponible:", e)

p("\n== 10. PRUEBA DE LOC (solo exitosos): Kruskal-Wallis (6 modelos) y Mann-Whitney (Free vs Paid por proveedor) ==")
ok = df[df.success == 1]
groups = [ok[ok.ai_model_name == m]['loc'].dropna() for m in models_order]
for m, gg in zip(models_order, groups):
    p(f"{DISPLAY_MAP[m]:30s}: media={gg.mean():5.1f}, mediana={gg.median():5.1f}, sd={gg.std():5.1f}, n={len(gg):3d}")
h, pv = stats.kruskal(*groups)
p(f"Kruskal-Wallis (6 modelos) H={h:.3f}, p={pv:.6f}")
for prov, m_free, m_paid in intra_pairs:
    g1 = ok[ok.ai_model_name == m_free]['loc'].dropna()
    g2 = ok[ok.ai_model_name == m_paid]['loc'].dropna()
    u, pv = stats.mannwhitneyu(g1, g2, alternative='two-sided')
    p(f"  {prov:10s} LOC ({m_free} vs {m_paid}): media {g1.mean():.1f} -> {g2.mean():.1f} (mediana {g1.median():.1f} -> {g2.median():.1f}), U={u:.0f}, p={pv:.5f}")

p("\n== 11. CLASIFICACION DE FALLOS (Compilacion vs Logicos) EN LOS 6 MODELOS ==")
for m in models_order:
    g = df[df.ai_model_name == m]
    comp_fail = int((g.compiles == 0).sum())
    log_fail = int(((g.compiles == 1) & (g.success == 0)).sum())
    tot_fail = comp_fail + log_fail
    p(f"{DISPLAY_MAP[m]:30s}: compila/ejecuta={100*g.compiles.mean():5.1f}%, "
      f"total_fallos={tot_fail:2d} (compilacion={comp_fail:2d}, fallo_logico={log_fail:2d})")

(BASE / 'analysis_results_combined').mkdir(exist_ok=True)
(BASE / 'analysis_results_combined' / 'statistical_validation.txt').write_text(out.getvalue(), encoding='utf-8')

