# P06: Max Subarray Sum — Python 3.11
# Paradigma: ESTRICTAMENTE FUNCIONAL
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[int] y tuple[int, int] permiten expresar
#    los tipos sin importar List o Tuple de typing.
#
# 2. map():
#    Transforma los elementos mediante una función pura.
#
# 3. filter():
#    Selecciona elementos mediante un predicado.
#
# 4. functools.reduce():
#    Aplica el algoritmo de Kadane sin bucles explícitos.
#
# 5. Tuplas inmutables:
#    Cada paso produce un nuevo estado.
#
# 6. Python 3.11:
#    Puede aprovechar las optimizaciones del intérprete,
#    incluida la especialización adaptativa.
#
# NOTA:
# list[int] y tuple[int, int] se introdujeron en
# Python 3.9. map(), filter() y reduce() son anteriores
# a Python 3.11. No se necesita ninguna característica
# exclusiva de Python 3.11 para este algoritmo.
#
# Biblioteca estándar únicamente.
# Sin for, while, variables mutables ni recursión.

from functools import reduce


def max_subarray_sum(nums: list[int]) -> int:
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Estado inicial inmutable:
    # (mejor suma que termina aquí, mejor suma global).
    initial_state: tuple[int, int] = (nums[0], nums[0])

    # Función pura que calcula el siguiente estado.
    def step(
        state: tuple[int, int],
        num: int
    ) -> tuple[int, int]:

        current_sum, max_sum = state

        new_current_sum = max(num, current_sum + num)
        new_max_sum = max(max_sum, new_current_sum)

        return (new_current_sum, new_max_sum)

    # map() transforma cada elemento en su posición
    # y valor, sin modificar la lista original.
    indexed_nums = map(
        lambda pair: (pair[0], pair[1]),
        enumerate(nums)
    )

    # filter() excluye la primera posición porque
    # su valor ya inicializó el estado.
    remaining_nums = filter(
        lambda pair: pair[0] > 0,
        indexed_nums
    )

    # map() extrae los valores conservando su orden.
    values = map(
        lambda pair: pair[1],
        remaining_nums
    )

    # reduce() aplica Kadane con estados inmutables.
    final_state = reduce(step, values, initial_state)

    return final_state[1]


# TEST 1: Ejemplo del enunciado.
assert max_subarray_sum(
    [-2, 1, -3, 4, -1, 2, 1, -5, 4]
) == 6

# TEST 2: Todos los elementos negativos.
assert max_subarray_sum(
    [-8, -3, -6, -2, -5, -4]
) == -2

# TEST 3: Todos los elementos positivos.
assert max_subarray_sum(
    [1, 2, 3, 4, 5]
) == 15

print("All tests passed")