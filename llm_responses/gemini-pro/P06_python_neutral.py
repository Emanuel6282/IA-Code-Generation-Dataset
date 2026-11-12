def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        raise ValueError("El arreglo debe tener al menos 1 elemento.")

    current_max = arr[0]
    global_max = arr[0]

    for i in range(1, len(arr)):
        # Decide si es mejor continuar el subarreglo actual o empezar uno nuevo
        current_max = max(arr[i], current_max + arr[i])
        
        # Actualiza el máximo global si encontramos una suma mayor
        global_max = max(global_max, current_max)

    return global_max

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo (mezcla de positivos y negativos)
assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

# 2. Caso con todos los elementos negativos (debe retornar el menos negativo)
assert max_subarray_sum([-5, -2, -9, -1, -4]) == -1

# 3. Caso con todos los elementos positivos (debe retornar la suma total)
assert max_subarray_sum([1, 2, 3, 4, 5]) == 15