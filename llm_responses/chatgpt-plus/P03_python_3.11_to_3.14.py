# P03: Flatten Nested List — Python 3.14
#
# CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
#
# 1. Alias de tipo con la sintaxis moderna "type":
#    Permite describir listas recursivamente anidadas.
#    Introducida en Python 3.12 y compatible con 3.14.
#
# 2. Anotaciones de tipos modernas:
#    Se utiliza list[int] en lugar de typing.List[int].
#    Mejora la legibilidad y elimina importaciones.
#
# 3. Evaluación diferida de anotaciones (Python 3.14):
#    Facilita el uso de referencias a tipos definidos
#    posteriormente. No modifica la ejecución del algoritmo.
#
# 4. Mejoras del intérprete de Python 3.14:
#    Se aprovechan las optimizaciones disponibles
#    sin alterar el comportamiento del código.
#
# 5. Estilo iterativo:
#    Se conservan while, for y una pila explícita.
#    No se utiliza recursión ni bibliotecas externas.


type NestedList = list[int | NestedList]


def flatten(nested: NestedList) -> list[int]:
    result: list[int] = []
    stack: list[int | NestedList] = [nested]

    while stack:
        item = stack.pop()

        if isinstance(item, list):
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)

    return result


# TESTS ORIGINALES

# Test 1: Lista anidada
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

# Test 2: Lista vacía
assert flatten([]) == []

# Test 3: Lista plana
assert flatten([1, 2, 3]) == [1, 2, 3]


# RESUMEN DE CAMBIOS — PYTHON 3.11 A PYTHON 3.14
#
# 1. Se reemplazó typing.List[int] por list[int].
#    Motivo: utilizar las anotaciones modernas integradas.
#
# 2. Se incorporó el alias recursivo NestedList.
#    Motivo: describir explícitamente la estructura
#    de listas anidadas de enteros.
#
# 3. Se sustituyó Any por NestedList.
#    Motivo: proporcionar un contrato de tipos más preciso
#    para las entradas válidas del problema.
#
# 4. Se añadieron anotaciones a result y stack.
#    Motivo: mejorar la documentación y el análisis estático.
#
# 5. Se conservaron los bucles while y for.
#    Motivo: mantener el algoritmo estrictamente iterativo.
#
# 6. Se mantuvo reversed(item).
#    Motivo: conservar el orden original de los elementos
#    durante el procesamiento mediante una pila LIFO.
#
# 7. Se conservaron append() y pop().
#    Motivo: utilizar operaciones eficientes de las listas.
#
# 8. Se mantuvieron exactamente los tres assert originales.
#    Motivo: verificar que los resultados no cambien.
#
# 9. Se aprovecha la evaluación diferida de anotaciones
#    disponible en Python 3.14.
#    Motivo: facilitar el trabajo con anotaciones de tipos.
#
# NOTA:
# La sintaxis "type" y list[int] no fueron introducidas
# específicamente en Python 3.14.
# Se utilizan porque son idiomáticas y compatibles con
# esta versión del lenguaje.