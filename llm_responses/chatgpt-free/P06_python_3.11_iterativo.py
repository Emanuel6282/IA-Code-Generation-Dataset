# Max Subarray Sum — Python 3.11
#
# Algoritmo de Kadane:
# Mantiene la máxima suma del subarreglo que termina en la posición
# actual y la máxima suma encontrada hasta el momento.
#
# Características de Python 3.11:
# No existe una característica nueva de Python 3.11 que mejore
# materialmente este algoritmo iterativo, por lo que se mantiene
# una implementación simple y eficiente.
#
# Complejidad:
# - Tiempo: O(n)
# - Espacio adicional: O(1)
#
# No se utilizan map(), filter(), reduce() ni recursión.


def max_subarray_sum(numbers: list[int]) -> int:
    current_sum = numbers[0]
    max_sum = numbers[0]

    for number in numbers[1:]:
        if current_sum + number > number:
            current_sum = current_sum + number
        else:
            current_sum = number

        if current_sum > max_sum:
            max_sum = current_sum

    return max_sum


# Tests

# TC01: caso del ejemplo
assert max_subarray_sum(
    [-2, 1, -3, 4, -1, 2, 1, -5, 4]
) == 6

# TC02: todos los elementos son negativos
assert max_subarray_sum(
    [-5, -2, -8, -1]
) == -1

# TC03: todos los elementos son positivos
assert max_subarray_sum(
    [1, 2, 3, 4]
) == 10

print("All tests passed")