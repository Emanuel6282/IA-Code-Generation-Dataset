# Dataset de Tesis — Análisis del código generado por Inteligencia Artificial según paradigma de programación, versión del lenguaje y tipo de modelo

## Objetivo
Analizar empíricamente la fiabilidad funcional (`pass@1`), los modos de fallo (compilación frente a lógica) y la concisión (`LOC`) del código generado por Inteligencia Artificial según el **paradigma de programación**, la **versión del lenguaje** y el **tipo de modelo** (gratuito frente a de paga).

---

## Modelos Evaluados
| Proveedor | Nivel Gratuito (`Free`)         | Nivel de Paga (`Paid`)         | Muestras |
|-----------|---------------------------------|--------------------------------|----------|
| OpenAI    | ChatGPT Free (`GPT-4o mini`)    | ChatGPT Plus (`GPT-6`)         | 378      |
| Google    | Gemini Free (`3.5 Flash`)       | Gemini Pro (`3.1 Pro`)         | 378      |
| Anthropic | Claude Free (`Sonnet 5`)        | Claude Pro (`Opus 5.5`)        | 378      |

---

## Lenguajes y Versiones
| Lenguaje | Versión Heredada ($v_1$) | Versión Intermedia ($v_2$) | Versión Moderna ($v_3$) |
|----------|--------------------------|----------------------------|-------------------------|
| Python   | 3.9                      | 3.11                       | 3.14                    |
| Java     | 8 (LTS)                  | 17 (LTS)                   | 21 (LTS)                |
| Scala    | 2.13 (`2.13.12`)         | 3.3 LTS (`3.3.3`)          | 3.6 (`3.6.0`)           |

---

## Problemas Algorítmicos (P01-P07)
| ID  | Nombre                              | Sesgo Natural | Casos de Prueba (TC) |
|-----|-------------------------------------|---------------|----------------------|
| P01 | Word Frequency                      | Funcional     | 5                    |
| P02 | Binary Search                       | Iterativo     | 7                    |
| P03 | Flatten Nested List                 | Funcional     | 6                    |
| P04 | Balanced Parentheses                | Neutro        | 8                    |
| P05 | Transaction Pipeline                | Funcional     | 5                    |
| P06 | Max Subarray Sum (Kadane)           | Iterativo     | 7                    |
| P07 | 2D Flood Fill                       | Iterativo     | 5                    |

---

## Construcción Paso a Paso del Dataset ($N = 1134$)
| Tipo de Prompt                    | 1 Lenguaje (7 problemas) | 3 Lenguajes ($\times 3$) | 6 Modelos ($\times 6$) |
|-----------------------------------|--------------------------|--------------------------|------------------------|
| Neutral                           | $7 \times 1 = 7$         | 21                       | 126                    |
| Restringido (Versión y Paradigma) | $7 \times 6 = 42$        | 126                      | 756                    |
| Refactorización Temporal          | $7 \times 2 = 14$        | 42                       | 252                    |
| **TOTAL GLOBAL**                  | **63 prompts**           | **189 prompts**          | **1134 muestras**      |

---

## Estructura de Directorios
```
Dataset/
├── README.md
├── requirements.txt
├── dataset_free_models.csv          <- Resultados empíricos de los 3 modelos gratuitos (567 filas)
├── dataset_paid_models.csv          <- Resultados empíricos de los 3 modelos de pago (567 filas)
├── dataset_all_models.csv           <- Corpus consolidado completo (1.134 filas)
├── dataset_template.csv             <- Plantilla base del esquema de datos
├── llm_responses/                   <- los 1.134 archivos de código generados por los 6 LLMs
│   ├── chatgpt-free/                <- 189 respuestas de ChatGPT Free (GPT-4o mini)
│   ├── gemini-free/                 <- 189 respuestas de Gemini Free (3.5 Flash)
│   ├── claude-free/                 <- 189 respuestas de Claude Free (Sonnet 5)
│   ├── chatgpt-plus/                <- 189 respuestas de ChatGPT Plus (GPT-6)
│   ├── gemini-pro/                  <- 189 respuestas de Gemini Pro (3.1 Pro)
│   └── claude-pro/                  <- 189 respuestas de Claude Pro (Opus 5.5)
├── prompts/
│   ├── neutral/                     <- 7 prompts (uno por problema)
│   ├── version_constrained/         <- Template + instrucciones
│   ├── refactor/                    <- Template + instrucciones
│   ├── generated/                   <- Los 189 prompts exactos en formato texto (.txt) listos para usar
│   ├── prompts_data.json            <- Base de datos con metadatos de cada prompt
│   └── prompt_browser.html          <- Interfaz visual para explorar los prompts interactivamente
├── unit_tests/
│   ├── test_cases.json              <- Especificación universal de casos de prueba
│   ├── python/                      <- 7 suites de prueba unittest (Python)
│   ├── java/                        <- 7 suites JUnit 5 + pom.xml (Maven)
│   └── scala/                       <- 7 suites ScalaTest + build.sbt (sbt)
├── base_code/
│   ├── python/                      <- Código base v3.9 para refactoring
│   ├── java/                        <- Código base Java 8 para refactoring
│   └── scala/                       <- Código base Scala 2.13 para refactoring
├── scripts/
│   ├── dataset_builder.py           <- Ejecutor de pruebas y constructor del dataset gratuito
│   ├── dataset_builder_paid.py      <- Ejecutor de pruebas y constructor del dataset de pago
│   ├── data_analysis.py             <- Generación de gráficos para modelos gratuitos (imagenes/)
│   ├── data_analysis_paid.py        <- Generación de gráficos para modelos de pago (imagenes/)
│   ├── data_analysis_combined.py    <- Generación de gráficos comparativos globales (imagenes/)
│   ├── statistical_validation.py    <- Validación estadística exploratoria (Free)
│   ├── statistical_validation_paid.py <- Validación estadística exploratoria (Paid)
│   └── statistical_validation_combined.py <- Validación estadística exploratoria global
├── imagenes/                        <- Las 10 figuras exportadas a 300 DPI utilizadas en la tesis
├── analysis_results_free/           <- Gráficos y reportes exploratorios (Modelos Gratuitos)
├── analysis_results_paid/           <- Gráficos y reportes exploratorios (Modelos de Pago)
└── analysis_results_combined/       <- Gráficos, reporte comparativo global e informe de fallos
```

