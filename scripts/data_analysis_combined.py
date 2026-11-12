# -*- coding: utf-8 -*-
"""Generacion de graficas comparativas globales (Free vs Paid - 6 modelos, 1134 filas).
Guarda las imagenes en analysis_results_combined/
"""
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from pathlib import Path

sns.set_theme(style="whitegrid")
plt.rcParams.update({'font.size': 11})

BASE_DIR = Path(__file__).resolve().parent.parent
FREE_CSV = BASE_DIR / 'dataset_free_models.csv'
PAID_CSV = BASE_DIR / 'dataset_paid_models.csv'
CSV_PATH = BASE_DIR / 'dataset_all_models.csv'
OUTPUT_DIR = BASE_DIR / 'analysis_results_combined'
IMG_DIR = BASE_DIR / 'imagenes'
OUTPUT_DIR.mkdir(exist_ok=True)
IMG_DIR.mkdir(exist_ok=True)

if FREE_CSV.exists() and PAID_CSV.exists():
    pd.concat([pd.read_csv(FREE_CSV), pd.read_csv(PAID_CSV)], ignore_index=True).to_csv(CSV_PATH, index=False)
df = pd.read_csv(CSV_PATH)
df['tests_passed_ratio'] = pd.to_numeric(df['tests_passed_ratio'], errors='coerce').fillna(0.0)
df['loc'] = pd.to_numeric(df['loc'], errors='coerce')
df['success'] = (df['tests_passed_ratio'] == 1.0).astype(int)

PROVIDER_MAP = {
    'ChatGPT Free': 'OpenAI\n(GPT-4o mini vs GPT-6)',
    'ChatGPT Plus': 'OpenAI\n(GPT-4o mini vs GPT-6)',
    'Gemini Free': 'Google\n(3.5 Flash vs 3.1 Pro)',
    'Gemini Pro': 'Google\n(3.5 Flash vs 3.1 Pro)',
    'Claude Free': 'Anthropic\n(Sonnet 5 vs Opus 5.5)',
    'Claude Pro': 'Anthropic\n(Sonnet 5 vs Opus 5.5)',
}
SHORT_NAME = {
    'ChatGPT Free': 'GPT-4o mini\n(Gratuito)',
    'ChatGPT Plus': 'GPT-6\n(De Pago)',
    'Gemini Free': 'Gemini 3.5 Flash\n(Gratuito)',
    'Gemini Pro': 'Gemini 3.1 Pro\n(De Pago)',
    'Claude Free': 'Sonnet 5\n(Gratuito)',
    'Claude Pro': 'Opus 5.5\n(De Pago)',
}
TIER_LABEL = {'free': 'Nivel Gratuito', 'paid': 'Nivel de Pago'}

df['provider_group'] = df['ai_model_name'].map(PROVIDER_MAP)
df['short_model'] = df['ai_model_name'].map(SHORT_NAME)
df['tier_label'] = df['ai_tier'].map(TIER_LABEL)

model_order = [
    'GPT-4o mini\n(Gratuito)', 'GPT-6\n(De Pago)',
    'Gemini 3.5 Flash\n(Gratuito)', 'Gemini 3.1 Pro\n(De Pago)',
    'Sonnet 5\n(Gratuito)', 'Opus 5.5\n(De Pago)'
]

# ---------------------------------------------------------
# Grafico 1: Free vs Paid por Proveedor
# ---------------------------------------------------------
plt.figure(figsize=(10, 6.5))
prov_tier = df.groupby(['provider_group', 'tier_label'], as_index=False)['success'].mean()
prov_tier['success'] *= 100
prov_order = [
    'OpenAI\n(GPT-4o mini vs GPT-6)',
    'Google\n(3.5 Flash vs 3.1 Pro)',
    'Anthropic\n(Sonnet 5 vs Opus 5.5)'
]
ax = sns.barplot(
    data=prov_tier, x='provider_group', y='success', hue='tier_label',
    order=prov_order, hue_order=['Nivel Gratuito', 'Nivel de Pago'], palette=['#4C9F70', '#2B59C3']
)
plt.title('Comparación de Tasa de Éxito: Nivel Gratuito vs. Nivel de Pago por Proveedor', fontsize=14, fontweight='bold', pad=58)
plt.ylabel('Tasa de Éxito (%)')
plt.xlabel('Proveedor y Par de Modelos')
plt.ylim(0, 105)
ax.set_yticks([0, 20, 40, 60, 80, 100])
plt.legend(title='Nivel de Suscripción', loc='lower center', bbox_to_anchor=(0.5, 1.01), ncol=2, frameon=True)
for p in ax.patches:
    h = p.get_height()
    if h > 0:
        ax.annotate(f"{h:.1f}%", (p.get_x() + p.get_width() / 2., h),
                    ha='center', va='bottom', fontsize=10, fontweight='bold')
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '1_free_vs_paid_by_provider.png', dpi=300, bbox_inches='tight')
plt.savefig(IMG_DIR / '1_free_vs_paid_by_provider.png', dpi=300, bbox_inches='tight')
plt.close()

