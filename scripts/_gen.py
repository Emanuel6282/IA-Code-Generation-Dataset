import os, json, csv
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
NEUTRAL_DIR = BASE_DIR / 'prompts' / 'neutral'
BASE_CODE_DIR = BASE_DIR / 'base_code'
OUTPUT_DIR = BASE_DIR / 'prompts' / 'generated'
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

PROBLEMS = {
    'P01':{'name':'Word Frequency','func_names':{'python':'word_frequency','java':'wordFrequency','scala':'wordFrequency'},'neutral_file':'p01_word_frequency.txt','base_files':{'python':'p01_word_frequency_v39.py','java':'P01WordFrequency_v8.java','scala':'P01WordFrequency_v213.scala'}},
    'P02':{'name':'Binary Search','func_names':{'python':'binary_search','java':'binarySearch','scala':'binarySearch'},'neutral_file':'p02_binary_search.txt','base_files':{'python':'p02_binary_search_v39.py','java':'P02BinarySearch_v8.java','scala':'P02BinarySearch_v213.scala'}},
    'P03':{'name':'Flatten Nested List','func_names':{'python':'flatten','java':'flatten','scala':'flatten'},'neutral_file':'p03_flatten_list.txt','base_files':{'python':'p03_flatten_list_v39.py','java':'P03FlattenList_v8.java','scala':'P03FlattenList_v213.scala'}},
    'P04':{'name':'Balanced Parentheses','func_names':{'python':'is_balanced','java':'isBalanced','scala':'isBalanced'},'neutral_file':'p04_balanced_parentheses.txt','base_files':{'python':'p04_balanced_parentheses_v39.py','java':'P04BalancedParentheses_v8.java','scala':'P04BalancedParentheses_v213.scala'}},
    'P05':{'name':'Transaction Pipeline','func_names':{'python':'process_transactions','java':'processTransactions','scala':'processTransactions'},'neutral_file':'p05_transaction_pipeline.txt','base_files':{'python':'p05_transaction_pipeline_v39.py','java':'P05TransactionPipeline_v8.java','scala':'P05TransactionPipeline_v213.scala'}},
    'P06':{'name':'Max Subarray Sum','func_names':{'python':'max_subarray_sum','java':'maxSubarraySum','scala':'maxSubarraySum'},'neutral_file':'p06_max_subarray.txt','base_files':{'python':'p06_max_subarray_v39.py','java':'P06MaxSubarray_v8.java','scala':'P06MaxSubarray_v213.scala'}},
    'P07':{'name':'2D Flood Fill','func_names':{'python':'flood_fill','java':'floodFill','scala':'floodFill'},'neutral_file':'p07_flood_fill.txt','base_files':{'python':'p07_flood_fill_v39.py','java':'P07FloodFill_v8.java','scala':'P07FloodFill_v213.scala'}},
}
LANGUAGES={'python':{'display':'Python','versions':['3.9','3.11','3.14']},'java':{'display':'Java','versions':['8','17','21']},'scala':{'display':'Scala','versions':['2.13','3.3','3.6']}}
VT={'python':[('3.9','3.11'),('3.11','3.14')],'java':[('8','17'),('17','21')],'scala':[('2.13','3.3'),('3.3','3.6')]}
PD={'python':{'iterativo':'Usa bucles for/while con variables mutables. Evita map(),filter(),reduce() y recursion.','funcional':'Usa inmutabilidad. Emplea map(),filter(),functools.reduce(). Evita for/while con estado mutable.'},'java':{'iterativo':'Usa bucles for/while, ArrayList, HashMap. Evita Streams, lambdas y recursion.','funcional':'Usa Stream API con lambdas. Usa Optional. Evita bucles for/while imperativos.'},'scala':{'iterativo':'Usa var, mutable.ListBuffer, mutable.Map, while. Evita map(),filter(),foldLeft() y recursion.','funcional':'Usa val, colecciones inmutables, map(),filter(),foldLeft(),flatMap(). Usa @tailrec. Evita var y mutable.*.'}}
MODELS=['ChatGPT Free','ChatGPT Plus','Gemini Free','Gemini Advanced','Claude Free','Claude Pro']

def rn(pid): p=NEUTRAL_DIR/PROBLEMS[pid]['neutral_file']; return p.read_text(encoding='utf-8') if p.exists() else '[ERR]'
def rb(pid,lang):
    fname=PROBLEMS[pid]['base_files'][lang]; p=BASE_CODE_DIR/lang/fname
    if not p.exists(): return '[ERR]'
    lines=[l for l in p.read_text(encoding='utf-8').splitlines() if not l.startswith('// P0') and not l.startswith('# P0')]
    return '\n'.join(lines).strip()

def neutral(pid,lang):
    ld=LANGUAGES[lang]['display']; fn=PROBLEMS[pid]['func_names'][lang]
    return rn(pid).replace('{LANGUAGE}',ld).replace('{FUNC_NAME}',fn).strip()

