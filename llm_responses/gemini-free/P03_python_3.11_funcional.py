# Python 3.11 - Paradigma Funcional Estricto
# Uso de functools.reduce, map(), pattern matching estructural (match/case)
# y anotaciones de tipo nativas con unión (|) sin bucles imperativos for/while.

from functools import reduce


def flatten(nested_list: list[int | list]) -> list[int]:
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista plana
    aplicando el paradigma funcional con functools.reduce, map y pattern matching (match/case)
    sin estado mutable ni bucles for/while.
    """
    return reduce(
        lambda acc, item: acc + (
            match item:
                case list() as sublist: flatten(sublist)
                case int() as val: [val]
        ),
        map(lambda elem: elem, nested_list),
        []
    )


# --- Casos de prueba (Python 3.11 - Paradigma Funcional) ---

# Caso 1: Ejemplo provisto con varios niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Falló el Caso 1"

# Caso 2: Listas vacías y sublistas vacías intercaladas
assert flatten([]) == [], "Falló el Caso 2a (Lista vacía)"
assert flatten([[], [[]], [1, []], [[2]], []]) == [1, 2], "Falló el Caso 2b (Sublistas vacías)"

# Caso 3: Lista totalmente plana y lista profundamente anidada
assert flatten([10, 20, 30]) == [10, 20, 30], "Falló el Caso 3a (Lista ya plana)"
assert flatten([[[[[99]]]]]) == [99], "Falló el Caso 3b (Profundamente anidada)"

print("¡Todos los casos de prueba pasaron correctamente!")