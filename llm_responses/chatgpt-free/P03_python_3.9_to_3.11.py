# P03: Flatten Nested List — Python 3.11
#
# Características utilizadas:
# 1. Tipado genérico incorporado: list[int]
#    - Python 3.9+ permite usar list[int] directamente.
#    - Evita importar List desde typing y hace el código más simple.
#
# 2. Any desde typing
#    - Se mantiene para representar que la entrada puede contener
#      listas anidadas a cualquier nivel.
#
# No se fuerza ninguna característica exclusiva de Python 3.11:
# el algoritmo basado en pila ya es eficiente e idiomático y las
# novedades de 3.11 no aportan una mejora directa en este caso.

from typing import Any


def flatten(nested: Any) -> list[int]:
    result = []
    stack = [nested]

    while stack:
        item = stack.pop()

        if isinstance(item, list):
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)

    return result


assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, 2, 3]) == [1, 2, 3]


# Cambios realizados:
# 1. Se reemplazó List[int] por list[int].
#    Es la sintaxis moderna de tipado genérico disponible desde Python 3.9.
#
# 2. Se eliminó la importación de List.
#    Ya no es necesaria y simplifica el código.
#
# 3. Se conservó Any.
#    La estructura puede contener listas anidadas arbitrariamente.
#
# 4. Se mantuvo la pila basada en list.
#    Permite realizar el recorrido iterativamente sin recurrir a
#    recursión y mantiene el mismo comportamiento.
#
# 5. Se conservaron exactamente los tres casos de prueba originales.
#
# 6. No se modificó el algoritmo.
#    Por lo tanto, el comportamiento y el orden de los elementos
#    son idénticos al código de Python 3.9.
#
# 7. No se introdujeron características de Python 3.11 artificialmente.
#    Por ejemplo, ExceptionGroup, except* o mejoras internas de
#    rendimiento no aportan valor a este algoritmo concreto.