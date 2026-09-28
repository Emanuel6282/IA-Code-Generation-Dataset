# P02: Binary Search — Python 3.14
#
# Características utilizadas:
# 1. list[int]:
#    Se utiliza la sintaxis moderna para anotar listas de enteros,
#    evitando la importación de List desde typing.
#
# 2. Anotaciones de tipos integradas:
#    Python 3.14 mantiene la sintaxis de tipos integrada de Python,
#    haciendo el código más simple y legible.
#
# Nota:
# No se introduce una característica específica de Python 3.14
# que altere el algoritmo, ya que la búsqueda binaria ya tiene
# una implementación iterativa eficiente. Se conserva la lógica
# original para garantizar un comportamiento idéntico.


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
#    La importación ya no es necesaria.
#
# 2. Se reemplazó:
#       List[int]
#
#    por:
#       list[int]
#
#    Esto proporciona una sintaxis más moderna y legible para
#    expresar listas de enteros.
#
# 3. Se mantuvo exactamente el nombre de la función:
#       binary_search
#
# 4. Se mantuvo el algoritmo iterativo original utilizando while.
#
# 5. Se conservaron las variables left, right y mid y la misma
#    estrategia de reducción del intervalo de búsqueda.
#
# 6. Se conservaron exactamente los tres casos de prueba.
#
# 7. No se utiliza bisect ni ninguna otra función de búsqueda
#    binaria de la biblioteca estándar.
#
# 8. No se fuerzan características específicas de Python 3.14
#    que no proporcionen una ventaja real para este algoritmo.
#
# 9. El comportamiento y los resultados permanecen idénticos.