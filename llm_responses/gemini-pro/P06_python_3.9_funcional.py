# CARACTERISTICAS DE PYTHON 3.9 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Anotaciones de tipos genéricas estándar (PEP 585): Uso nativo de `list[int]` en la firma, disponible desde Python 3.9.
# 2. Reducción Funcional Pura (`functools.reduce`): El algoritmo de Kadane se expresa como un plegado funcional (fold/reduce) sobre una estructura de estado inmutable en forma de tupla `(current_sum, max_sum)`.
# 3. Transparencia Referencial e Inmutabilidad: Se evita por completo el uso de bucles `for`/`while` imperativos y variables mutables locales, delegando la evolución del estado a una función de transición pura de orden superior.

from functools import reduce

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        raise ValueError("El arreglo debe contener al menos un elemento.")
    
    # Función de transición pura que calcula el nuevo estado: (current_sum, max_sum)
    transition = lambda acc, x: (
        max(x, acc[0] + x), 
        max(acc[1], max(x, acc[0] + x))
    )
    
    # Plegado funcional utilizando reduce con estado inicial inmutable
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