---

## Convención de Nombres de Funciones (obligatorio en prompts)
| ID  | Python                  | Java/Scala                 |
|-----|-------------------------|----------------------------|
| P01 | word_frequency          | wordFrequency              |
| P02 | binary_search           | binarySearch               |
| P03 | flatten                 | flatten                    |
| P04 | is_balanced             | isBalanced                 |
| P05 | process_transactions    | processTransactions        |
| P06 | max_subarray_sum        | maxSubarraySum             |
| P07 | flood_fill              | floodFill                  |

---

## Instalación y Ejecución

A continuación se detallan los requisitos y comandos para ejecutar el análisis estadístico, la generación de gráficos y la evaluación automatizada de las pruebas unitarias.

### 1. Requisitos del Entorno

* **Análisis estadístico y generación de gráficos:**
  * **Python 3.11+** y `pip`.
* **Compilación y ejecución de las pruebas unitarias:**
  * **Python 3.11+**: ejecuta las suites de `unit_tests/python/` mediante `unittest`.
  * **Java JDK 21 + Apache Maven (`mvn`)**: ejecuta las suites de `unit_tests/java/` (JUnit 5) aplicando validación de versión mediante el parámetro `-Dmaven.compiler.release={8|17|21}`.
  * **sbt (Scala Build Tool 1.9+)**: ejecuta las suites de `unit_tests/scala/` (ScalaTest) alternando el compilador entre `Scala 2.13.12`, `Scala 3.3.3` y `Scala 3.6.0`.

---

### 2. Instalación de Dependencias de Python

Desde la raíz del proyecto, crear un entorno virtual e instalar las librerías requeridas (`pandas`, `matplotlib`, `seaborn`, `scipy`, `statsmodels`):

```bash
# 1. Crear entorno virtual
python -m venv env

# 2. Activar el entorno virtual
# En Windows (PowerShell):
.\env\Scripts\Activate.ps1
# En Windows (CMD):
env\Scripts\activate
# En Linux / macOS:
source env/bin/activate

# 3. Instalar dependencias
pip install -r requirements.txt
```

---

### 3. Ejecución del Análisis de Resultados y Generación de Gráficos

Comandos para calcular las métricas de evaluación (tasas de éxito funcional `pass@1`, desglose de modos de fallo de compilación frente a lógicos, y concisión en líneas de código `LOC`) y generar las gráficas (`300 DPI`):

```bash
# A. Modelos Gratuitos -> actualiza analysis_results_free/
python scripts/data_analysis.py
python scripts/statistical_validation.py

# B. Modelos de Pago -> actualiza analysis_results_paid/
python scripts/data_analysis_paid.py
python scripts/statistical_validation_paid.py

# C. Comparativa Global (Free vs. Paid, 1.134 pruebas) -> actualiza analysis_results_combined/
python scripts/data_analysis_combined.py
python scripts/statistical_validation_combined.py
```

Los archivos generados se almacenan en:
* `analysis_results_free/statistical_validation.txt` y `analysis_results_free/*.png`
* `analysis_results_paid/statistical_validation.txt` y `analysis_results_paid/*.png`
* `analysis_results_combined/statistical_validation.txt` y `analysis_results_combined/*.png`
* `analysis_results_combined/INFORME_PRUEBAS_FALLIDAS.md`

---

### 4. Ejecución de las Pruebas Unitarias (Construcción de los Datasets)

Para evaluar los 1.134 archivos de código en `llm_responses/` compilando y ejecutando cada caso de prueba en Python, Java y Scala:

```bash
# 1. Evaluar los 567 archivos de los modelos gratuitos -> genera dataset_free_models.csv
python scripts/dataset_builder.py

# 2. Evaluar los 567 archivos de los modelos de pago -> genera dataset_paid_models.csv
python scripts/dataset_builder_paid.py
```

> **Nota:** En la primera ejecución, `Maven` y `sbt` descargan las dependencias de JUnit 5, ScalaTest y los compiladores de Scala (`2.13.12`, `3.3.3`, `3.6.0`).

---

### 5. Ejecución de una Prueba Individual por Lenguaje

Para evaluar un archivo individual de `llm_responses/` de forma aislada:

* **En Python:**
  ```bash
  # En PowerShell:
  $env:SOLUTION_PATH="llm_responses/claude-pro/P01_python_3.11_funcional.py"
  python unit_tests/python/test_p01_word_frequency.py
  ```
* **En Java (especificando la versión objetivo `8`, `17` o `21`):**
  1. Copiar el código a `unit_tests/java/src/main/java/Solution.java` (con la clase pública nombrada `Solution`).
  2. Ejecutar desde `unit_tests/java/`:
     ```bash
     mvn test -Dtest=TestP01WordFrequency -Dmaven.compiler.release=21
     ```
* **En Scala (especificando la versión objetivo `2.13.12`, `3.3.3` o `3.6.0`):**
  1. Copiar el código a `unit_tests/scala/src/main/scala/Solution.scala` (dentro de `object Solution`).
  2. Ejecutar desde `unit_tests/scala/`:
     ```bash
     sbt "++ 3.3.3" "testOnly *P01WordFrequencySpec"
     ```
