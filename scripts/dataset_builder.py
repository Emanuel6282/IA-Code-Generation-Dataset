import os
import json
import csv
import re
import sys
import subprocess
import shutil
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
RESPONSES_DIR = BASE_DIR / 'llm_responses'
PROMPTS_JSON = BASE_DIR / 'prompts' / 'prompts_data.json'
OUTPUT_CSV = BASE_DIR / 'dataset_free_models.csv'

MODELS_TIER = {
    'chatgpt-free': ('ChatGPT Free', 'free'),
    'gemini-free': ('Gemini Free', 'free'),
    'claude-free': ('Claude Free', 'free')
}

COLUMNS = [
    'prompt_id', 'problem_name', 'language', 'target_version', 'prompt_type', 
    'paradigm_requested', 'ai_model_name', 'ai_tier', 'loc',
    'compiles_or_runs', 'tests_passed_ratio', 'notes'
]

TEST_MAP_JAVA = {
    'p01': 'TestP01WordFrequency',
    'p02': 'TestP02BinarySearch',
    'p03': 'TestP03FlattenList',
    'p04': 'TestP04BalancedParentheses',
    'p05': 'TestP05TransactionPipeline',
    'p06': 'TestP06MaxSubarray',
    'p07': 'TestP07FloodFill',
}

TEST_MAP_SCALA = {
    'p01': 'P01WordFrequencySpec',
    'p02': 'P02BinarySearchSpec',
    'p03': 'P03FlattenListSpec',
    'p04': 'P04BalancedParenthesesSpec',
    'p05': 'P05TransactionPipelineSpec',
    'p06': 'P06MaxSubarraySpec',
    'p07': 'P07FloodFillSpec',
}

FUNC_MAP_SCALA = {
    'p01': 'wordFrequency',
    'p02': 'binarySearch',
    'p03': 'flatten',
    'p04': 'isBalanced',
    'p05': 'processTransactions',
    'p06': 'maxSubarraySum',
    'p07': 'floodFill',
}

def strip_comments(code):
    code = re.sub(r'/\*.*?\*/', lambda m: ' ' * len(m.group(0)), code, flags=re.S)
    code = re.sub(r'//[^\n]*', lambda m: ' ' * len(m.group(0)), code)
    return code

def prepare_java_code(code):
    if not code.strip().startswith('import'):
        code = "import java.util.*;\nimport java.util.stream.*;\n" + code
    nc = strip_comments(code)
    m = re.search(r'\bpublic\s+class\s+(\w+)', nc)
    if m:
        orig_name = m.group(1)
        code = re.sub(r'\bpublic\s+class\s+' + re.escape(orig_name) + r'\b', 'public class Solution', code, count=1)
        if orig_name != 'Solution':
            code = re.sub(r'\b' + re.escape(orig_name) + r'\b', 'Solution', code)
    else:
        code = f"public class Solution {{\n{code}\n}}"
    return code

def prepare_scala_code(code, prompt_id):
    problem_id = prompt_id.split('_')[0].lower()
    fn = FUNC_MAP_SCALA.get(problem_id, '')
    nc = strip_comments(code)
    is_top_level_fn = bool(fn and re.search(rf'(?m)^def\s+{fn}\b', nc))
    objs = list(re.finditer(r'\bobject\s+(\w+)', nc))
    if objs and not is_top_level_fn:
        fn_m = re.search(rf'\bdef\s+{fn}\b', nc) if fn else None
        target_obj = objs[-1].group(1)
        if fn_m:
            before = [o.group(1) for o in objs if o.start() < fn_m.start()]
            if before:
                target_obj = before[-1]
        code = re.sub(r'\bobject\s+' + re.escape(target_obj) + r'\b', 'object Solution', code, count=1)
        if target_obj != 'Solution':
            code = re.sub(r'\b' + re.escape(target_obj) + r'\b', 'Solution', code)
        if not code.endswith('\n'):
            code += '\n'
    else:
        code = f"object Solution {{\n{code}\n}}\n"
    return code

