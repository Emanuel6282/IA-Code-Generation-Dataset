import pandas as pd
from pathlib import Path

CSV_PATH = Path(r'c:\Users\Ema\Documents\Dataset\dataset_free_models.csv')
df = pd.read_csv(CSV_PATH)
df['success'] = (pd.to_numeric(df['tests_passed_ratio'], errors='coerce') == 1.0).astype(int)
df['loc'] = pd.to_numeric(df['loc'], errors='coerce')

print("--- EXITO POR MODELO ---")
print(df.groupby('ai_model_name')['success'].mean() * 100)

print("\n--- EXITO POR MODELO Y LENGUAJE ---")
print(df.groupby(['ai_model_name', 'language'])['success'].mean() * 100)

print("\n--- LOC PROMEDIO POR MODELO (SOLO EXITOSOS) ---")
df_success = df[df['success'] == 1]
print(df_success.groupby('ai_model_name')['loc'].mean())
