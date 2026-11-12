# P06: Max Subarray Sum — Python 3.9
# Paradigma: ESTRICTAMENTE FUNCIONAL
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. list[int] (Python 3.9, PEP 585):
#    Permite declarar tipos genéricos sin importar List
#    desde el módulo typing.
#
# 2. tuple[int, int] (Python 3.9, PEP 585):
#    Representa el estado inmutable del algoritmo.
#
# 3. map():
#    Transforma los elementos de entrada funcionalmente.
#
# 4. filter():
#    Permite procesar elementos mediante un predicado.
#
# 5. functools.reduce():
#    Acumula resultados sin utilizar bucles explícitos.
#
# 6. lambda:
#    Define funciones anónimas sin modificar variables.
#
# 7. Inmutabilidad:
#    Cada paso genera una nueva tupla en lugar de
#    modificar un acumulador existente.
#
# NOTA:
# map(), filter(), reduce() y lambda existían antes
# de Python 3.9. Las anotaciones list[int] y
# tuple[int, int] fueron habilitadas por PEP 585.
#
# Biblioteca estándar únicamente.
# Sin for, while, variables mutables ni recursión.

from functools import reduce


def max_subarray_sum(nums: list[int]) -> int:
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Transformación funcional de los elementos.
    mapped_nums = map(lambda x: x, nums)

    # Filtrado funcional que conserva todos los enteros.
    # No se eliminan los negativos porque pueden
    # pertenecer al subarreglo óptimo.
    filtered_nums = filter(lambda x: isinstance(x, int), mapped_nums)

    # Estado inmutable:
    # (suma del subarreglo actual, suma máxima global).
    initial_state: tuple[int, int] = (nums[0], nums[0])

    # Transición funcional del algoritmo de Kadane.
    def step(state: tuple[int, int], num: int) -> tuple[int, int]:
        current_sum, max_sum = state

        new_current_sum = max(num, current_sum + num)
        new_max_sum = max(max_sum, new_current_sum)

        return (new_current_sum, new_max_sum)

    # Saltar el primer elemento, ya utilizado
    # para inicializar el estado.
    remaining_nums = filter(
        lambda pair: pair[0] > 0,
        enumerate(filtered_nums)
    )

    # Extraer los valores restantes.
    values = map(lambda pair: pair[1], remaining_nums)

    # Reducción funcional sin acumuladores mutables.
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