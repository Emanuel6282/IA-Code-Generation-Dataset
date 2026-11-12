"""Flatten Nested List - solución iterativa para Python 3.9."""

from typing import Union

# Tipo recursivo: una lista cuyos elementos son enteros u otras listas anidadas.
# Python 3.9 (PEP 585): list[...] nativo en lugar de typing.List. La unión se escribe
# con typing.Union porque el operador `int | X` en tipos no llega hasta Python 3.10.
NestedList = list[Union[int, "NestedList"]]


def flatten(nested: NestedList) -> list[int]:
    """Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.

    Usa una pila explícita de iteradores en lugar de recursión, así que funciona con
    cualquier nivel de anidamiento sin alcanzar el límite de recursión de Python.
    """
    result: list[int] = []
    stack = [iter(nested)]
    while stack:
        exhausted = True
        for item in stack[-1]:
            if isinstance(item, list):
                stack.append(iter(item))  # baja un nivel; este se retoma al terminar
                exhausted = False
                break
            result.append(item)
        if exhausted:
            stack.pop()  # el nivel actual se agotó: vuelve al anterior
    return result


if __name__ == "__main__":
    # Anidamiento profundo, más allá del límite de recursión por defecto (~1000).
    deep: NestedList = [7]
    for _ in range(5000):
        deep = [deep]

    test_cases: list[tuple[str, NestedList, list[int]]] = [
        # Caso 1: ejemplo del enunciado
        ("ejemplo del enunciado", [1, [2, 3], [4, [5, 6]]], [1, 2, 3, 4, 5, 6]),
        # Caso 2: lista vacía y sublistas vacías anidadas
        ("listas vacías", [[], [[]], [[], [[]]]], []),
        # Caso 3: anidamiento muy profundo
        ("anidamiento profundo", [0, deep, 8], [0, 7, 8]),
    ]

    assert flatten([]) == [], "lista vacía -> lista vacía"
    for name, nested, expected in test_cases:
        result = flatten(nested)
        assert result == expected, f"{name}: esperado {expected}, obtenido {result}"

    print("Todas las pruebas pasaron.")