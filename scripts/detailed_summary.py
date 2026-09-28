import csv
from pathlib import Path
from collections import Counter

csv_path = Path(r'c:\Users\Ema\Documents\Dataset\dataset_free_models.csv')

stats = {'python': {'YES':0, 'NO':0, 'PASSED_ALL':0},
         'java': {'YES':0, 'NO':0, 'PASSED_ALL':0},
         'scala': {'YES':0, 'NO':0, 'PASSED_ALL':0}}

with open(csv_path, 'r', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        lang = row['language'].lower()
        if row['compiles_or_runs'] == 'YES':
            stats[lang]['YES'] += 1
        else:
            stats[lang]['NO'] += 1
            
        if row['tests_passed_ratio'] == '1.0':
            stats[lang]['PASSED_ALL'] += 1

print(stats)
