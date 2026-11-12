# Análisis Comparativo Global — Modelos LLM Gratuitos vs. de Paga ($N = 1134$)

Este directorio (`analysis_results_combined`) consolida los resultados empíricos y gráficos del **corpus completo (`dataset_all_models.csv`, $N = 1134$ muestras)** para la tesis **"Análisis del código generado por Inteligencia Artificial según paradigma de programación, versión del lenguaje y tipo de modelo"**, comparando los **3 modelos gratuitos ($n = 567$)** frente a los **3 modelos de suscripción de paga ($n = 567$)**:

| Proveedor | Nivel Gratuito (`Free`) | Nivel de Paga (`Paid`) | Muestras por Proveedor |
| :--- | :--- | :--- | :---: |
| **OpenAI** | ChatGPT Free (`GPT-4o mini`) | ChatGPT Plus (`GPT-6`) | $189 + 189 = 378$ |
| **Google** | Gemini Free (`Gemini 3.5 Flash`) | Gemini Pro (`Gemini 3.1 Pro`) | $189 + 189 = 378$ |
| **Anthropic** | Claude Free (`Claude Sonnet 5`) | Claude Pro (`Claude Opus 5.5`) | $189 + 189 = 378$ |

---

## 1. Construcción del Corpus Experimental ($N = 1134$)

A partir de los **7 problemas algorítmicos canónicos (`P01`–`P07`)**, para cada lenguaje individual se construyeron tres categorías de prompts:
*   `7` Prompts **Neutrales** ($7 \times 1 = 7$ por lenguaje $\to 21$ en los 3 lenguajes $\to 126$ muestras en los 6 modelos).
*   `42` Prompts **Restringidos por Versión y Paradigma** ($7\text{ problemas} \times 3\text{ versiones} \times 2\text{ paradigmas} = 42$ por lenguaje $\to 126$ en los 3 lenguajes $\to 756$ muestras en los 6 modelos).
*   `14` Prompts de **Refactorización Temporal** ($7\text{ problemas} \times 2\text{ transiciones } (v_1 \to v_2 \text{ y } v_2 \to v_3) = 14$ por lenguaje $\to 42$ en los 3 lenguajes $\to 252$ muestras en los 6 modelos).

Esto totaliza **63 prompts por lenguaje**, **189 prompts únicos en los 3 lenguajes** (Python `3.9/3.11/3.14`, Java `8/17/21` LTS y Scala `2.13/3.3 LTS/3.6`) y **$N = 1134$ muestras de código** evaluadas de forma determinista con `unittest` (Python), `Maven -Dmaven.compiler.release={8|17|21}` (Java) y `sbt ++ {2.13.12|3.3.3|3.6.0}` (Scala).

---

## 2. Resultados Globales por Modelo y Nivel de Acceso

### Cuadro 1: Tasa de Éxito Funcional (`pass@1`), Modos de Fallo y Verbosidad (`LOC`)

| Modelo Evaluado | Nivel | Éxitos / Total | Tasa de Éxito (`pass@1`) | Fallos Compilación | Fallos Lógicos | LOC Promedio (Mediana) |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **Claude Pro (`Opus 5.5`)** | Paga | 161 / 189 | **85.19 %** | 28 | **0** | 47.2 (43.0) |
| **Claude Free (`Sonnet 5`)** | Gratuito | 161 / 189 | **85.19 %** | 28 | **0** | 34.8 (32.0) |
| **Gemini Pro (`3.1 Pro`)** | Paga | 160 / 189 | **84.66 %** | 29 | **0** | **33.1** (29.5) |
| **Gemini Free (`3.5 Flash`)** | Gratuito | 149 / 189 | **78.84 %** | 38 | 2 | 33.4 (**28.0**) |
| **ChatGPT Plus (`GPT-6`)** | Paga | 151 / 189 | **79.89 %** | 36 | 2 | 46.7 (35.0) |
| **ChatGPT Free (`GPT-4o mini`)** | Gratuito | 134 / 189 | **70.90 %** | 34 | **21** | 47.9 (29.0) |
| **Agregado Nivel Gratuito** | **Free** | **444 / 567** | **78.31 %** | **100** | **23** | **38.3 (30.0)** |
| **Agregado Nivel de Paga** | **Paid** | **472 / 567** | **83.25 %** | **93** | **2** | **42.1 (35.0)** |
| **TOTAL CORPUS** | **Ambos** | **916 / 1134** | **80.78 %** | **193** | **25** | **40.3 (32.0)** |

