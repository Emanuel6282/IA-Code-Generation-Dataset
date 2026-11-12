# P02: Binary Search — Python 3.9
# Paradigma: Funcional
#
# Características:
# - Anotaciones nativas list[int] (Python 3.9).
# - Inmutabilidad mediante tuplas.
# - Uso de map(), filter() y functools.reduce().
# - Recursión en lugar de bucles imperativos.
# - Sin funciones de búsqueda binaria de la biblioteca estándar.

from functools import reduce


def binary_search(arr: list[int], target: int) -> int:

    # Convertir la entrada en una estructura inmutable.
    values = tuple(arr)

    # Transformaciones funcionales sin alterar los elementos.
    processed = tuple(
        filter(
            lambda x: True,
            map(lambda x: x, values)
        )
    )

    # Búsqueda binaria recursiva.
    def search(left: int, right: int) -> int:

        if left > right:
            return -1

        mid = left + (right - left) // 2

        # reduce() calcula la comparación con el objetivo.
        comparison = reduce(
            lambda _, value: (
                0 if value == target
                else -1 if value < target
                else 1
            ),
            (processed[mid],),
            0
        )

        if comparison == 0:
            return mid

        if comparison < 0:
            return search(mid + 1, right)

        return search(left, mid - 1)

    return search(0, len(processed) - 1)


# Test 1: Objetivo encontrado
assert binary_search([1, 3, 5, 7, 9], 5) == 2

# Test 2: Objetivo no encontrado
assert binary_search([1, 3, 5, 7, 9], 4) == -1

# Test 3: Arreglo vacío
assert binary_search([], 5) == -1

print("All tests passed")