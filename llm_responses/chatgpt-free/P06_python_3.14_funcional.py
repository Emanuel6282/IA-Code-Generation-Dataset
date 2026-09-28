# Max Subarray Sum — Python 3.14
#
# Características utilizadas:
# 1. list[int]:
#    Se utiliza la sintaxis moderna de anotación de tipos.
#
# 2. Tuplas inmutables:
#    Representan el estado del algoritmo sin utilizar variables mutables.
#
# 3. map():
#    Construye una secuencia funcional de enteros a partir de la entrada.
#
# 4. filter():
#    Mantiene los elementos enteros de la entrada sin utilizar estado mutable.
#
# 5. functools.reduce():
#    Implementa el algoritmo de Kadane mediante la transformación de un
#    estado inmutable en cada paso.
#
# No se utilizan for, while, var, estado mutable ni recursión.
#
# Nota:
# No existe una característica específica de Python 3.14 que mejore
# materialmente este algoritmo funcional. Por ello, no se introducen
# características nuevas de forma artificial.


from functools import reduce


def max_subarray_sum(numbers: list[int]) -> int:
    values = tuple(
        map(
            int,
            filter(
                lambda number: isinstance(number, int),
                numbers
            )
        )
    )

    initial_state = (values[0], values[0])

    def step(
        state: tuple[int, int],
        number: int
    ) -> tuple[int, int]:
        current_sum, max_sum = state

        new_current_sum = max(
            number,
            current_sum + number
        )

        new_max_sum = max(
            max_sum,
            new_current_sum
        )

        return new_current_sum, new_max_sum

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