---

## 3. Comparación Intra-Proveedor: Brecha entre Versiones Gratuitas y de Paga

### Cuadro 2: Desempeño Gratuito vs. de Paga por Proveedor ($n = 189$ prompts por modelo)

| Proveedor | Transición (`Free → Paid`) | Éxitos Free | Éxitos Paid | Diferencia (`Paid - Free`) | Fallos Lógicos (`Free → Paid`) |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **OpenAI** | `GPT-4o mini → GPT-6` | 134 / 189 (70.90 %) | 151 / 189 (79.89 %) | **+8.99 %** (+17 éxitos) | **21 → 2** (-90.5 %) |
| **Google** | `Gemini 3.5 Flash → 3.1 Pro` | 149 / 189 (78.84 %) | 160 / 189 (84.66 %) | **+5.82 %** (+11 éxitos) | **2 → 0** (-100 %) |
| **Anthropic** | `Sonnet 5 → Opus 5.5` | 161 / 189 (85.19 %) | 161 / 189 (85.19 %) | **0.00 %** (0 diferencia) | **0 → 0** (0 fallos) |
| **Global** | **Nivel Gratuito → Nivel de Paga** | **444 / 567 (78.31 %)** | **472 / 567 (83.25 %)** | **+4.94 %** (+28 éxitos) | **23 → 2** (-91.3 %) |

1.  **OpenAI (`GPT-4o mini` $\to$ `GPT-6`):** Presenta el mayor salto al pasar a la versión de paga (**+8.99 %**, subiendo de 70.90 % a 79.89 %) y reduce los fallos lógicos de **21 a solo 2**. Mejora en los tres lenguajes: en Python sube de 79.4 % a 96.8 % (+17.4 %), en Java de 74.6 % a 82.5 % (+7.9 %) y en Scala de 58.7 % a 60.3 % (+1.6 %).
2.  **Google (`Gemini 3.5 Flash` $\to$ `Gemini 3.1 Pro`):** Sube de 78.84 % a 84.66 % (**+5.82 %**), alcanza **100.0 % en Python (63/63)**, elimina por completo los fallos lógicos (de 2 a 0) y registra el mayor salto en **Scala**, pasando de **58.7 % a 74.6 % (+15.9 %)**.
3.  **Anthropic (`Claude Sonnet 5` $\to$ `Claude Opus 5.5`):** Ambos niveles alcanzan el puntaje más alto del estudio (**85.19 %, 161/189**), con **100.0 % en Python**, **100.0 % en tareas de Refactorización** y **0 fallos lógicos** en las 378 muestras evaluadas.

---

## 4. Resultados por Lenguaje, Versión, Paradigma y Problema

### Cuadro 3: Tasa de Éxito por Lenguaje de Programación ($n = 63$ por modelo; $n = 378$ por lenguaje)

| Lenguaje | Nivel Gratuito | Nivel de Paga | Total Lenguaje | `GPT-4o mini` | `GPT-6` | `3.5 Flash` | `3.1 Pro` | `Sonnet 5` | `Opus 5.5` |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **Python** | 92.1 % | **98.9 %** | **95.5 %** | 79.4 % | 96.8 % | 96.8 % | **100.0 %** | **100.0 %** | **100.0 %** |
| **Java** | 77.8 % | **81.5 %** | **79.6 %** | 74.6 % | **82.5 %** | 81.0 % | 79.4 % | 77.8 % | **82.5 %** |
| **Scala** | 65.1 % | **69.3 %** | **67.2 %** | 58.7 % | 60.3 % | 58.7 % | **74.6 %** | **77.8 %** | 73.0 % |

### Cuadro 4: Resultados por Versión del Lenguaje y Refactorización Temporal

