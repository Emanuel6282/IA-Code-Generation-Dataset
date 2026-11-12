# P03: Flatten Nested List — Python 3.11
#
# CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
#
# 1. list[int]:
#    Anotación moderna para listas de enteros.
#    Disponible desde Python 3.9.
#
# 2. Any de typing:
#    Conserva la flexibilidad del parámetro original.
#
# 3. Mejoras de rendimiento de CPython 3.11:
#    El intérprete incorpora especialización adaptativa
#    de instrucciones, sin cambiar el algoritmo.
#
# 4. Estilo moderno:
#    Formato PEP 8 y anotaciones de tipos explícitas.
#
# 5. Algoritmo iterativo:
#    Se conserva la pila explícita y el bucle while.

from typing import Any


def flatten(nested: Any) -> list[int]:
    result: list[int] = []
    stack: list[Any] = [nested]

    while stack:
        item = stack.pop()

        if isinstance(item, list):
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)

    return result


# TESTS ORIGINALES (SIN CAMBIOS DE COMPORTAMIENTO)

# Test 1: Lista anidada
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

# Test 2: Lista vacía
assert flatten([]) == []

# Test 3: Lista plana
assert flatten([1, 2, 3]) == [1, 2, 3]


# RESUMEN DE CAMBIOS — PYTHON 3.9 A PYTHON 3.11
#
# 1. Se eliminó la importación de List.
#    Motivo: utilizar directamente list[int].
#
# 2. Se mantuvo Any para el parámetro nested.
#    Motivo: conservar la firma flexible original.
#
# 3. Se añadieron anotaciones a result y stack.
#    Motivo: mejorar la legibilidad y facilitar
#    el análisis estático de tipos.
#
# 4. Se conservó el bucle while.
#    Motivo: mantener el comportamiento iterativo.
#
# 5. Se mantuvo reversed(item).
#    Motivo: preservar el orden original de los enteros
#    al utilizar una pila LIFO.
#
# 6. Se mantuvieron append() y pop().
#    Motivo: aprovechar las operaciones eficientes
#    de las listas utilizadas como pilas.
#
# 7. Se conservaron exactamente los tres assert.
#    Motivo: verificar resultados idénticos.
#
# 8. No se introdujeron características innecesarias
#    como match/case.
#    Motivo: isinstance() resulta claro y adecuado
#    para distinguir listas de otros elementos.
#
# NOTA:
# list[int] no es exclusivo de Python 3.11.
# Las mejoras específicas de rendimiento de CPython
# 3.11 son internas y no requieren cambios sintácticos.