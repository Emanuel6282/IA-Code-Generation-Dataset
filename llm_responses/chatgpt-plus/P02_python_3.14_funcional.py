# P02: Binary Search — Python 3.14
# Paradigma: Estrictamente funcional
#
# Características:
# - Anotaciones de tipos nativas: list[int].
# - Evaluación diferida de anotaciones (Python 3.14).
# - Uso de tuplas inmutables.
# - Uso de map(), filter() y functools.reduce().
# - Recursión sin estado mutable.
# - Sin bibliotecas externas.
# - Sin funciones predefinidas de búsqueda binaria.

from functools import reduce


def binary_search(arr: list[int], target: int) -> int:

    # Convertir la lista en una estructura inmutable.
    values = tuple(arr)

    # Transformaciones funcionales que conservan
    # todos los elementos y sus índices originales.
    processed = tuple(
        filter(
            lambda value: True,
            map(lambda value: value, values)
        )
    )

    # Búsqueda binaria recursiva.
    def search(left: int, right: int) -> int:

        # Caso base: intervalo vacío.
        if left > right:
            return -1

        # Calcular el índice central.
        mid = left + (right - left) // 2

        # Comparar el elemento central con el objetivo.
        comparison = reduce(
            lambda _, value: (
                0 if value == target
                else -1 if value < target
                else 1
            ),
            (processed[mid],),
            0
        )

        # Caso base: objetivo encontrado.
        if comparison == 0:
            return mid

        # Buscar recursivamente en la mitad derecha.
        if comparison < 0:
            return search(mid + 1, right)

        # Buscar recursivamente en la mitad izquierda.
        return search(left, mid - 1)

    return search(0, len(processed) - 1)


# Test 1: Objetivo encontrado
assert binary_search([1, 3, 5, 7, 9], 5) == 2

# Test 2: Objetivo no encontrado
assert binary_search([1, 3, 5, 7, 9], 4) == -1

# Test 3: Arreglo vacío
assert binary_search([], 5) == -1

print("All tests passed")