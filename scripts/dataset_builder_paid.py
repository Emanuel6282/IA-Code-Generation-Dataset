import os
import json
import csv
import re
import sys
import subprocess
import shutil
import time
import threading
from pathlib import Path
from dataset_builder import count_loc, run_python_test, TEST_MAP_JAVA, TEST_MAP_SCALA, prepare_java_code, prepare_scala_code

BASE_DIR = Path(__file__).resolve().parent.parent
RESPONSES_DIR = BASE_DIR / 'llm_responses'
PROMPTS_JSON = BASE_DIR / 'prompts' / 'prompts_data.json'
OUTPUT_CSV = BASE_DIR / 'dataset_paid_models.csv'

MODELS_TIER = {
    'chatgpt-plus': ('ChatGPT Plus', 'paid'),
    'gemini-pro': ('Gemini Pro', 'paid'),
    'claude-pro': ('Claude Pro', 'paid')
}

COLUMNS = [
    'prompt_id', 'problem_name', 'language', 'target_version', 'prompt_type', 
    'paradigm_requested', 'ai_model_name', 'ai_tier', 'loc',
    'compiles_or_runs', 'tests_passed_ratio', 'notes'
]

ANSI_RE = re.compile(r'\x1b\[[0-9;]*[a-zA-Z]')

