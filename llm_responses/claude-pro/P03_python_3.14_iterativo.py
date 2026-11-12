"""Flatten Nested List - solución iterativa para Python 3.14."""

from string.templatelib import Interpolation, Template

# Sentencia `type` (PEP 695, Python 3.12+): declara el alias recursivo sin comillas,
# porque su valor se evalúa de forma perezosa y puede referirse a sí mismo.
type NestedList = list[int | NestedList]


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


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, de forma iterativa.

    A diferencia de una f-string, una t-string no produce un str directamente:
    produce un Template con sus partes literales e interpolaciones por separado.
    """
    parts: list[str] = []
    for item in template:
        if isinstance(item, Interpolation):
            value = item.value
            match item.conversion:
                case "r":
                    value = repr(value)
                case "s":
                    value = str(value)
                case "a":
                    value = ascii(value)
            parts.append(format(value, item.format_spec))
        else:
            parts.append(item)
    return "".join(parts)


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
        # El mensaje solo se construye si el assert falla; se arma con una t-string.
        assert result == expected, render(t"Caso {name!r}: esperado {expected}, obtenido {result}")

    print("Todas las pruebas pasaron.")