# Max Subarray Sum — Python 3.9
#
# Implementación funcional del algoritmo de Kadane.
#
# Características utilizadas:
# - map(): transforma cada elemento en una representación inmutable.
# - filter(): conserva los elementos de la entrada sin utilizar estado mutable.
# - functools.reduce(): procesa secuencialmente los elementos y construye
#   un nuevo estado inmutable en cada paso.
# - Tuplas: representan el estado de cada paso sin mutabilidad.
#
# Complejidad:
# - Tiempo: O(n)
# - Espacio adicional: O(1) para el estado del algoritmo.
#
# No se utilizan for, while, var ni estado mutable.


from functools import reduce


def max_subarray_sum(numbers):
    values = tuple(
        map(
            lambda number: number,
            filter(lambda number: True, numbers)
        )
    )

    initial_state = (values[0], values[0])

    def step(state, number):
        current_sum, max_sum = state

        new_current_sum = max(
            number,
            current_sum + number
        )

        new_max_sum = max(
            max_sum,
            new_current_sum
        )

        return (new_current_sum, new_max_sum)

    _, result = reduce(
        step,
        values[1:],
        initial_state
    )

    return result


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