"""Flatten Nested List - solución iterativa para Python 3.11."""

from typing import TypeAlias

# Tipo recursivo: una lista cuyos elementos son enteros u otras listas anidadas.
# Se escribe como cadena porque se refiere a sí mismo; dentro usa la sintaxis de
# unión `int | X` (disponible desde 3.10, en lugar de typing.Union).
NestedList: TypeAlias = "list[int | NestedList]"


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

    # Se ejecutan todos los casos aunque alguno falle y se reportan juntos.
    failures: list[AssertionError] = []
    for name, nested, expected in test_cases:
        try:
            result = flatten(nested)
            assert result == expected, f"esperado {expected}, obtenido {result}"
        except AssertionError as error:
            # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
            error.add_note(f"Caso fallido: {name}")
            failures.append(error)

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): agrupa todos los fallos
        raise ExceptionGroup("Fallaron casos de prueba de flatten", failures)

    print("Todas las pruebas pasaron.")