# P03: Flatten Nested List — Python 3.14
#
# Características utilizadas:
# 1. Tipado genérico incorporado: list[int]
#    - Sintaxis moderna para expresar listas de enteros.
#    - Evita utilizar List[int] de typing.
#
# 2. Anotaciones de tipo modernas de Python:
#    - Se mantiene Any para representar una estructura que puede
#      contener listas anidadas a cualquier nivel.
#
# Nota:
# Python 3.14 no incorpora una característica específica que mejore
# directamente este algoritmo de recorrido. Por ello, se mantiene
# el algoritmo iterativo basado en una pila, evitando cambios
# artificiales que no mejorarían legibilidad ni rendimiento.

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
#
# 1. Se reemplazó List[int] por list[int].
#    Es la sintaxis moderna de genéricos incorporados y resulta
#    más legible en Python 3.14.
#
# 2. Se eliminó la importación de List.
#    Ya no es necesaria al utilizar list[int].
#
# 3. Se conservó Any.
#    La entrada puede contener listas anidadas arbitrariamente.
#
# 4. Se mantuvo el algoritmo basado en una pila.
#    Esto conserva el comportamiento iterativo original y evita
#    depender de la profundidad de la recursión.
#
# 5. Se mantuvieron los mismos tres tests y los mismos resultados.
#
# 6. Se conservaron while, pop(), reversed() y append().
#    Cambiarlos no produciría una mejora significativa para este
#    algoritmo y podría alterar su comportamiento.
#
# 7. No se introdujeron características de Python 3.14 de manera
#    artificial. Las novedades de esta versión no aportan una
#    mejora directa de legibilidad o rendimiento para este problema.
#
# 8. La complejidad permanece:
#    - Tiempo: O(n)
#    - Espacio adicional: O(n)