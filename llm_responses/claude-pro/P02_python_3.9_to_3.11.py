# P02: Binary Search — migrado de Python 3.9 a Python 3.11
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Intérprete más rápido de CPython 3.11 (PEP 659, intérprete adaptativo
#    especializado): especializa en tiempo de ejecución las operaciones que más
#    se repiten en este bucle (comparaciones de enteros, indexación de listas,
#    división entera). El código se acelera sin cambiar ninguna línea; por eso
#    se conserva el bucle simple, que es justo el patrón que más se beneficia.
# 2. collections.abc.Sequence[int] en la anotación del parámetro: la función solo
#    necesita len() e indexación, así que acepta cualquier secuencia (lista,
#    tupla, range...). Los genéricos de collections.abc son la forma recomendada
#    desde 3.9 y los alias de typing (List) están obsoletos.
#
# Nota: las novedades sintácticas propias de 3.11 (ExceptionGroup/except*,
# typing.Self, LiteralString, tomllib) no aplican a una búsqueda binaria, así
# que no se fuerzan: usarlas aquí empeoraría la legibilidad sin ganar nada.

from collections.abc import Sequence


def binary_search(arr: Sequence[int], target: int) -> int:
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


# Casos de prueba (los mismos tres del código original)
assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11
# 1. `from typing import List` -> `from collections.abc import Sequence`: los
#    alias de typing para colecciones están obsoletos; lo idiomático es usar los
#    tipos nativos o los de collections.abc.
# 2. `arr: List[int]` -> `arr: Sequence[int]`: la anotación describe lo que la
#    función realmente necesita (solo lee por índice), no una implementación
#    concreta. Es solo una anotación: el comportamiento en tiempo de ejecución
#    no cambia, y con una lista se obtienen exactamente los mismos resultados.
# 3. El algoritmo no cambia: mismas variables, mismas comparaciones y mismo
#    orden de ramas. Así se garantiza el mismo índice incluso con valores
#    repetidos, donde otra variante podría devolver otra aparición válida.
# 4. Formato PEP 8 en los tests ([1, 3, 5, 7, 9] con espacios). Los asserts
#    siguen a nivel de módulo, igual que en el original, para que se ejecuten en
#    los mismos momentos (también al importar el archivo).