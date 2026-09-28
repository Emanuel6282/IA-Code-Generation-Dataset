import csv
from pathlib import Path

csv_path = Path(r'c:\Users\Ema\Documents\Dataset\dataset_free_models.csv')
passed = 0
failed = 0
total = 0

with open(csv_path, 'r', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        total += 1
        if row['tests_passed_ratio'] == '1.0':
            passed += 1
        else:
            failed += 1

print(f"Total: {total}")
print(f"Passed: {passed}")
print(f"Failed: {failed}")
