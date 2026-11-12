# P06: Maximum Subarray Sum — Python
# Algoritmo de Kadane
#
# Complejidad temporal: O(n)
# Complejidad espacial: O(1)

def max_subarray_sum(nums: list[int]) -> int:
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Inicializar con el primer elemento.
    current_sum = nums[0]
    max_sum = nums[0]

    # Recorrer los elementos restantes.
    for num in nums[1:]:
        # Elegir entre comenzar un nuevo subarreglo
        # o extender el subarreglo actual.
        current_sum = max(num, current_sum + num)

        # Actualizar la suma máxima encontrada.
        max_sum = max(max_sum, current_sum)

    return max_sum


# TEST 1: Ejemplo del enunciado.
assert max_subarray_sum(
    [-2, 1, -3, 4, -1, 2, 1, -5, 4]
) == 6

# TEST 2: Todos los números negativos.
assert max_subarray_sum(
    [-8, -3, -6, -2, -5, -4]
) == -2

# TEST 3: Todos los números positivos.
assert max_subarray_sum(
    [1, 2, 3, 4, 5]
) == 15

print("All tests passed")