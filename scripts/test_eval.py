import os, sys, subprocess, re
from pathlib import Path

BASE_DIR = Path(r'c:\Users\Ema\Documents\Dataset')

def run_scala():
    scala_file = BASE_DIR / 'llm_responses' / 'chatgpt-free' / 'P01_scala_3.3_iterativo.scala'
    code = scala_file.read_text('utf-8')
    if re.search(r'\bobject\s+\w+', code):
        code = re.sub(r'\bobject\s+\w+', 'object Solution', code)
    else:
        code = f"object Solution {{\n{code}\n}}"
        
    dest = BASE_DIR / 'unit_tests' / 'scala' / 'src' / 'main' / 'scala' / 'Solution.scala'
    dest.write_text(code, 'utf-8')
    
    result = subprocess.run(
        ['sbt.bat', '++ 3.3.3', 'testOnly *P01WordFrequencySpec'],
        cwd=BASE_DIR / 'unit_tests' / 'scala',
        capture_output=True, text=True
    )
    print(result.stdout)

run_scala()
