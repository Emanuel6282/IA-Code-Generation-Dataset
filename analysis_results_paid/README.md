# Análisis de Resultados — Modelos LLM de Paga ($n = 567$)

Este directorio (`analysis_results_paid`) contiene los resultados empíricos y gráficos de la evaluación del código generado por Modelos de Lenguaje de Gran Escala (LLMs) en su nivel de suscripción de paga (**ChatGPT Plus / `GPT-6`**, **Gemini Pro / `Gemini 3.1 Pro`** y **Claude Pro / `Claude Opus 5.5`**).

## Metodología de la Investigación

Con el objetivo de mantener comparabilidad directa con el nivel gratuito (`analysis_results_free`), el proceso se estructuró bajo las mismas tres fases metodológicas:

### 1. Diseño del Corpus y Recolección de Datos
*   **Problemas Algorítmicos Base (`P01`–`P07`):** Se evaluaron los mismos 7 problemas canónicos (*Word Frequency*, *Binary Search*, *Flatten Nested List*, *Balanced Parentheses*, *Transaction Pipeline*, *Max Subarray Sum* y *2D Flood Fill*).
*   **Construcción Paso a Paso de los Prompts:** Para cada lenguaje individual se aplicaron los **63 prompts** (`7` neutrales + `42` restringidos por versión y paradigma + `14` de refactorización temporal), conformando en los 3 lenguajes (Python `3.9/3.11/3.14`, Java `8/17/21` LTS y Scala `2.13/3.3 LTS/3.6`) el bloque de **189 prompts únicos**.
*   **Muestras del Nivel de Paga:** Los 189 prompts fueron ejecutados en primera instancia (*zero-shot*) sobre los 3 modelos de suscripción de paga (`GPT-6`, `Gemini 3.1 Pro` y `Claude Opus 5.5`), totalizando **$n = 567$ archivos de código fuente**.

### 2. Entorno de Evaluación Automatizada (`scripts/dataset_builder_paid.py`)
*   **Saneamiento e Inyección en Contenedor `Solution`:** Se aplicaron las mismas reglas de limpieza de metadatos (`UTF-8 BOM`), conteo de líneas efectivas (`LOC`) y normalización de clases/objetos en `Solution` ignorando comentarios (`strip_comments`).
*   **Compilación Estricta por Versión y Pruebas Unitarias:** Se verificaron los 567 códigos mediante `unittest` en Python, Apache Maven + JUnit 5 (`-Dmaven.compiler.release={8|17|21}`) en Java, y SBT + ScalaTest (`++ {2.13.12 | 3.3.3 | 3.6.0}`) en Scala con limpieza previa de artefactos `.class` y `.tasty`.

### 3. Métricas de Evaluación
Los resultados se consolidaron en `dataset_paid_models.csv`, evaluando la **Tasa de Éxito Funcional (`pass@1`)**, la **Clasificación de Modos de Fallo (Compilación vs. Lógica)** y la **Verbosidad Computacional (`LOC`)**.

---

## Resultados y Conclusiones (Nivel de Paga, $n = 567$)

Sobre las **567 muestras evaluadas** en el nivel de paga se obtuvieron **472 soluciones exitosas**, alcanzando una tasa de éxito global del **83.25 %** (con 93 fallos de compilación y únicamente **2 fallos lógicos** en todo el segmento):

### A. Tasa de Éxito Global (`pass@1`) y Modos de Fallo por Modelo

| Modelo Evaluado | Éxitos / Total | Tasa de Éxito (`pass@1`) | Fallos de Compilación | Fallos Lógicos | LOC Promedio (Mediana) |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Claude Pro (`Opus 5.5`)** | 161 / 189 | **85.19 %** | 28 | **0** | 47.2 (43.0) |
| **Gemini Pro (`3.1 Pro`)** | 160 / 189 | **84.66 %** | 29 | **0** | **33.1 (29.5)** |
| **ChatGPT Plus (`GPT-6`)** | 151 / 189 | **79.89 %** | 36 | 2 | 46.7 (35.0) |
| **Total Nivel de Paga** | **472 / 567** | **83.25 %** | **93** | **2** | **42.1 (35.0)** |

**Hallazgos principales:**
*   **Claude Pro (`Opus 5.5`)** y **Gemini Pro (`3.1 Pro`)** lideran el nivel de paga con un desempeño prácticamente idéntico (**85.19 %** vs. **84.66 %**, es decir, 161 vs. 160 soluciones exitosas sobre 189), seguidos por **ChatGPT Plus (`GPT-6`)** con **79.89 %** (151/189).
*   Ni Claude Pro ni Gemini Pro cometieron un solo error lógico en las 189 pruebas (**0 fallos lógicos**), y ChatGPT Plus redujo sus fallos lógicos a solo **2 casos**.

### B. Rendimiento según Lenguaje, Versión y Paradigma ($n = 63$ por modelo y lenguaje)

*   **Python (98.9 % global en nivel de paga):** Claude Pro (**100.0 %**, 63/63), Gemini Pro (**100.0 %**, 63/63) y ChatGPT Plus (**96.8 %**, 61/63).
*   **Java (81.5 % global en nivel de paga):** ChatGPT Plus (**82.5 %**), Claude Pro (**82.5 %**) y Gemini Pro (**79.4 %**). En **Java 17 LTS** y **Java 21 LTS** los tres modelos alcanzan **85.7 %** de éxito, y en refactorización Java (`8 → 17` y `17 → 21`) alcanzan **95.2 %**, pero en **Java 8 LTS** la tasa se sitúa en **57.1 %** por la persistencia del anacronismo con APIs de Java 9+.
*   **Scala (69.3 % global en nivel de paga):** Gemini Pro lidera el nivel de paga con **74.6 %**, seguido de Claude Pro con **73.0 %** y ChatGPT Plus con **60.3 %**. En las tareas de refactorización hacia Scala 3 (`2.13 → 3.3` y `3.3 → 3.6`), el nivel de paga alcanza un **90.5 %** de éxito (con **100.0 %** en Claude Pro).
*   **Equilibrio exacto entre paradigmas:** En los prompts restringidos por versión y paradigma del nivel de paga, el enfoque **iterativo (78.8 %)** y el enfoque **funcional (78.8 %)** obtienen exactamente la misma tasa de éxito global.

### C. Verbosidad Computacional (`LOC` en soluciones exitosas)
1.  **Gemini Pro (`3.1 Pro`):** **33.1 líneas promedio** (mediana = 29.5, $n = 160$) — el modelo más conciso de todo el estudio.
2.  **ChatGPT Plus (`GPT-6`):** **46.7 líneas promedio** (mediana = 35.0, $n = 151$).
3.  **Claude Pro (`Opus 5.5`):** **47.2 líneas promedio** (mediana = 43.0, $n = 161$).