def count_loc(code):
    return len([line for line in code.splitlines() if line.strip() and not line.strip().startswith(('#', '//'))])

def run_python_test(prompt_id, file_path):
    problem_id = prompt_id.split('_')[0].lower() # p01
    test_dir = BASE_DIR / 'unit_tests' / 'python'
    test_files = list(test_dir.glob(f"test_{problem_id}*.py"))
    
    if not test_files:
        return 'NO', '0.0', 'Test file not found'
        
    test_file = test_files[0]
    env = os.environ.copy()
    env['SOLUTION_PATH'] = str(file_path.absolute())
    
    try:
        result = subprocess.run([sys.executable, str(test_file)], env=env, capture_output=True, text=True, timeout=10)
        if "RESULTADO FINAL" in result.stdout:
            match = re.search(r'tests_passed_ratio:\s*([\d\.]+)', result.stdout)
            if match:
                return 'YES', match.group(1), ''
            return 'YES', '0.0', 'Could not parse ratio'
        else:
            return 'NO', '0.0', result.stderr.strip()[:200]
    except subprocess.TimeoutExpired:
        return 'NO', '0.0', 'Timeout'
    except Exception as e:
        return 'NO', '0.0', str(e)

def run_java_test(prompt_id, file_path, target_version):
    problem_id = prompt_id.split('_')[0].lower()
    test_class = TEST_MAP_JAVA.get(problem_id)
    if not test_class:
        return 'NO', '0.0', 'Test map not found'
        
    code = prepare_java_code(file_path.read_text('utf-8'))
        
    dest = BASE_DIR / 'unit_tests' / 'java' / 'src' / 'main' / 'java' / 'Solution.java'
    if dest.exists(): dest.unlink()
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_text(code, 'utf-8')
    
    test_dir = BASE_DIR / 'unit_tests' / 'java' / 'src' / 'test' / 'java'
    for tf in test_dir.glob('*.java'):
        if tf.stem != test_class:
            tf.rename(tf.with_suffix('.java.bak'))
    
    ver = target_version
    if '->' in ver: ver = ver.split('->')[1].strip()
    if ver == 'cualquiera': ver = '21'
    if ver not in ['8', '17', '21']: ver = '21'
    
    try:
        result = subprocess.run(
            ['mvn.cmd', 'test', f'-Dtest={test_class}', f'-Dmaven.compiler.release={ver}'],
            cwd=str(BASE_DIR / 'unit_tests' / 'java'),
            capture_output=True, text=True, timeout=30
        )
        dest.unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
        out = result.stdout
        if "COMPILATION ERROR" in out:
            return 'NO', '0.0', 'Compilation Error'
        
        match = re.search(r'Tests run:\s*(\d+),\s*Failures:\s*(\d+),\s*Errors:\s*(\d+)', out)
        if match:
            total = int(match.group(1))
            failures = int(match.group(2))
            errors = int(match.group(3))
            passed = total - failures - errors
            ratio = round(passed / total, 2) if total > 0 else 0.0
            return 'YES', str(ratio), ''
        return 'NO', '0.0', 'Test run failed or could not parse'
    except subprocess.TimeoutExpired:
        dest.unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
        return 'NO', '0.0', 'Timeout'
    except Exception as e:
        dest.unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
        return 'NO', '0.0', str(e)

