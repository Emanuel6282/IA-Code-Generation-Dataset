# Características de Python 3.11 utilizadas en este refactor:
# 1. Structural Pattern Matching (`match / case`):
#    Sustituye la verificación con `isinstance()` por un patrón de coincidencia declarativo y extensible.
# 2. Tipado nativo con operador de unión (`list[int | list]`):
#    Elimina la necesidad de importar `List` o `Any` desde el módulo `typing`, permitiendo un tipado más preciso.
# 3. Optimización del intérprete (Specializing Adaptive Interpreter / PEP 659):
#    Python 3.11 optimiza la ejecución de Pattern Matching y bucles en bytecode sin penalizaciones de rendimiento.

def flatten(nested: list[int | list]) -> list[int]:
    """
    Desanida iterativamente cualquier lista de enteros usando una pila (stack)
    y Structural Pattern Matching de Python 3.11.
    """
    result: list[int] = []
    stack: list[int | list] = [nested]

    while stack:
        item = stack.pop()
        match item:
            case list() as sublist:
                for element in reversed(sublist):
                    stack.append(element)
            case int() as val:
                result.append(val)

    return result


# --- Casos de prueba conservados (Python 3.11) ---
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, 2, 3]) == [1, 2, 3]

print("All tests passed")


# --- Explicación de los cambios ---
# 1. Reemplazo de `isinstance()` por `match / case`:
#    En Python 3.11, el Structural Pattern Matching (`match item: case list(): ... case int(): ...`)
#    brinda una separación clara de tipos sin necesidad de encadenar estructuras `if/else`.
# 2. Modernización del sistema de tipos (PEP 604 & PEP 585):
#    Se eliminó la importación de `typing.List` y `typing.Any` en favor de los tipos genéricos nativos
#    `list[...]` y la sintaxis de unión con `|` (`int | list`), haciendo la firma más concisa y clara.
# 3. Claridad explicita del tipo `int`:
#    En lugar de capturar cualquier valor no-lista de forma implícita con `else`, se especifica `case int() as val`,
#    lo que garantiza que solo se agreguen enteros válidos al resultado final.