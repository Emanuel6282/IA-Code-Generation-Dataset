# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Exception Notes (`add_note()` - PEP 678): Python 3.11 introdujo la capacidad de adjuntar notas informativas directamente a las excepciones, permitiendo enriquecer el diagnóstico de errores (como una lista vacía) sin alterar el flujo de control iterativo.
# 2. Rendimiento del Intérprete Especializado (PEP 659): Python 3.11 incluye mejoras masivas de velocidad gracias al "specializing adaptive interpreter", acelerando de forma nativa la ejecución de bucles `for` y la manipulación de variables mutables.
# 3. Paradigma Iterativo Estricto: Uso exclusivo de bucles imperativos con variables de estado (`current_sum`, `max_sum`), evitando cualquier función de orden superior (`map`, `filter`, `reduce`) o recursión.

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        err = ValueError("El arreglo debe contener al menos un elemento.")
        err.add_note("Contexto de error: Se intentó calcular la suma máxima sobre un subarreglo vacío.")
        raise err
    
    current_sum = arr[0]
    max_sum = arr[0]
    
    # Bucle iterativo imperativo optimizado
    for i in range(1, len(arr)):
        if current_sum + arr[i] > arr[i]:
            current_sum = current_sum + arr[i]
        else:
            current_sum = arr[i]
            
        if current_sum > max_sum:
            max_sum = current_sum
            
    return max_sum

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: mezcla de positivos y negativos
assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

# 2. Caso con todos los elementos negativos (retorna el menos negativo)
assert max_subarray_sum([-5, -2, -9, -1, -4]) == -1

# 3. Caso con todos los elementos positivos (retorna la suma total)
assert max_subarray_sum([1, 2, 3, 4, 5]) == 15