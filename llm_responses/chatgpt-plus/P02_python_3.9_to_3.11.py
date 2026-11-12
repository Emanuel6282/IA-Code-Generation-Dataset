# P02: Binary Search — Python 3.11
#
# CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
#
# 1. list[int]:
#    Sintaxis moderna de anotaciones de tipos.
#    Disponible desde Python 3.9 y recomendada en Python 3.11.
#    Evita importar List desde typing.
#
# 2. Intérprete especializado de Python 3.11:
#    Python 3.11 incorpora optimizaciones internas que pueden
#    mejorar el rendimiento de operaciones frecuentes.
#    No requiere modificar la lógica del algoritmo.
#
# 3. Compatibilidad con Python 3.11:
#    Se conserva la estructura iterativa original porque
#    sigue siendo clara, eficiente e idiomática.


def binary_search(arr: list[int], target: int) -> int:
    left = 0
    right = len(arr) - 1

    while left <= right:
        mid = (left + right) // 2

        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1


# TESTS ORIGINALES

assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1


# RESUMEN DE CAMBIOS:
#
# 1. Se eliminó "from typing import List".
#    Motivo: Python 3.11 admite directamente list[int].
#
# 2. Se reemplazó List[int] por list[int].
#    Motivo: es la sintaxis moderna e idiomática para
#    anotaciones de listas en Python 3.11.
#
# 3. Se conservó el bucle while y las variables left/right.
#    Motivo: mantienen la lógica iterativa original.
#
# 4. Se mantuvo el cálculo mid = (left + right) // 2.
#    Motivo: preservar exactamente el algoritmo original.
#
# 5. Se conservaron los tres asserts originales.
#    Motivo: verificar el mismo comportamiento.
#
# 6. Se aprovechan automáticamente las optimizaciones
#    internas del intérprete de Python 3.11.
#    Motivo: posibles mejoras de rendimiento sin alterar
#    el código ni los resultados.