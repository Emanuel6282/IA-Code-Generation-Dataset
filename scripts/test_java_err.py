import os, sys, subprocess, re
from pathlib import Path

BASE_DIR = Path(r'c:\Users\Ema\Documents\Dataset')
java_file = BASE_DIR / 'llm_responses' / 'chatgpt-free' / 'P01_java_17_iterativo.java'
code = java_file.read_text('utf-8')
if not code.strip().startswith('import'):
    code = "import java.util.*;\nimport java.util.stream.*;\n" + code

if 'public class' in code:
    code = re.sub(r'public\s+class\s+\w+', 'public class Solution', code)
else:
    code = f"public class Solution {{\n{code}\n}}"
    
dest = BASE_DIR / 'unit_tests' / 'java' / 'src' / 'main' / 'java' / 'Solution.java'
if dest.exists(): dest.unlink()
dest.parent.mkdir(parents=True, exist_ok=True)
dest.write_text(code, 'utf-8')

test_dir = BASE_DIR / 'unit_tests' / 'java' / 'src' / 'test' / 'java'
for tf in test_dir.glob('*.java'):
    if tf.stem != 'TestP01WordFrequency':
        tf.rename(tf.with_suffix('.java.bak'))

result = subprocess.run(
    ['mvn.cmd', 'test', f'-Dtest=TestP01WordFrequency', f'-Dmaven.compiler.release=17'],
    cwd=str(BASE_DIR / 'unit_tests' / 'java'),
    capture_output=True, text=True, timeout=30
)
print("MAVEN OUTPUT:")
print(result.stdout)
for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
