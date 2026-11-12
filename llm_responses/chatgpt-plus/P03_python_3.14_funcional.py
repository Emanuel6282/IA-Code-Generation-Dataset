# P03: Flatten Nested List — Python 3.14
# Paradigma: Estrictamente funcional
#
# Características:
# - Alias de tipo recursivo mediante type.
# - Anotaciones modernas: list[int] y tuple[int, ...].
# - Uso de map(), filter() y functools.reduce().
# - Tuplas inmutables para resultados intermedios.
# - Recursión para recorrer listas anidadas.
# - Sin for, while ni estado mutable.
# - Biblioteca estándar únicamente.

from functools import reduce

type NestedList = list[int | NestedList]


def flatten(arr: NestedList) -> list[int]:

    def flatten_item(item: int | NestedList) -> tuple[int, ...]:
        if isinstance(item, list):
            return flatten_tuple(item)

        return (item,)

    def flatten_tuple(items: NestedList) -> tuple[int, ...]:
        # map(): procesa cada elemento recursivamente.
        mapped = map(flatten_item, items)

        # filter(): descarta los resultados vacíos.
        filtered = filter(bool, mapped)

        # reduce(): concatena tuplas sin modificarlas.
        return reduce(
            lambda acc, values: acc + values,
            filtered,
            ()
        )

    return list(flatten_tuple(arr))


# Test 1: Lista con varios niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

# Test 2: Lista vacía
assert flatten([]) == []

# Test 3: Anidamiento profundo y listas vacías
assert flatten([1, [[2]], [], [3, [4, [5]]]]) == [1, 2, 3, 4, 5]

print("All tests passed")