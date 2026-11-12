# -*- coding: utf-8 -*-
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os
from pathlib import Path

# Configuracion de estilo
sns.set_theme(style="whitegrid")
plt.rcParams.update({'font.size': 12})

# Rutas
BASE_DIR = Path(__file__).resolve().parent.parent
CSV_PATH = BASE_DIR / 'dataset_free_models.csv'
OUTPUT_DIR = BASE_DIR / 'analysis_results_free'
IMG_DIR = BASE_DIR / 'imagenes'
OUTPUT_DIR.mkdir(exist_ok=True)
IMG_DIR.mkdir(exist_ok=True)

# 1. Cargar datos
print("Cargando dataset...")
df = pd.read_csv(CSV_PATH)

# Convertir tipos
df['tests_passed_ratio'] = pd.to_numeric(df['tests_passed_ratio'], errors='coerce').fillna(0.0)
df['loc'] = pd.to_numeric(df['loc'], errors='coerce')
df['success'] = (df['tests_passed_ratio'] == 1.0).astype(int)

# Renombrar modelos para visualización
model_mapping = {
    'ChatGPT Free': 'GPT-4o mini',
    'Gemini Free': 'Gemini 3.5 Flash',
    'Claude Free': 'Sonnet 5'
}
df['ai_model_name'] = df['ai_model_name'].replace(model_mapping)

# ---------------------------------------------------------
# Estadisticas Globales
# ---------------------------------------------------------
print("\n--- RESUMEN GLOBAL ---")
total_filas = len(df)
exitos = df['success'].sum()
print(f"Total de pruebas: {total_filas}")
print(f"Pruebas que pasaron todos los tests: {exitos} ({(exitos/total_filas)*100:.1f}%)")

# ---------------------------------------------------------
# Grafico 1: Tasa de Exito por Modelo
# ---------------------------------------------------------
plt.figure(figsize=(8, 6))
success_by_model = df.groupby('ai_model_name')['success'].mean() * 100
ax = sns.barplot(x=success_by_model.index, y=success_by_model.values, hue=success_by_model.index, palette='viridis', legend=False)
plt.title('Tasa de Éxito por Modelo de IA (Nivel Gratuito)', fontsize=14, fontweight='bold')
plt.ylabel('Tasa de Éxito (%)')
plt.xlabel('Modelo')
plt.ylim(0, 100)
for p in ax.patches:
    ax.annotate(f"{p.get_height():.1f}%", (p.get_x() + p.get_width() / 2., p.get_height()), 
                ha='center', va='bottom', fontsize=11, fontweight='bold')
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '1_success_by_model.png', dpi=300)
plt.savefig(IMG_DIR / 'free_1_success_by_model.png', dpi=300)
plt.close()

# ---------------------------------------------------------
# Grafico 2: Tasa de Exito por Modelo y Lenguaje
# ---------------------------------------------------------
plt.figure(figsize=(10, 6.5))
success_by_model_lang = df.groupby(['ai_model_name', 'language'])['success'].mean().reset_index()
success_by_model_lang['success'] *= 100
ax = sns.barplot(x='ai_model_name', y='success', hue='language', data=success_by_model_lang, palette='Set2')
plt.title('Tasa de Éxito por Modelo y Lenguaje (Nivel Gratuito)', fontsize=14, fontweight='bold', pad=58)
plt.ylabel('Tasa de Éxito (%)')
plt.xlabel('Modelo')
plt.ylim(0, 110)
ax.set_yticks([0, 20, 40, 60, 80, 100])
for p in ax.patches:
    h = p.get_height()
    if h > 0:
        ax.annotate(f"{h:.1f}%", (p.get_x() + p.get_width() / 2., h),
                    ha='center', va='bottom', fontsize=10, fontweight='bold')
plt.legend(title='Lenguaje', loc='lower center', bbox_to_anchor=(0.5, 1.01), ncol=3, frameon=True)
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '2_success_by_model_language.png', dpi=300, bbox_inches='tight')
plt.savefig(IMG_DIR / 'free_2_success_by_model_language.png', dpi=300, bbox_inches='tight')
plt.close()

# ---------------------------------------------------------
# Grafico 3: Lineas de Codigo (LOC) Promedio (solo exitosos)
# ---------------------------------------------------------
plt.figure(figsize=(8, 6))
df_success = df[df['success'] == 1]
loc_by_model = df_success.groupby('ai_model_name')['loc'].mean()
ax = sns.barplot(x=loc_by_model.index, y=loc_by_model.values, hue=loc_by_model.index, palette='magma', legend=False)
plt.title('Promedio de Líneas de Código en Soluciones Exitosas (Nivel Gratuito)', fontsize=13, fontweight='bold')
plt.ylabel('Líneas de Código (LOC)')
plt.xlabel('Modelo')
plt.ylim(0, 55)
for p in ax.patches:
    ax.annotate(f"{p.get_height():.1f}", (p.get_x() + p.get_width() / 2., p.get_height()), 
                ha='center', va='bottom', fontsize=11, fontweight='bold')
plt.tight_layout()
plt.savefig(OUTPUT_DIR / '3_loc_by_model.png', dpi=300)
plt.savefig(IMG_DIR / 'free_3_loc_by_model.png', dpi=300)
plt.close()

print(f"\nAnalisis completado. Graficos guardados en: {OUTPUT_DIR}")
