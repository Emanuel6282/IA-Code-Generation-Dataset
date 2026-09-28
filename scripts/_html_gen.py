import json
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent
json_path = BASE_DIR / 'prompts' / 'prompts_data.json'
html_path = BASE_DIR / 'prompts' / 'prompt_browser.html'

all_prompts = json.loads(json_path.read_text(encoding='utf-8'))
prompts_json = json.dumps(all_prompts, ensure_ascii=False)
prompts_json = prompts_json.replace('</script>', '<\\/script>')

html = """<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Navegador de Prompts - Dataset Tesis</title>
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap');
:root{--bg:#0f1117;--surf:#1a1d2e;--surf2:#252840;--brd:#2d3154;--acc:#7c6af7;--acc2:#56cfb2;--txt:#e2e4f0;--dim:#8b8fa8;--ok:#4ade80;}
*{box-sizing:border-box;margin:0;padding:0;}
body{font-family:'Inter',sans-serif;background:var(--bg);color:var(--txt);height:100vh;display:flex;flex-direction:column;overflow:hidden;}
header{background:var(--surf);border-bottom:1px solid var(--brd);padding:12px 20px;display:flex;align-items:center;justify-content:space-between;gap:12px;flex-wrap:wrap;flex-shrink:0;}
.h-title{font-size:1rem;font-weight:700;color:var(--acc);}
.h-sub{font-size:0.75rem;color:var(--dim);}
.stats{display:flex;gap:20px;}
.stat{text-align:center;}
.sv{font-size:1.3rem;font-weight:700;color:var(--acc2);}
.sl{font-size:0.65rem;color:var(--dim);text-transform:uppercase;letter-spacing:.06em;}
.layout{display:flex;flex:1;overflow:hidden;}
.sidebar{width:280px;min-width:240px;background:var(--surf);border-right:1px solid var(--brd);display:flex;flex-direction:column;overflow:hidden;}
.filters{padding:12px;border-bottom:1px solid var(--brd);}
.fl{font-size:.65rem;text-transform:uppercase;letter-spacing:.08em;color:var(--dim);margin:8px 0 5px;}
.fg{display:flex;flex-wrap:wrap;gap:5px;margin-bottom:6px;}
.fb{padding:3px 9px;border-radius:20px;border:1px solid var(--brd);background:transparent;color:var(--dim);font-size:.72rem;cursor:pointer;transition:all .15s;font-family:'Inter',sans-serif;}
.fb:hover{border-color:var(--acc);color:var(--txt);}
.fb.on{background:var(--acc);border-color:var(--acc);color:#fff;font-weight:600;}
.sb{padding:7px 10px;background:var(--surf2);border:1px solid var(--brd);border-radius:7px;color:var(--txt);font-size:.82rem;width:100%;outline:none;font-family:'Inter',sans-serif;}
.sb:focus{border-color:var(--acc);}
.plist{flex:1;overflow-y:auto;padding:6px;}
.plist::-webkit-scrollbar{width:3px;}
.plist::-webkit-scrollbar-thumb{background:var(--brd);}
.pi{padding:9px 11px;border-radius:7px;cursor:pointer;margin-bottom:3px;border:1px solid transparent;transition:all .15s;}
.pi:hover{background:var(--surf2);border-color:var(--brd);}
.pi.sel{background:var(--surf2);border-color:var(--acc);}
.pid{font-size:.68rem;color:var(--acc);font-weight:600;font-family:'JetBrains Mono',monospace;}
.pnm{font-size:.78rem;font-weight:500;margin:2px 0;}
.pmt{font-size:.68rem;color:var(--dim);}
.badge{display:inline-block;padding:1px 6px;border-radius:10px;font-size:.63rem;font-weight:600;text-transform:uppercase;margin-right:3px;}
.bn{background:#1e3a5f;color:#60a5fa;}
.bvc{background:#3b1f5e;color:#c084fc;}
.br{background:#1e4a35;color:var(--acc2);}
.dots{display:flex;gap:3px;margin-top:4px;}
.dot{width:8px;height:8px;border-radius:50%;border:1px solid var(--brd);background:var(--surf);}
.dot.done{background:var(--ok);border-color:var(--ok);}
.panel{flex:1;display:flex;flex-direction:column;overflow:hidden;padding:16px;gap:12px;}
.phead{background:var(--surf);border:1px solid var(--brd);border-radius:10px;padding:14px 18px;display:flex;justify-content:space-between;align-items:flex-start;gap:12px;flex-wrap:wrap;flex-shrink:0;}
.phead h2{font-size:1rem;font-weight:700;margin-bottom:5px;}
.tags{display:flex;gap:6px;flex-wrap:wrap;}
.tag{padding:2px 9px;border-radius:20px;font-size:.7rem;font-weight:500;border:1px solid var(--brd);color:var(--dim);}
.pbr{height:3px;background:var(--brd);border-radius:2px;margin-top:8px;width:180px;}
.pbf{height:100%;background:linear-gradient(90deg,var(--acc),var(--acc2));border-radius:2px;transition:width .3s;}
.actions{display:flex;flex-direction:column;gap:8px;align-items:flex-end;}
.btn{padding:8px 15px;border-radius:7px;border:none;font-size:.8rem;font-weight:600;cursor:pointer;font-family:'Inter',sans-serif;transition:all .15s;display:flex;align-items:center;gap:5px;}
.btn-p{background:var(--acc);color:#fff;}
.btn-p:hover{opacity:.85;transform:translateY(-1px);}
.mcl{display:flex;gap:6px;flex-wrap:wrap;}
.ml{display:flex;align-items:center;gap:5px;padding:4px 9px;border-radius:7px;border:1px solid var(--brd);cursor:pointer;font-size:.72rem;font-family:'Inter',sans-serif;user-select:none;transition:all .15s;}
.ml:hover{border-color:var(--acc2);}
.ml.chk{background:#0d2e25;border-color:var(--acc2);color:var(--acc2);}
.ml input{display:none;}
.ta-wrap{flex:1;display:flex;flex-direction:column;background:var(--surf);border:1px solid var(--brd);border-radius:10px;overflow:hidden;min-height:0;}
.ta-bar{padding:8px 14px;border-bottom:1px solid var(--brd);display:flex;justify-content:space-between;align-items:center;flex-shrink:0;}
.ta-lbl{font-size:.7rem;color:var(--dim);font-family:'JetBrains Mono',monospace;}
.cc{font-size:.68rem;color:var(--dim);}
#ta{flex:1;background:transparent;border:none;padding:14px;color:var(--txt);font-family:'JetBrains Mono',monospace;font-size:.8rem;line-height:1.6;resize:none;outline:none;overflow-y:auto;}
#ta::-webkit-scrollbar{width:3px;}
#ta::-webkit-scrollbar-thumb{background:var(--brd);}
.empty{flex:1;display:flex;flex-direction:column;align-items:center;justify-content:center;color:var(--dim);gap:10px;}
.empty-icon{font-size:2.5rem;}
.toast{position:fixed;bottom:20px;right:20px;background:var(--ok);color:#064e3b;padding:9px 18px;border-radius:7px;font-weight:600;font-size:.82rem;transform:translateY(60px);opacity:0;transition:all .3s;z-index:999;}
.toast.show{transform:translateY(0);opacity:1;}
</style>
</head>
<body>
<header>
  <div>
    <div class="h-title">Navegador de Prompts &mdash; Dataset Tesis</div>
    <div class="h-sub">Selecciona un prompt, copialo y pegalo en ChatGPT / Claude / Gemini</div>
  </div>
  <div class="stats">
    <div class="stat"><div class="sv" id="s-total">189</div><div class="sl">Prompts</div></div>
    <div class="stat"><div class="sv" id="s-done">0</div><div class="sl">Completos</div></div>
    <div class="stat"><div class="sv" id="s-pct">0%</div><div class="sl">Progreso</div></div>
  </div>
</header>
<div class="layout">
  <div class="sidebar">
    <div class="filters">
      <div class="fl">Problema</div>
      <div class="fg" id="fp">
        <button class="fb on" data-f="problem" data-v="all">Todos</button>
        <button class="fb" data-f="problem" data-v="P01">P01</button>
        <button class="fb" data-f="problem" data-v="P02">P02</button>
        <button class="fb" data-f="problem" data-v="P03">P03</button>
        <button class="fb" data-f="problem" data-v="P04">P04</button>
        <button class="fb" data-f="problem" data-v="P05">P05</button>
        <button class="fb" data-f="problem" data-v="P06">P06</button>
        <button class="fb" data-f="problem" data-v="P07">P07</button>
      </div>
      <div class="fl">Lenguaje</div>
      <div class="fg">
        <button class="fb on" data-f="lang" data-v="all">Todos</button>
        <button class="fb" data-f="lang" data-v="Python">Python</button>
        <button class="fb" data-f="lang" data-v="Java">Java</button>
        <button class="fb" data-f="lang" data-v="Scala">Scala</button>
      </div>
      <div class="fl">Tipo</div>
      <div class="fg">
        <button class="fb on" data-f="type" data-v="all">Todos</button>
        <button class="fb" data-f="type" data-v="neutral">Neutro</button>
        <button class="fb" data-f="type" data-v="version_constrained">Restringido</button>
        <button class="fb" data-f="type" data-v="refactor">Refactor</button>
      </div>
      <input class="sb" type="text" id="si" placeholder="Buscar...">
    </div>
    <div class="plist" id="pl"></div>
  </div>
  <div class="panel" id="panel">
    <div class="empty" id="empty"><div class="empty-icon">📋</div><div>Selecciona un prompt de la lista</div></div>
    <div id="view" style="display:none;flex:1;flex-direction:column;gap:12px;min-height:0;">
      <div class="phead">
        <div>
          <h2 id="v-title">-</h2>
          <div class="tags" id="v-tags"></div>
          <div class="pbr"><div class="pbf" id="v-pb" style="width:0%"></div></div>
          <div style="margin-top:12px;font-size:0.75rem;color:var(--dim);display:flex;align-items:center;gap:6px;">
            Guardar como: <code id="v-fn" style="background:var(--surf2);padding:3px 7px;border-radius:4px;color:var(--acc2);font-family:'JetBrains Mono',monospace;"></code>
            <button class="btn" style="padding:4px 8px;font-size:0.65rem;background:var(--surf2);color:var(--txt);border:1px solid var(--brd);" onclick="copyFn(this)">📋</button>
          </div>
        </div>
        <div class="actions">
          <button class="btn btn-p" onclick="copy()">📋 Copiar prompt</button>
          <div class="mcl" id="mcl"></div>
        </div>
      </div>
      <div class="ta-wrap">
        <div class="ta-bar"><span class="ta-lbl">PROMPT COMPLETO</span><span class="cc" id="cc">0 chars</span></div>
        <textarea id="ta" readonly></textarea>
      </div>
    </div>
  </div>
</div>
<div class="toast" id="toast">Copiado al portapapeles</div>
<script>
const P=__PROMPTS_JSON__;
const M=['ChatGPT Free','ChatGPT Plus','Gemini Free','Gemini Advanced','Claude Free','Claude Pro'];
const F={problem:'all',lang:'all',type:'all'};
let q='',sel=null,D=JSON.parse(localStorage.getItem('td')||'{}');
const save=()=>localStorage.setItem('td',JSON.stringify(D));
const filt=()=>P.filter(p=>(F.problem==='all'||p.problem_id===F.problem)&&(F.lang==='all'||p.language===F.lang)&&(F.type==='all'||p.type===F.type)&&(!q||p.problem_name.toLowerCase().includes(q)||p.id.toLowerCase().includes(q)||p.language.toLowerCase().includes(q)));
function renderList(){
  const l=document.getElementById('pl');l.innerHTML='';
  filt().forEach(p=>{
    const done=D[p.id]||{};const dc=M.filter(m=>done[m]).length;
    const d=document.createElement('div');d.className='pi'+(p.id===sel?' sel':'');d.onclick=()=>select(p.id);
    const bc=p.type==='neutral'?'bn':p.type==='version_constrained'?'bvc':'br';
    const bl=p.type==='neutral'?'neutro':p.type==='version_constrained'?'restringido':'refactor';
    d.innerHTML=`<div class="pid">${p.problem_id} · ${p.language}</div><div class="pnm">${p.problem_name}</div><div class="pmt"><span class="badge ${bc}">${bl}</span>${p.version!=='cualquiera'&&p.version!=='N/A'?p.version:''} ${p.paradigm!=='neutral'&&p.paradigm!=='N/A'?'· '+p.paradigm:''}</div><div class="dots">${M.map(m=>`<div class="dot${done[m]?' done':''}" title="${m}"></div>`).join('')}</div>`;
    l.appendChild(d);
  });
  updateStats();
}
function select(id){
  sel=id;const p=P.find(x=>x.id===id);if(!p)return;
  document.getElementById('empty').style.display='none';
  const v=document.getElementById('view');v.style.display='flex';
  document.getElementById('v-title').textContent=p.problem_id+' · '+p.problem_name;
  const bc=p.type==='neutral'?'bn':p.type==='version_constrained'?'bvc':'br';
  const bl=p.type==='neutral'?'Neutro':p.type==='version_constrained'?'Restringido':'Refactor';
  document.getElementById('v-tags').innerHTML=`<span class="tag">${p.language}</span><span class="tag">${p.version}</span><span class="tag badge ${bc}">${bl}</span>${p.paradigm!=='neutral'&&p.paradigm!=='N/A'?`<span class="tag">${p.paradigm}</span>`:''}`;
  
  const ext = p.language==='Python'?'.py':p.language==='Java'?'.java':'.scala';
  document.getElementById('v-fn').textContent = p.id + ext;
  
  document.getElementById('ta').value=p.prompt;
  document.getElementById('cc').textContent=p.prompt.length+' chars';
  renderChecklist(id);renderList();
}
function renderChecklist(id){
  const done=D[id]||{};const cl=document.getElementById('mcl');cl.innerHTML='';
  M.forEach(m=>{
    const l=document.createElement('label');l.className='ml'+(done[m]?' chk':'');
    l.innerHTML=`<input type="checkbox" ${done[m]?'checked':''} onchange="toggle('${id}','${m}',this.checked)"><span>${done[m]?'✅':'⬜'}</span><span>${m}</span>`;
    cl.appendChild(l);
  });
  const dc=M.filter(m=>done[m]).length;
  document.getElementById('v-pb').style.width=(dc/M.length*100)+'%';
}
function toggle(id,m,v){if(!D[id])D[id]={};D[id][m]=v;save();renderChecklist(id);renderList();}
function updateStats(){
  let done=0;P.forEach(p=>{const d=D[p.id]||{};if(M.every(m=>d[m]))done++;});
  document.getElementById('s-done').textContent=done;
  document.getElementById('s-pct').textContent=Math.round(done/P.length*100)+'%';
}
function copy(){
  navigator.clipboard.writeText(document.getElementById('ta').value).then(()=>{
    const t=document.getElementById('toast');t.classList.add('show');
    setTimeout(()=>t.classList.remove('show'),2000);
    const b=document.querySelector('.btn-p');b.textContent='Copiado!';
    setTimeout(()=>b.innerHTML='📋 Copiar prompt',1500);
  });
}
function copyFn(btn){
  navigator.clipboard.writeText(document.getElementById('v-fn').textContent).then(()=>{
    const old = btn.innerHTML; btn.innerHTML='✅'; setTimeout(()=>btn.innerHTML=old, 1500);
  });
}
document.querySelectorAll('.fb').forEach(b=>b.onclick=()=>{
  const f=b.dataset.f,v=b.dataset.v;F[f]=v;
  document.querySelectorAll(`[data-f="${f}"]`).forEach(x=>x.classList.remove('on'));
  b.classList.add('on');renderList();
});
document.getElementById('si').oninput=e=>{q=e.target.value.toLowerCase();renderList();};
renderList();
</script>
</body></html>"""

html = html.replace('__PROMPTS_JSON__', prompts_json)
html_path.write_text(html, encoding='utf-8')
print(f'HTML browser generado: {html_path}')
print(f'Tamanio: {html_path.stat().st_size // 1024} KB')