def run_java_test_in_dir(java_dir, prompt_id, file_path, target_version):
    problem_id = prompt_id.split('_')[0].lower()
    test_class = TEST_MAP_JAVA.get(problem_id)
    if not test_class:
        return 'NO', '0.0', 'Test map not found'
        
    code = prepare_java_code(file_path.read_text('utf-8'))
        
    dest = java_dir / 'src' / 'main' / 'java' / 'Solution.java'
    if dest.exists(): dest.unlink()
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_text(code, 'utf-8')
    
    test_dir = java_dir / 'src' / 'test' / 'java'
    for tf in test_dir.glob('*.java'):
        if tf.stem != test_class:
            tf.rename(tf.with_suffix('.java.bak'))
    
    ver = target_version
    if '->' in ver: ver = ver.split('->')[1].strip()
    if ver == 'cualquiera': ver = '21'
    if ver not in ['8', '17', '21']: ver = '21'
    
    try:
        result = subprocess.run(
            ['mvn.cmd', '-o', 'test', f'-Dtest={test_class}', f'-Dmaven.compiler.release={ver}'],
            cwd=str(java_dir),
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


def scala_eff_ver(v):
    if '->' in v: v = v.split('->')[1].strip()
    if v == 'cualquiera' or v == '3.6': return '3.6.0'
    if v == '2.13': return '2.13.12'
    if v == '3.3': return '3.3.3'
    return '3.6.0'


def evaluate_scala_batch(scala_tasks, on_result):
    scala_dir = BASE_DIR / 'unit_tests' / 'scala'
    test_dir = scala_dir / 'src' / 'test' / 'scala'
    sbt_bin = shutil.which('sbt.bat') or 'sbt.bat'

    scala_tasks_sorted = sorted(
        scala_tasks,
        key=lambda t: (scala_eff_ver(t['target_version']), t['prompt_id'], t['ai_model_name'])
    )

    proc = subprocess.Popen(
        [sbt_bin, '-Dsbt.color=false'],
        cwd=str(scala_dir),
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
        bufsize=1
    )

    for line in proc.stdout:
        if 'set current project to scala-unit-tests' in ANSI_RE.sub('', line):
            break

    counter = [900000]
    def run_cmd(cmd):
        counter[0] += 1
        m = counter[0]
        proc.stdin.write(cmd + '\n')
        proc.stdin.flush()
        proc.stdin.write(f'eval {m}\n')
        proc.stdin.flush()
        out = []
        target = f'ans: Int = {m}'
        for line in proc.stdout:
            clean = ANSI_RE.sub('', line)
            out.append(clean)
            if target in clean:
                break
        return ''.join(out)

    cur_ver = [None]
    try:
        for t in scala_tasks_sorted:
            ver = scala_eff_ver(t['target_version'])
            if cur_ver[0] != ver:
                run_cmd(f'++ {ver}')
                cur_ver[0] = ver

            for cf in (scala_dir / 'target').rglob('Solution*'):
                if cf.is_file():
                    cf.unlink(missing_ok=True)

            pid = t['prompt_id']
            test_class = TEST_MAP_SCALA[pid.split('_')[0].lower()]
            for tf in test_dir.glob('*.scala.bak'):
                tf.rename(tf.with_suffix(''))
            for tf in test_dir.glob('*.scala'):
                if tf.stem != test_class:
                    tf.rename(tf.with_suffix('.scala.bak'))

            code = prepare_scala_code(t['file_path'].read_text('utf-8'), pid)

            dest = scala_dir / 'src' / 'main' / 'scala' / 'Solution.scala'
            if dest.exists():
                dest.unlink()
            dest.parent.mkdir(parents=True, exist_ok=True)
            dest.write_text(code, 'utf-8')
            future_t = time.time() + (counter[0] - 900000) * 10
            os.utime(dest, (future_t, future_t))

            out = run_cmd(f'testOnly *{test_class}')
            dest.unlink(missing_ok=True)
            for tf in test_dir.glob('*.bak'):
                tf.rename(tf.with_suffix(''))

            match = re.search(r'Tests: succeeded (\d+), failed (\d+)', out)
            if match:
                succeeded = int(match.group(1))
                failed = int(match.group(2))
                total = succeeded + failed
                ratio = round(succeeded / total, 2) if total > 0 else 0.0
                c, r_str, notes = 'YES', str(ratio), ''
            elif "Failed to compile" in out or "[error]" in out:
                c, r_str, notes = 'NO', '0.0', 'Compilation Error'
            else:
                c, r_str, notes = 'NO', '0.0', 'Test run failed or could not parse'

            on_result(t, c, r_str, notes)
    finally:
        (scala_dir / 'src' / 'main' / 'scala' / 'Solution.scala').unlink(missing_ok=True)
        for tf in test_dir.glob('*.bak'):
            tf.rename(tf.with_suffix(''))
        try:
            proc.stdin.write('exit\n')
            proc.stdin.flush()
            proc.terminate()
        except Exception:
            pass


def build(reuse_java_from_existing=False):
    t_start = time.time()
    with open(PROMPTS_JSON, 'r', encoding='utf-8') as f:
        prompts_data = json.load(f)
    prompt_map = {p['id']: p for p in prompts_data}

    existing_java = {}
    if reuse_java_from_existing and OUTPUT_CSV.exists():
        with open(OUTPUT_CSV, 'r', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                if row['language'].lower() == 'java':
                    existing_java[(row['ai_model_name'], row['prompt_id'])] = (
                        row['compiles_or_runs'], row['tests_passed_ratio'], row['notes']
                    )

    tasks = []
    model_order = {'ChatGPT Plus': 0, 'Gemini Pro': 1, 'Claude Pro': 2}

    for model_folder, (model_name, tier) in MODELS_TIER.items():
        folder_path = RESPONSES_DIR / model_folder
        if not folder_path.exists():
            continue
        for file_path in sorted(folder_path.glob('*.*')):
            if not file_path.is_file():
                continue
            prompt_id = file_path.stem
            if prompt_id not in prompt_map:
                continue
            p = prompt_map[prompt_id]
            code = file_path.read_text('utf-8')
            loc = count_loc(code)
            tasks.append({
                'prompt_id': prompt_id,
                'problem_name': p['problem_name'],
                'language': p['language'],
                'target_version': p['version'],
                'prompt_type': p['type'],
                'paradigm_requested': p['paradigm'],
                'ai_model_name': model_name,
                'ai_tier': tier,
                'loc': loc,
                'file_path': file_path
            })

    py_tasks = [t for t in tasks if t['language'].lower() == 'python']
    java_tasks = [t for t in tasks if t['language'].lower() == 'java']
    scala_tasks = [t for t in tasks if t['language'].lower() == 'scala']

    print(f"Total archivos a evaluar: {len(tasks)} (Python: {len(py_tasks)}, Java: {len(java_tasks)}, Scala: {len(scala_tasks)})", flush=True)

    results = []
    lock = threading.Lock()
    done_count = [0]

    def record(item, compiles, ratio, notes):
        row = {k: item[k] for k in [
            'prompt_id', 'problem_name', 'language', 'target_version',
            'prompt_type', 'paradigm_requested', 'ai_model_name', 'ai_tier', 'loc'
        ]}
        row['compiles_or_runs'] = compiles
        row['tests_passed_ratio'] = ratio
        row['notes'] = notes
        with lock:
            results.append(row)
            done_count[0] += 1
            if done_count[0] % 25 == 0 or done_count[0] == len(tasks):
                elapsed = time.time() - t_start
                print(f"[{elapsed:5.1f}s] Procesados {done_count[0]}/{len(tasks)} archivos...", flush=True)

    for t in py_tasks:
        c, r, n = run_python_test(t['prompt_id'], t['file_path'])
        record(t, c, r, n)

    if reuse_java_from_existing and len(existing_java) == len(java_tasks):
        for t in java_tasks:
            c, r, n = existing_java[(t['ai_model_name'], t['prompt_id'])]
            record(t, c, r, n)
    else:
        scratch_java = BASE_DIR / 'unit_tests' / '_java_workers_tmp'
        if scratch_java.exists():
            shutil.rmtree(scratch_java, ignore_errors=True)
        scratch_java.mkdir(parents=True, exist_ok=True)
        java_dirs = []
        for i in range(3):
            jd = scratch_java / f'java_{i}'
            shutil.copytree(BASE_DIR / 'unit_tests' / 'java', jd)
            java_dirs.append(jd)

        def run_java_partition(worker_idx, subset):
            jd = java_dirs[worker_idx]
            for t in subset:
                c, r, n = run_java_test_in_dir(jd, t['prompt_id'], t['file_path'], t['target_version'])
                record(t, c, r, n)

        threads = []
        for i in range(3):
            subset = java_tasks[i::3]
            threads.append(threading.Thread(target=run_java_partition, args=(i, subset)))
        for th in threads:
            th.start()
        for th in threads:
            th.join()
        shutil.rmtree(scratch_java, ignore_errors=True)

    evaluate_scala_batch(scala_tasks, record)

    results.sort(key=lambda r: (model_order.get(r['ai_model_name'], 99), r['prompt_id']))

    with open(OUTPUT_CSV, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=COLUMNS)
        writer.writeheader()
        writer.writerows(results)

    print(f"Dataset final generado con {len(results)} filas en {OUTPUT_CSV} en {time.time() - t_start:.1f}s", flush=True)

if __name__ == '__main__':
    reuse = '--reuse-java' in sys.argv
    build(reuse_java_from_existing=reuse)
