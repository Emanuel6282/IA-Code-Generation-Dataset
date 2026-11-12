# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Optimización del Intérprete para Funciones de Orden Superior: Python 3.14 introduce mejoras importantes en la evaluación de expresiones lambda y el rendimiento de funciones integradas como `reduce`, optimizando la propagación de estados inmutables.
# 2. Plegado Funcional Puro (`functools.reduce`): Aplicación del algoritmo de Kadane a través de transformaciones puras sobre una tupla de estado inmutable `(current_sum, max_sum)`.
# 3. Transparencia Referencial Absoluta: Cero efectos secundarios, ausencia total de bucles imperativos (`for`/`while`) y eliminación de variables locales con estado mutable.

from functools import reduce

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        raise ValueError("El arreglo debe contener al menos un elemento.")
    
    # Función de transición pura que computa el siguiente estado inmutable: (current_sum, max_sum)
    transition = lambda acc, x: (
        max(x, acc[0] + x),
        max(acc[1], max(x, acc[0] + x))
    )
    
    # Plegado funcional utilizando reduce con un estado inicial inmutable
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