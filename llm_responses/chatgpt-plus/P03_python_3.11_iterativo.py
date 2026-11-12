# P03: Flatten Nested List — Python 3.11
# Paradigma: Estrictamente iterativo
#
# Características:
# - Anotaciones modernas de tipos con list[int].
# - Alias de tipo recursivo para listas anidadas.
# - Uso de while y estructuras mutables.
# - Pila explícita para evitar la recursión.
# - Sin map(), filter(), reduce() ni recursión.
# - Biblioteca estándar únicamente.

from typing import TypeAlias

NestedList: TypeAlias = list[int | "NestedList"]


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