| Lenguaje y Versión / Transición | Tipo de Tarea | Muestras ($n$) | Nivel Gratuito (%) | Nivel de Paga (%) | Total (%) | Observación Empírica Principal |
| :--- | :---: | :---: | :---: | :---: | :---: | :--- |
| **Java cualquiera** | Neutral | 42 | 90.5 % | 85.7 % | **88.1 %** | Alta fiabilidad cuando no se restringe la versión |
| **Java 8 (LTS)** | Restringido | 84 | 52.4 % | **57.1 %** | **54.8 %** | Caída por uso de APIs de Java 9+ (`List.of`, `Map.of`, `var`) |
| **Java 17 (LTS)** | Restringido | 84 | 78.6 % | **85.7 %** | **82.1 %** | Los 3 modelos de paga alcanzan 85.7 % de forma uniforme |
| **Java 21 (LTS)** | Restringido | 84 | 78.6 % | **85.7 %** | **82.1 %** | El nivel de paga supera al gratuito en +7.1 puntos porcentuales |
| **Java 8 → 17 / 17 → 21** | Refactor | 84 | **95.2 %** | **95.2 %** | **95.2 %** | 100 % de éxito en los modelos de OpenAI y Anthropic |
| **Python cualquiera** | Neutral | 42 | 95.2 % | **100.0 %** | **97.6 %** | 100 % de éxito en todo el nivel de paga |
| **Python 3.9 / 3.11 / 3.14** | Restringido | 252 | 90.5 %–92.9 % | **95.2 %–100.0 %** | **92.9 %–96.4 %** | 100 % de éxito en Python 3.9 y 3.14 en todo el nivel de paga |
| **Python 3.9 → 3.11 / 3.11 → 3.14** | Refactor | 84 | 90.5 %–95.2 % | **100.0 %** | **95.2 %–97.6 %** | 100 % de éxito en refactorización Python en todo el nivel de paga |
| **Scala cualquiera** | Neutral | 42 | 61.9 % | **71.4 %** | **66.7 %** | El nivel de paga supera al gratuito en +9.5 puntos porcentuales |
| **Scala 2.13** | Restringido | 84 | 57.1 % | **61.9 %** | **59.5 %** | Gemini Pro lidera el nivel de paga con 71.4 % |
| **Scala 3.3 LTS / 3.6** | Restringido | 168 | 59.5 %–61.9 % | **61.9 %** | **60.7 %–61.9 %** | Gemini Pro lidera con 71.4 % en ambas versiones de Scala 3 |
| **Scala 2.13 → 3.3 / 3.3 → 3.6** | Refactor | 84 | 81.0 %–85.7 % | **90.5 %** | **85.7 %–88.1 %** | 100 % de éxito en Claude Free y Claude Pro en migración Scala 3 |

### Cuadro 5: Desempeño por Tipo de Prompt, Paradigma y Problema Algorítmico (`P01`–`P07`)

| Dimensión | Categoría | Muestras ($n$) | Nivel Gratuito (%) | Nivel de Paga (%) | Total Global (%) | Observación Empírica |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- |
| **Tipo de Prompt** | **Refactorización** | 252 | 90.5 % | **95.2 %** | **92.9 %** | Modalidad con mayor tasa de éxito en ambos niveles |
| | **Neutral** | 126 | 82.5 % | **85.7 %** | **84.1 %** | Línea base sin restricciones de versión ni paradigma |
| | **Restringido (Versión/Paradigma)** | 756 | 73.5 % | **78.8 %** | **76.2 %** | Mayor exigencia por validación estricta del compilador |
| **Paradigma** | **Iterativo** | 378 | 75.1 % | **78.8 %** | **77.0 %** | Ligera ventaja en el nivel gratuito (+3.1 %) |
| *(en Restringidos)* | **Funcional** | 378 | 72.0 % | **78.8 %** | **75.4 %** | El nivel de paga iguala exactamente ambos paradigmas (78.8 %) |
| **Problema** | **P01 Word Frequency** | 162 | **90.1 %** | 88.9 % | **89.5 %** | 100 % en Python, 90.7 % en Scala y 77.8 % en Java |
| | **P02 Binary Search** | 162 | 96.3 % | **100.0 %** | **98.1 %** | **100 % de éxito en todo el nivel de paga (81/81)** |
| | **P03 Flatten Nested List** | 162 | 56.8 % | **64.2 %** | **60.5 %** | El nivel de paga supera al gratuito en +7.4 % |
| | **P04 Balanced Parentheses** | 162 | 91.4 % | **100.0 %** | **95.7 %** | **100 % de éxito en todo el nivel de paga (81/81)** |
| | **P05 Transaction Pipeline** | 162 | **53.1 %** | 43.2 % | **48.1 %** | 98.1 % en Python vs. 25.9 % en Scala y 20.4 % en Java |
| | **P06 Max Subarray Sum** | 162 | 92.6 % | **100.0 %** | **96.3 %** | **100 % de éxito en todo el nivel de paga (81/81)** |
| | **P07 2D Flood Fill** | 162 | 67.9 % | **86.4 %** | **77.2 %** | **Salto de +18.5 % en el nivel de paga** (100 % en Java) |
