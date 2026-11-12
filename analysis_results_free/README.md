# Análisis de Resultados — Modelos LLM Gratuitos ($n = 567$)

Este directorio (`analysis_results_free`) contiene los resultados empíricos y gráficos de la evaluación del código generado por Modelos de Lenguaje de Gran Escala (LLMs) en su nivel de acceso gratuito (**ChatGPT Free / `GPT-4o mini`**, **Gemini Free / `Gemini 3.5 Flash`** y **Claude Free / `Claude Sonnet 5`**).

## Metodología de la Investigación

El proceso de recopilación, evaluación y análisis se estructuró en tres fases metodológicas:

### 1. Diseño del Corpus y Recolección de Datos
*   **Problemas Algorítmicos Base (`P01`–`P07`):** Se evaluaron 7 problemas canónicos (*Word Frequency*, *Binary Search*, *Flatten Nested List*, *Balanced Parentheses*, *Transaction Pipeline*, *Max Subarray Sum* y *2D Flood Fill*).
*   **Construcción Paso a Paso de los Prompts:** Para un solo lenguaje de programación sobre los 7 problemas se formularon:
    *   `7` Prompts Neutrales ($7 \times 1 = 7$).
    *   `42` Prompts Restringidos por Versión y Paradigma ($7\text{ problemas} \times 3\text{ versiones} \times 2\text{ paradigmas} = 42$).
    *   `14` Prompts de Refactorización Temporal ($7\text{ problemas} \times 2\text{ transiciones} = 14$).
    Esto suma **63 prompts por lenguaje**, que al replicarse en los **3 lenguajes** (Python `3.9/3.11/3.14`, Java `8/17/21` LTS y Scala `2.13/3.3 LTS/3.6`) conforman **189 prompts únicos**.
*   **Interacción Zero-Shot:** La obtención del código se realizó mediante las interfaces web oficiales de ChatGPT, Gemini y Claude apoyándose en el navegador interactivo `prompts/prompt_browser.html`, replicando las condiciones reales de uso de un desarrollador.
*   **Muestras del Nivel Gratuito:** Los 189 prompts fueron respondidos por los 3 modelos gratuitos, totalizando **$n = 567$ archivos de código fuente**.

### 2. Entorno de Evaluación Automatizada (`scripts/dataset_builder.py`)
*   **Saneamiento e Inyección en Contenedor `Solution`:** El script elimina marcas `UTF-8 BOM`, contabiliza las líneas de código efectivas (`LOC`, sin líneas vacías ni comentarios) y normaliza las clases u objetos dentro de `Solution` ignorando bloques de comentarios (`strip_comments`).
*   **Compilación Estricta por Versión y Pruebas Unitarias:**
    *   **Python:** `unittest` en subproceso aislado (`unit_tests/python/`).
    *   **Java:** Apache Maven + JUnit 5 sobre JDK 21 aplicando `-Dmaven.compiler.release={8|17|21}`.
    *   **Scala:** SBT + ScalaTest conmutando dinámicamente el compilador entre `2.13.12`, `3.3.3` y `3.6.0` previa limpieza de artefactos `.class` y `.tasty`.

### 3. Métricas de Evaluación
Los resultados se consolidaron en `dataset_free_models.csv`, evaluando tres indicadores directos:
1.  **Tasa de Éxito Funcional (`pass@1`):** Porcentaje de soluciones que compilan sin errores (`compiles_or_runs = YES`) y aprueban el 100 % de los casos de prueba unitarios (`tests_passed_ratio = 1.0`) en el primer intento.
2.  **Clasificación de Modos de Fallo:** Desglose de soluciones fallidas entre *Fallos de Compilación* (`compiles_or_runs = NO`) y *Fallos Lógicos* (`compiles_or_runs = YES` pero `tests_passed_ratio < 1.0`).
3.  **Verbosidad Computacional (`LOC`):** Promedio y mediana de líneas de código efectivas en las soluciones exitosas.

---

## Resultados y Conclusiones (Nivel Gratuito, $n = 567$)

Sobre las **567 muestras evaluadas** en el nivel gratuito se obtuvieron **444 soluciones exitosas**, lo que representa una tasa de éxito global del **78.31 %** (con 100 fallos de compilación y 23 fallos lógicos):

### A. Tasa de Éxito Global (`pass@1`) y Modos de Fallo por Modelo

| Modelo Evaluado | Éxitos / Total | Tasa de Éxito (`pass@1`) | Fallos de Compilación | Fallos Lógicos | LOC Promedio (Mediana) |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Claude Free (`Sonnet 5`)** | 161 / 189 | **85.19 %** | 28 | **0** | 34.8 (32.0) |
| **Gemini Free (`3.5 Flash`)** | 149 / 189 | **78.84 %** | 38 | 2 | **33.4 (28.0)** |
| **ChatGPT Free (`GPT-4o mini`)** | 134 / 189 | **70.90 %** | 34 | **21** | 47.9 (29.0) |
| **Total Nivel Gratuito** | **444 / 567** | **78.31 %** | **100** | **23** | **38.3 (30.0)** |

**Hallazgos principales:**
*   **Claude Free (`Sonnet 5`)** lidera el nivel gratuito con **85.19 %**, superando en +6.35 puntos porcentuales a **Gemini Free (`3.5 Flash`)** (78.84 %) y en +14.29 puntos porcentuales a **ChatGPT Free (`GPT-4o mini`)** (70.90 %).
*   Mientras que la totalidad de los fallos de Claude Free (28) y casi todos los de Gemini Free (38 de 40) corresponden a errores de compilación estática en Java y Scala, **ChatGPT Free concentra 21 de los 23 fallos lógicos** de toda la capa gratuita.

### B. Rendimiento según Lenguaje, Versión y Paradigma ($n = 63$ por modelo y lenguaje)

*   **Python (92.1 % global en nivel gratuito):** Claude Free (**100.0 %**, 63/63), Gemini Free (**96.8 %**, 61/63) y ChatGPT Free (**79.4 %**, 50/63).
*   **Java (77.8 % global en nivel gratuito):** Gemini Free (**81.0 %**), Claude Free (**77.8 %**) y ChatGPT Free (**74.6 %**). En **Java 8 LTS** el éxito cae a **52.4 %** frente al **78.6 %** en **Java 17 LTS** y **Java 21 LTS**, debido al uso de métodos introducidos en Java 9+ (`List.of()`, `Map.of()`, `var`) que no existían en Java 8.
*   **Scala (65.1 % global en nivel gratuito):** Claude Free destaca con **77.8 %** (incluyendo 100 % de éxito en refactorización de código hacia Scala 3) frente al **58.7 %** de Gemini Free y ChatGPT Free.
*   **Paradigma (en prompts restringidos):** En el nivel gratuito el enfoque **iterativo alcanza 75.1 %** de éxito frente al **72.0 % en el enfoque funcional**.

### C. Verbosidad Computacional (`LOC` en soluciones exitosas)
1.  **Gemini Free (`3.5 Flash`):** **33.4 líneas promedio** (mediana = 28.0, $n = 149$).
2.  **Claude Free (`Sonnet 5`):** **34.8 líneas promedio** (mediana = 32.0, $n = 161$).
3.  **ChatGPT Free (`GPT-4o mini`):** **47.9 líneas promedio** (mediana = 29.0, $n = 134$).
