# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Exception Notes (`add_note()` - PEP 678): Introducido en Python 3.11, permite adjuntar notas diagnósticas estructuradas a las excepciones (como una lista vacía), mejorando la observabilidad sin comprometer la pureza referencial.
# 2. Plegado Funcional Puro (`functools.reduce`): Implementación del algoritmo de Kadane mediante transformaciones inmutables sobre una tupla de estado `(current_sum, max_sum)`.
# 3. Transparencia Referencial y Funciones Puras: Cero efectos secundarios, ausencia total de bucles imperativos (`for`/`while`) y eliminación de variables locales con estado mutable.

from functools import reduce

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        err = ValueError("El arreglo debe contener al menos un elemento.")
        err.add_note("Contexto funcional: No se puede calcular la suma máxima de un subarreglo vacío.")
        raise err
    
    # Función de transición pura que evalúa y retorna el nuevo estado inmutable: (current_sum, max_sum)
    transition = lambda acc, x: (
        max(x, acc[0] + x),
        max(acc[1], max(x, acc[0] + x))
    )
    
    # Reducción funcional propagando el estado inmutable a lo largo de la secuencia
    _, max_sum = reduce(transition, arr[1:], (arr[0], arr[0]))
    
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