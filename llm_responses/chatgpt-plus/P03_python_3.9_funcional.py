# P03: Flatten Nested List — Python 3.9
# Paradigma: Estrictamente funcional
#
# Características:
# - Inmutabilidad mediante tuplas.
# - Uso de map(), filter() y functools.reduce().
# - Recursión para procesar listas anidadas.
# - Anotaciones de tipos modernas: list[int].
# - Sin for, while ni variables mutables.
# - Biblioteca estándar únicamente.

from functools import reduce
from typing import Union

NestedList = list[Union[int, "NestedList"]]


def flatten(arr: NestedList) -> list[int]:

    def flatten_item(item) -> tuple[int, ...]:
        if isinstance(item, list):
            return flatten_tuple(item)

        return (item,)

    def flatten_tuple(items) -> tuple[int, ...]:
        # map: aplana cada elemento recursivamente.
        mapped = map(flatten_item, items)

        # filter: descarta resultados vacíos.
        filtered = filter(bool, mapped)

        # reduce: concatena las tuplas sin mutarlas.
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