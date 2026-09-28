from __future__ import annotations
from functools import reduce


def flatten(nested_list: list[int | list]) -> list[int]:
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista plana
    aplicando el paradigma funcional con functools.reduce, map y concatenación de tuplas/listas
    inmutables sin bucles for/while ni estado mutable.
    """
    return reduce(
        lambda acc, x: acc + (flatten(x) if isinstance(x, list) else [x]),
        map(lambda elem: elem, nested_list),
        []
    )


# --- Casos de prueba (Python 3.9 - Paradigma Funcional) ---

# Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Falló el Caso 1"

# Caso 2: Lista vacía y estructuras con sublistas vacías intercaladas
assert flatten([]) == [], "Falló el Caso 2a (Lista vacía)"
assert flatten([[], [[]], [1, []], [[2]], []]) == [1, 2], "Falló el Caso 2b (Sublistas vacías)"

# Caso 3: Lista totalmente plana y lista profundamente anidada
assert flatten([10, 20, 30]) == [10, 20, 30], "Falló el Caso 3a (Lista ya plana)"
assert flatten([[[[[99]]]]]) == [99], "Falló el Caso 3b (Profundamente anidada)"

print("¡Todos los casos de prueba pasaron correctamente!")