def vc(pid,lang,ver,par):
    ld=LANGUAGES[lang]['display']; fn=PROBLEMS[pid]['func_names'][lang]
    pd=PD[lang][par]; pn=PROBLEMS[pid]['name']
    raw=rn(pid).replace('{LANGUAGE}',ld).replace('{FUNC_NAME}',fn)
    return f"Resuelve el siguiente problema en {ld} version {ver}, usando ESTRICTAMENTE el paradigma {par}.\n\nProblema: {pn}\n{raw}\n\n=== REQUISITOS ADICIONALES ===\n\n1. LENGUAJE Y VERSION: Usa EXCLUSIVAMENTE {ld} {ver}. Usa las caracteristicas nuevas de {ver} cuando esten disponibles.\n\n2. PARADIGMA {par.upper()}: {pd}\n\n3. NOMBRE DE FUNCION: Exactamente \"{fn}\".\n\n4. BIBLIOTECA ESTANDAR UNICAMENTE: No uses librerias de terceros.\n\n5. TESTS: Incluye al final 3 casos de prueba usando el mecanismo estandar de {ld}.".strip()

def refactor(pid,lang,ov,nv):
    ld=LANGUAGES[lang]['display']; fn=PROBLEMS[pid]['func_names'][lang]
    code=rb(pid,lang); pn=PROBLEMS[pid]['name']
    return f"El siguiente codigo resuelve \"{pn}\" en {ld} version {ov}:\n\n```{lang}\n{code}\n```\n\nMigra este codigo a {ld} version {nv}:\n\n1. CARACTERISTICAS NUEVAS: Usa las de {ld} {nv} que mejoren legibilidad o rendimiento. Al inicio, en un comentario, lista cuales usaste y por que.\n\n2. COMPORTAMIENTO IDENTICO: Misma logica, mismos resultados.\n\n3. NOMBRE DE FUNCION: Debe permanecer exactamente \"{fn}\".\n\n4. MISMOS TESTS: Conserva los casos de prueba, adaptados a {ld} {nv}.\n\n5. EXPLICA TUS CAMBIOS: Al final en un comentario, enumera los cambios y por que son idiomaticos en {ld} {nv}.".strip()

all_prompts=[]
count=0
for pid in PROBLEMS:
    pn=PROBLEMS[pid]['name']
    for lang in LANGUAGES:
        ld=LANGUAGES[lang]['display']
        d=OUTPUT_DIR/pid/'neutral'; d.mkdir(parents=True,exist_ok=True)
        txt=neutral(pid,lang); (d/f'{pid}_{lang}_neutral.txt').write_text(txt,encoding='utf-8')
        all_prompts.append({'id':f'{pid}_{lang}_neutral','problem_id':pid,'problem_name':pn,'language':ld,'version':'cualquiera','type':'neutral','paradigm':'neutral','prompt':txt}); count+=1
        for ver in LANGUAGES[lang]['versions']:
            for par in ['iterativo','funcional']:
                d2=OUTPUT_DIR/pid/'version_constrained'/lang/ver; d2.mkdir(parents=True,exist_ok=True)
                txt2=vc(pid,lang,ver,par); (d2/f'{pid}_{lang}_{ver.replace(".","_")}_{par}.txt').write_text(txt2,encoding='utf-8')
                all_prompts.append({'id':f'{pid}_{lang}_{ver}_{par}','problem_id':pid,'problem_name':pn,'language':ld,'version':ver,'type':'version_constrained','paradigm':par,'prompt':txt2}); count+=1
        for (ov,nv) in VT[lang]:
            d3=OUTPUT_DIR/pid/'refactor'/lang; d3.mkdir(parents=True,exist_ok=True)
            txt3=refactor(pid,lang,ov,nv); (d3/f'{pid}_{lang}_{ov.replace(".","_")}_to_{nv.replace(".","_")}.txt').write_text(txt3,encoding='utf-8')
            all_prompts.append({'id':f'{pid}_{lang}_{ov}_to_{nv}','problem_id':pid,'problem_name':pn,'language':ld,'version':f'{ov} -> {nv}','type':'refactor','paradigm':'N/A','prompt':txt3}); count+=1

with open(BASE_DIR/'prompts'/'tracking.csv','w',newline='',encoding='utf-8') as f:
    w=csv.DictWriter(f,fieldnames=['prompt_id','problem','language','version','type','paradigm','ChatGPT Free','ChatGPT Plus','Gemini Free','Gemini Advanced','Claude Free','Claude Pro'])
    w.writeheader()
    for p in all_prompts: w.writerow({'prompt_id':p['id'],'problem':p['problem_name'],'language':p['language'],'version':p['version'],'type':p['type'],'paradigm':p['paradigm'],'ChatGPT Free':'','ChatGPT Plus':'','Gemini Free':'','Gemini Advanced':'','Claude Free':'','Claude Pro':''})

with open(BASE_DIR/'prompts'/'prompts_data.json','w',encoding='utf-8') as f: json.dump(all_prompts,f,ensure_ascii=False,indent=2)
print(f'OK: {count} prompts. tracking.csv y prompts_data.json listos.')
