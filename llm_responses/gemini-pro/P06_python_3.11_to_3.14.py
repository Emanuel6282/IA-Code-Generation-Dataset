# CARACTERISTICAS DE PYTHON 3.14 UTILIZADAS Y POR QUE:
# 1. Tipado Integrado sin Importaciones (PEP 585): Se utiliza `list[int]` directamente, eliminando la dependencia del módulo `typing` del código heredado, estandarizando la firma al estilo contemporáneo.
# 2. Funciones Integradas Optimizadas por JIT (PEP 744 / Python 3.13+): Se reemplaza la estructura condicional manual (`if/else`) por llamadas directas a `max()`. Con el compilador Just-In-Time (JIT) introducido y refinado en las ramas 3.13/3.14, las llamadas a funciones integradas en bucles intensivos se optimizan (inlining), superando la eficiencia de las bifurcaciones lógicas manuales.
# 3. Notas de Diagnóstico en Excepciones (PEP 678): Se incorpora validación defensiva utilizando `.add_note()`, consolidado como la práctica idiomática para enriquecer el contexto de los errores nativos sin alterar la pila de llamadas.

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Validación fallida: El algoritmo de Kadane requiere al menos un elemento para inicializar el estado local.")
        raise err

    current_max = arr[0]
    global_max = arr[0]
    
    for i in range(1, len(arr)):
        # Idiomatismo moderno: delegar la lógica de comparación al built-in C subyacente
        current_max = max(arr[i], current_max + arr[i])
        global_max = max(global_max, current_max)
            
    return global_max

# ==========================================
# CASOS DE PRUEBA
# ==========================================

assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
assert max_subarray_sum([1]) == 1
assert max_subarray_sum([-2, -1]) == -1

print("Todos los tests en Python 3.14 pasaron correctamente.")

# /*
# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.14:
# 
# 1. **Limpieza de Dependencias de Tipado**: 
#    El código original forzaba la importación de `List`. Al migrar a `list[int]`, el código se vuelve completamente autónomo, reduciendo la carga de módulos en memoria al inicio de la ejecución y mejorando la legibilidad.
# 
# 2. **Delegación Matemática y Eliminación de Ramas (Branching)**: 
#    El bloque original `if current_max + arr[i] > arr[i]: ... else: ...` es un enfoque puramente algorítmico tradicional. Al refactorizarlo hacia `current_max = max(arr[i], current_max + arr[i])`, no solo se condensa la estructura visual, sino que se alinea el código con la arquitectura del intérprete moderno. Al ingresar y tabular este fragmento en tu plataforma web de recolección de prompts, contarás con una pieza clave para el estudio comparativo: demuestra cómo un flujo imperativo iterativo evoluciona para apoyarse en la infraestructura C/JIT subyacente, reduciendo la carga cognitiva y los posibles errores de generación en modelos de lenguaje.
# 
# 3. **Observabilidad Estructurada**: 
#    El uso de `add_note()` refleja el estándar maduro de la rama 3.14 para el manejo de casos límite (como un subarreglo vacío), aislando el manejo de errores del núcleo del algoritmo sin perder fidelidad descriptiva.
# */