def run_scala_test(prompt_id, file_path, target_version):
    problem_id = prompt_id.split('_')[0].lower()
    test_class = TEST_MAP_SCALA.get(problem_id)
    if not test_class:
        return 'NO', '0.0', 'Test map not found'
        
    code = prepare_scala_code(file_path.read_text('utf-8'), prompt_id)
        
    dest = BASE_DIR / 'unit_tests' / 'scala' / 'src' / 'main' / 'scala' / 'Solution.scala'
    if dest.exists(): dest.unlink()
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_text(code, 'utf-8')
    
    test_dir = BASE_DIR / 'unit_tests' / 'scala' / 'src' / 'test' / 'scala'
    for tf in test_dir.glob('*.scala'):
        if tf.stem != test_class:
            tf.rename(tf.with_suffix('.scala.bak'))
    
    ver = target_version
    if '->' in ver: ver = ver.split('->')[1].strip()
    if ver == 'cualquiera': ver = '3.6.0'
    if ver == '2.13': ver = '2.13.12'
    elif ver == '3.3': ver = '3.3.3'
    elif ver == '3.6': ver = '3.6.0'
    
    try:
        result = subprocess.run(
            ['sbt.bat', f'++ {ver}', f'testOnly *{test_class}'],
            cwd=str(BASE_DIR / 'unit_tests' / 'scala'),
            capture_output=True, text=True, timeout=45
        )
        dest.unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
        out = result.stdout
        
        match = re.search(r'Tests: succeeded (\d+), failed (\d+)', out)
        if match:
            succeeded = int(match.group(1))
            failed = int(match.group(2))
            total = succeeded + failed
            ratio = round(succeeded / total, 2) if total > 0 else 0.0
            return 'YES', str(ratio), ''
            
        if "Failed to compile" in out or "[error]" in out:
            return 'NO', '0.0', 'Compilation Error'
            
        return 'NO', '0.0', 'Test run failed or could not parse'
    except subprocess.TimeoutExpired:
        dest.unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
        return 'NO', '0.0', 'Timeout'
    except Exception as e:
        dest.unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'): tf.rename(tf.with_suffix(''))
        return 'NO', '0.0', str(e)


def build():
    with open(PROMPTS_JSON, 'r', encoding='utf-8') as f:
        prompts_data = json.load(f)
    
    prompt_map = {p['id']: p for p in prompts_data}
    dataset = []
    
    count = 0

    for model_folder, (model_name, tier) in MODELS_TIER.items():
        folder_path = RESPONSES_DIR / model_folder
        if not folder_path.exists():
            continue
            
        for file_path in folder_path.glob('*.*'):
            if not file_path.is_file():
                continue
                
            prompt_id = file_path.stem
            if prompt_id not in prompt_map:
                continue
                
            p = prompt_map[prompt_id]
            with open(file_path, 'r', encoding='utf-8') as f:
                code = f.read()
                
            loc = count_loc(code)
            lang = p['language'].lower()
            
            if lang == 'python':
                compiles_or_runs, tests_passed_ratio, notes = run_python_test(prompt_id, file_path)
            elif lang == 'java':
                compiles_or_runs, tests_passed_ratio, notes = run_java_test(prompt_id, file_path, p['version'])
            elif lang == 'scala':
                compiles_or_runs, tests_passed_ratio, notes = run_scala_test(prompt_id, file_path, p['version'])
            else:
                compiles_or_runs, tests_passed_ratio, notes = 'N/A', '0.0', 'Lang not supported'

            dataset.append({
                'prompt_id': prompt_id,
                'problem_name': p['problem_name'],
                'language': p['language'],
                'target_version': p['version'],
                'prompt_type': p['type'],
                'paradigm_requested': p['paradigm'],
                'ai_model_name': model_name,
                'ai_tier': tier,
                'loc': loc,
                'compiles_or_runs': compiles_or_runs,
                'tests_passed_ratio': tests_passed_ratio,
                'notes': notes
            })
            
            count += 1
            if count % 10 == 0:
                print(f"Procesados {count} archivos...")

    with open(OUTPUT_CSV, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=COLUMNS)
        writer.writeheader()
        writer.writerows(dataset)
        
    print(f"Dataset final generado con {len(dataset)} filas en {OUTPUT_CSV}")

if __name__ == '__main__':
    build()
