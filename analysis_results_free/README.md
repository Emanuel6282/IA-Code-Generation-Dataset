# Análisis de Resultados - Modelos LLM Gratuitos

Este directorio (`analysis_results_free`) contiene los resultados estadísticos y gráficos de la evaluación del código generado por Modelos de Lenguaje Grande (LLMs) en su capa gratuita (ChatGPT Free, Gemini Free y Claude Free).

## Metodología de la Investigación

Para sustentar la rigurosidad científica de estos resultados, el proceso de recopilación, evaluación y análisis se estructuró en tres fases metodológicas:

### 1. Generación del Dataset y Recolección de Datos
*   **Diseño Experimental:** Se elaboraron 7 problemas algorítmicos base (Word Frequency, Binary Search, Flatten List, Balanced Parentheses, Transaction Pipeline, Max Subarray, y Flood Fill).
*   **Diversidad Estructural:** Para cada problema, se crearon permutaciones de *prompts* exigiendo soluciones en 3 lenguajes de programación (Java, Python, Scala), abarcando múltiples restricciones de versiones, y exigiendo diferentes paradigmas de programación (iterativo, funcional) y tareas de refactorización. Esto dio como resultado 189 prompts únicos.
*   **Interacción Zero-Shot:** La obtención del código se realizó de manera manual copiando y pegando el prompt directamente en las interfaces web oficiales de los modelos. Esto garantiza que se mide la respuesta natural que recibe un usuario promedio, evitando optimizaciones o *system prompts* implícitos asociados al uso de APIs.
*   **Modelos Evaluados:** ChatGPT Free, Gemini Free y Claude Free (totalizando 567 archivos de código fuente).

### 2. Entorno de Evaluación Automatizada y Aislada
Para verificar de forma empírica si las respuestas de la inteligencia artificial cumplían los requisitos, se implementó un sistema automatizado (`scripts/dataset_builder.py`) que actuó como un evaluador ciego:
*   **Inyección Dinámica:** El script procesó los 567 códigos fuente, detectó el lenguaje, y adaptó dinámicamente las funciones generadas en clases predefinidas (`Solution`).
*   **Aislamiento Estricto:** Para garantizar que una falla de compilación fuese responsabilidad exclusiva del código evaluado, se aplicó una técnica de aislamiento (renombrando temporalmente archivos de pruebas ajenas para evitar colisiones).
*   **Limpieza de Entorno:** Se procesaron y eliminaron automáticamente metadatos invisibles añadidos por sistemas operativos (como el UTF-8 BOM), garantizando que las compilaciones de `javac` y `scalac` fallaran única y exclusivamente por errores sintácticos o semánticos propios del LLM.
*   **Marcos de Prueba (Test Frameworks):** Se utilizó Unittest para Python, JUnit (Maven) para Java y ScalaTest (sbt) para Scala. Cada código debió superar una batería de 5 a 7 casos de prueba formales (`TC01`, `TC02`...) diseñados para validar casos base y límites algorítmicos.

### 3. Procesamiento Estadístico
Los resultados de la fase de pruebas se consolidaron en el archivo `dataset_free_models.csv`. Posteriormente, mediante un entorno de Python utilizando las librerías `pandas`, `matplotlib` y `seaborn`, se calcularon las tasas de éxito y la verbosidad computacional de las respuestas.

---

## Resultados y Conclusiones Preliminares (Fase Gratuita)

Sobre un universo de 567 archivos generados, el análisis empírico arrojó los siguientes resultados:

### A. Tasa de Éxito Global
*(Porcentaje de códigos que compilaron y superaron el 100% de las pruebas unitarias en el primer intento sin intervención humana).*

1.  **Gemini Free:** 67.72%
2.  **Claude Free:** 67.20%
3.  **ChatGPT Free:** 60.85%

**Análisis:** Los modelos desarrollados por Google y Anthropic superan estadísticamente al modelo gratuito de OpenAI en capacidades lógicas y algorítmicas bajo escenarios "zero-shot". La paridad técnica entre Gemini y Claude sugiere que sus arquitecturas recientes poseen una mayor capacidad de inferencia algorítmica sin requerir iteraciones o correcciones adicionales por parte del usuario.

### B. Rendimiento Según Paradigma y Lenguaje
Al agrupar la tasa de éxito según el lenguaje de programación impuesto:

*   **Java:** Gemini (80.9%), Claude (74.6%), ChatGPT (74.6%).
*   **Python:** Gemini (71.4%), Claude (71.4%), ChatGPT (55.6%).
*   **Scala:** Claude (55.6%), ChatGPT (52.4%), Gemini (50.8%).

**Análisis:** Se observa una correlación directa entre el volumen de documentación histórica de un lenguaje y el rendimiento algorítmico del LLM. Java resultó ser el entorno más propenso al éxito automatizado; su tipado estático y rigidez estructural minimizan la ambigüedad que habitualmente afecta a los modelos de lenguaje. Python, a pesar de su popularidad, evidenció una caída considerable en la efectividad de ChatGPT debido a su tipado dinámico. Por último, Scala representa una barrera de complejidad evidente: su paradigma funcional avanzado y menor presencia en las bases de datos de entrenamiento provocaron que la efectividad de todos los modelos descendiera a valores cercanos al 50%.

### C. Eficiencia Computacional y Verbosidad (Líneas de Código - LOC)
Analizando de forma exclusiva los algoritmos que fueron exitosos, se midió el promedio de líneas escritas necesarias para resolver el mismo problema:

1.  **Gemini Free:** 35.4 líneas promedio
2.  **Claude Free:** 36.9 líneas promedio
3.  **ChatGPT Free:** 49.4 líneas promedio

**Análisis:** ChatGPT Free tiende a presentar una verbosidad significativamente mayor (produciendo aproximadamente 14 líneas adicionales por solución en promedio). En el contexto de la Ingeniería de Software, un código más conciso e idiomático, que preserve la legibilidad y la corrección lógica, es fundamental para disminuir la deuda técnica. Gemini y Claude demostraron una capacidad de síntesis superior, empleando abstracciones de las bibliotecas estándar de manera más eficiente.
