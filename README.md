# Dataset de Tesis — Evaluación de LLMs para Generación de Código

## Objetivo
Evaluar la capacidad de los LLMs para generar código **correcto**, **con conciencia de versión** y **adherente al paradigma solicitado** en distintos lenguajes, versiones y niveles de modelo.

---

## Modelos Evaluados
| Tier     | Modelo identificado                     |
|----------|-----------------------------------------|
| Gratuito | ChatGPT Free (GPT-4o mini)              |
| Pago     | ChatGPT Plus (GPT-4o)                   |
| Gratuito | Gemini Free (3.5 Flash)                 |
| Pago     | Gemini Advanced (3.5 Pro)               |
| Gratuito | Claude Free (Sonnet 5)                  |
| Pago     | Claude Pro (Sonnet 5)                   |

---

## Lenguajes y Versiones
| Lenguaje | Versión 1 | Versión 2 | Versión 3 |
|----------|-----------|-----------|-----------|
| Python   | 3.9       | 3.11      | 3.14      |
| Java     | 8         | 17        | 21        |
| Scala    | 2.13      | 3.3       | 3.6       |

---

## Problemas (P01-P07)
| ID  | Nombre                              | Sesgo Natural |
|-----|-------------------------------------|---------------|
| P01 | Word Frequency                      | Funcional     |
| P02 | Binary Search                       | Iterativo     |
| P03 | Flatten Nested List                 | Funcional     |
| P04 | Balanced Parentheses                | Neutro        |
| P05 | Transaction Pipeline                | Funcional     |
| P06 | Max Subarray Sum (Kadane)           | Iterativo     |
| P07 | 2D Flood Fill                       | Iterativo     |

---

## Tamaño del Dataset
| Tipo de Prompt        | Cálculo                                     | Filas |
|-----------------------|---------------------------------------------|-------|
| Neutro                | 7 × 3 lenguajes × 6 modelos                 | 126   |
| Restringido (versión) | 7 × 3L × 3V × 2 paradigmas × 6 modelos      | 756   |
| Refactorización       | 7 × 3L × 2 transiciones × 6 modelos         | 252   |
| **TOTAL**             |                                             | **1134**|

---

## Estructura de Directorios
```
Dataset/
├── README.md
├── dataset_template.csv
├── prompts/
│   ├── neutral/               <- 7 prompts (uno por problema)
│   ├── version_constrained/   <- Template + instrucciones
│   ├── refactor/              <- Template + instrucciones
│   ├── generated/             <- Los 189 prompts exactos en formato texto (.txt) listos para usar
│   ├── prompts_data.json      <- Base de datos con metadatos de cada prompt
│   └── prompt_browser.html    <- (Recomendado) Interfaz visual para explorar los prompts interactivamente en el navegador
├── unit_tests/
│   ├── test_cases.json        <- Especificación universal de casos
│   ├── python/                <- 7 archivos unittest
│   ├── java/                  <- 7 archivos JUnit 5 + pom.xml
│   └── scala/                 <- 7 archivos ScalaTest + build.sbt
├── base_code/
│   ├── python/                <- Código base v3.9 para refactoring
│   ├── java/                  <- Código base Java 8 para refactoring
│   └── scala/                 <- Código base Scala 2.13 para refactoring
└── scripts/
    └── dataset_builder.py     <- Script de métricas automáticas
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

## Flujo de Trabajo
1. **Ir a** `prompts/` y seleccionar el prompt del tipo deseado
2. **Enviar** a cada modelo (ChatGPT, Claude, Gemini Pro, Gemini Flash)
3. **Guardar** el código generado como `solution.py` / `Solution.java` / `Solution.scala`
4. **Ejecutar** el unit test correspondiente
5. **Registrar** resultados en `dataset_template.csv`
6. **Automatizar** métricas con `scripts/dataset_builder.py`

---

## Instalación y Ejecución

Para replicar el análisis estadístico y la generación de gráficas, asegúrate de instalar las dependencias de Python:

```bash
# 1. Crear entorno virtual
python -m venv env
source env/bin/activate  # En Windows: env\Scripts\activate

# 2. Instalar dependencias
pip install -r requirements.txt

# 3. Ejecutar el script de análisis
python scripts/data_analysis.py
```
