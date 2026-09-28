# P02: Binary Search — Python 3.11
#
# Características utilizadas:
# 1. list[int]:
#    Se utiliza la sintaxis de tipos incorporada de Python para listas,
#    evitando importar List desde typing. Esta sintaxis está disponible
#    desde Python 3.9 y es idiomática en Python 3.11.
#
# 2. Type hints modernos:
#    Se mantienen las anotaciones de tipos para mejorar la legibilidad
#    y facilitar el análisis estático del código.
#
# Nota:
# Python 3.11 no aporta una característica específica que mejore
# significativamente el algoritmo de búsqueda binaria. Por ello,
# se conserva la lógica iterativa original para garantizar un
# comportamiento idéntico.

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


# Casos de prueba
assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1


# ============================================================
# CAMBIOS REALIZADOS
# ============================================================
#
# 1. Se eliminó:
#       from typing import List
#
#    Ya no es necesario importar List para expresar el tipo de una
#    lista.
#
# 2. Se reemplazó:
#       List[int]
#
#    por:
#       list[int]
#
#    Esta sintaxis es más moderna e idiomática en Python 3.11.
#
# 3. Se mantuvo exactamente el nombre:
#       binary_search
#
# 4. Se conservó la lógica de búsqueda binaria:
#       - left y right delimitan el intervalo.
#       - mid calcula la posición central.
#       - se reduce el intervalo en cada iteración.
#       - se retorna -1 si el elemento no existe.
#
# 5. Se mantuvo el paradigma iterativo mediante while.
#
# 6. Se conservaron los tres tests originales.
#
# 7. No se utiliza ninguna función de búsqueda binaria de la
#    biblioteca estándar.
#
# 8. No se introdujeron características de Python 3.11 que no
#    aporten una mejora real al algoritmo, preservando así el
#    comportamiento y rendimiento de la implementación original.