def max_subarray_sum(nums: list[int]) -> int:
    """
    Encuentra la suma máxima de cualquier subarreglo contiguo no vacío.
    Utiliza el Algoritmo de Kadane para lograr complejidad O(n).
    """
    if not nums:
        raise ValueError("El arreglo no puede estar vacío.")

    max_so_far = nums[0]
    current_max = nums[0]

    for num in nums[1:]:
        # Decide si extender el subarreglo actual o comenzar uno nuevo desde 'num'
        current_max = max(num, current_max + num)
        # Actualiza la suma máxima global encontrada hasta el momento
        max_so_far = max(max_so_far, current_max)

    return max_so_far


# ==========================================
# CASOS DE PRUEBA (ASSERTS)
# ==========================================

# Caso 1: Ejemplo del enunciado con números mixtos (positivos y negativos)
# Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

# Caso 2: Todos los elementos son negativos
# Subarreglo óptimo: [-1] (retorna el menos negativo / valor máximo)
assert max_subarray_sum([-5, -3, -8, -1, -4]) == -1

# Caso 3: Todos los elementos son positivos
# Subarreglo óptimo: [1, 2, 3, 4] -> suma = 10
assert max_subarray_sum([1, 2, 3, 4]) == 10

print("¡Todos los casos de prueba pasaron correctamente!")