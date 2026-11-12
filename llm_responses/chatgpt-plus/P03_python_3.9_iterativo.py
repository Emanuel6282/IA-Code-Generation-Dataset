# P03: Flatten Nested List — Python 3.9
# Paradigma: Estrictamente iterativo
#
# Características:
# - Uso de while y variables mutables.
# - Pila explícita para recorrer listas anidadas.
# - Anotaciones de tipos compatibles con Python 3.9.
# - Sin recursión, map(), filter() ni reduce().
# - Biblioteca estándar únicamente.

from typing import Union

NestedList = list[Union[int, "NestedList"]]


def flatten(arr: NestedList) -> list[int]:
    result: list[int] = []
    stack = [iter(arr)]

    while stack:
        try:
            item = next(stack[-1])

            if isinstance(item, list):
                stack.append(iter(item))
            else:
                result.append(item)

        except StopIteration:
            stack.pop()

    return result


# Test 1: Lista con varios niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

# Test 2: Lista vacía
assert flatten([]) == []

# Test 3: Anidamiento profundo y listas vacías
assert flatten([1, [[2]], [], [3, [4, [5]]]]) == [1, 2, 3, 4, 5]

print("All tests passed")