# ---------------------------------------------------------
# Grafico 2: Exito por Modelo y Lenguaje (6 Modelos)
# ---------------------------------------------------------
plt.figure(figsize=(12, 6.5))
mod_lang = df.groupby(['short_model', 'language'], as_index=False)['success'].mean()
mod_lang['success'] *= 100
ax = sns.barplot(
    data=mod_lang, x='short_model', y='success', hue='language',
    order=model_order, palette='Set2'
)
plt.title('Tasa de Éxito por Modelo y Lenguaje de Programación (6 Modelos)', fontsize=14, fontweight='bold', pad=58)
plt.ylabel('Tasa de Éxito (%)')
plt.xlabel('Modelo (Gratuito vs. De Pago)')
plt.ylim(0, 110)
ax.set_yticks([0, 20, 40, 60, 80, 100])
plt.legend(title='Lenguaje', loc='lower center', bbox_to_anchor=(0.5, 1.01), ncol=3, frameon=True)
for p in ax.patches:
    h = p.get_height()
    if h > 0:
        ax.annotate(f"{h:.1f}%", (p.get_x() + p.get_width() / 2., h),
                    ha='center', va='bottom', fontsize=8.5, fontweight='bold')
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '2_success_by_language_and_tier.png', dpi=300, bbox_inches='tight')
plt.savefig(IMG_DIR / '2_success_by_language_and_tier.png', dpi=300, bbox_inches='tight')
plt.close()

# ---------------------------------------------------------
# Grafico 3: Conciencia de Version (Version-Constrained) Free vs Paid
# ---------------------------------------------------------
vc = df[df['prompt_type'] == 'version_constrained'].copy()
vc['lang_ver'] = vc['language'] + ' ' + vc['target_version'].astype(str)
ver_order = [
    'Java 8', 'Java 17', 'Java 21',
    'Python 3.9', 'Python 3.11', 'Python 3.14',
    'Scala 2.13', 'Scala 3.3', 'Scala 3.6'
]
plt.figure(figsize=(12, 6.5))
ver_tier = vc.groupby(['lang_ver', 'tier_label'], as_index=False)['success'].mean()
ver_tier['success'] *= 100
ax = sns.barplot(
    data=ver_tier, x='lang_ver', y='success', hue='tier_label',
    order=ver_order, hue_order=['Nivel Gratuito', 'Nivel de Pago'], palette=['#4C9F70', '#2B59C3']
)
plt.title('Tasa de Éxito por Versión del Lenguaje (Nivel Gratuito vs. Nivel de Pago)', fontsize=14, fontweight='bold', pad=58)
plt.ylabel('Tasa de Éxito (%)')
plt.xlabel('Lenguaje y Versión Objetivo')
plt.ylim(0, 110)
ax.set_yticks([0, 20, 40, 60, 80, 100])
plt.xticks(rotation=20)
plt.legend(title='Nivel de Suscripción', loc='lower center', bbox_to_anchor=(0.5, 1.01), ncol=2, frameon=True)
for p in ax.patches:
    h = p.get_height()
    if h > 0:
        ax.annotate(f"{h:.1f}%", (p.get_x() + p.get_width() / 2., h),
                    ha='center', va='bottom', fontsize=8.5, fontweight='bold')
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '3_version_awareness_comparison.png', dpi=300, bbox_inches='tight')
plt.savefig(IMG_DIR / '3_version_awareness_comparison.png', dpi=300, bbox_inches='tight')
plt.close()

# ---------------------------------------------------------
# Grafico 4: LOC Promedio en los 6 Modelos (Soluciones Exitosas)
# ---------------------------------------------------------
plt.figure(figsize=(10, 6))
df_ok = df[df['success'] == 1]
loc_mod = df_ok.groupby('short_model', as_index=False)['loc'].mean()
ax = sns.barplot(
    data=loc_mod, x='short_model', y='loc', hue='short_model',
    order=model_order, palette='magma', legend=False
)
plt.title('Promedio de Líneas de Código (LOC) en los 6 Modelos (Soluciones Exitosas)', fontsize=14, fontweight='bold')
plt.ylabel('Promedio de Líneas de Código (LOC)')
plt.xlabel('Modelo (Gratuito vs. De Pago)')
plt.ylim(0, 55)
for p in ax.patches:
    h = p.get_height()
    if h > 0:
        ax.annotate(f"{h:.1f}", (p.get_x() + p.get_width() / 2., h),
                    ha='center', va='bottom', fontsize=10, fontweight='bold')
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '4_loc_comparison_all_models.png', dpi=300)
plt.savefig(IMG_DIR / '4_loc_comparison_all_models.png', dpi=300)
plt.close()

print(f"Graficas combinadas guardadas en: {OUTPUT_DIR}")

