# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Anotaciones de tipos genéricas estándar (PEP 585): Uso nativo de `list[int]` en la firma de la función, consolidado y optimizado en las versiones modernas del lenguaje.
# 2. Control de Flujo Mejorado y Control de Excepciones Limpio (PEP 758 / PEP 765): Python 3.14 introduce validaciones estrictas en bloques de control y mejoras notables en la gestión del intérprete y advertencias de sintaxis. Aunque aquí implementamos un paradigma puramente iterativo con bucles estándar, nos beneficiamos del rendimiento superior del JIT experimental y del recolector de basura incremental integrado en esta versión.
# 3. Paradigma Iterativo Estricto (Algoritmo de Kadane): Uso exclusivo de bucles imperativos con variables mutables (`current_sum`, `max_sum`), evitando completamente funciones funcionales (`map`, `filter`, `reduce`) o recursión.

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        raise ValueError("El arreglo debe contener al menos un elemento.")
    
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