import csv
from collections import defaultdict
from pathlib import Path
csv_path = Path(r'c:\Users\Ema\Documents\Dataset\dataset_free_models.csv')
errors = defaultdict(set)
with open(csv_path, 'r', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        if row['compiles_or_runs'] == 'NO':
            lang = row['language'].lower()
            err = row['notes'].split('\n')[0][:100]
            errors[lang].add(err)

for lang, errs in errors.items():
    print(f"{lang.upper()}:")
    for e in errs:
        print(f"  - {e}")
