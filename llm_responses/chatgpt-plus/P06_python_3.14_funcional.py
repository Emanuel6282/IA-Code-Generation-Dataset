# P06: Max Subarray Sum — Python 3.14
# Paradigma: ESTRICTAMENTE FUNCIONAL
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones modernas:
#    list[int] y tuple[int, int] permiten expresar
#    los tipos de entrada y del estado inmutable.
#
# 2. Evaluación diferida de anotaciones (Python 3.14):
#    PEP 649 y PEP 749 permiten evaluar las
#    anotaciones cuando se necesitan.
#
# 3. map():
#    Transforma los elementos sin modificar la entrada.
#
# 4. filter():
#    Selecciona elementos mediante un predicado.
#
# 5. functools.reduce():
#    Aplica el algoritmo de Kadane sin bucles explícitos.
#
# 6. Tuplas inmutables:
#    Cada transición produce un nuevo estado.
#
# NOTA:
# map(), filter(), reduce() y las anotaciones
# list[int] ya existían antes de Python 3.14.
# La evaluación diferida de anotaciones es una
# característica incorporada en Python 3.14.
#
# Biblioteca estándar únicamente.
# Sin for, while, variables mutables ni recursión.

from functools import reduce


def max_subarray_sum(nums: list[int]) -> int:
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Estado inicial inmutable:
    # (máxima suma que termina aquí, máxima suma global).
    initial_state: tuple[int, int] = (nums[0], nums[0])

    # Función pura: transforma un estado en otro.
    def step(
        state: tuple[int, int],
        num: int
    ) -> tuple[int, int]:

        current_sum, max_sum = state

        new_current_sum = max(num, current_sum + num)
        new_max_sum = max(max_sum, new_current_sum)

        return (new_current_sum, new_max_sum)

    # map() transforma cada elemento en un par
    # (posición, valor), sin alterar el arreglo.
    indexed_nums = map(
        lambda pair: (pair[0], pair[1]),
        enumerate(nums)
    )

    # filter() excluye únicamente la primera posición,
    # ya utilizada para inicializar el estado.
    # Los valores negativos se conservan.
    remaining_nums = filter(
        lambda pair: pair[0] > 0,
        indexed_nums
    )

    # map() extrae los valores en su orden original.
    values = map(
        lambda pair: pair[1],
        remaining_nums
    )

    # reduce() aplica Kadane mediante estados inmutables.
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