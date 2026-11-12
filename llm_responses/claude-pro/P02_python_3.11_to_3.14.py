# P02: Binary Search — migrado a Python 3.14
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en 3.14): las
#    anotaciones ya no se evalúan al definir la función, sino solo cuando alguien
#    las consulta (por ejemplo, con annotationlib). Definir la función cuesta
#    menos y ya no hace falta `from __future__ import annotations` ni poner tipos
#    entre comillas. No requiere cambiar el código: basta con anotar normalmente.
# 2. collections.abc.Sequence[int] en la anotación del parámetro: la función solo
#    necesita len() e indexación, así que acepta cualquier secuencia (lista,
#    tupla, range...). Los alias de typing (List) están obsoletos.
# 3. Mejoras del intérprete de CPython heredadas de 3.11-3.14 (intérprete
#    adaptativo especializado, recolector de basura incremental en 3.14):
#    aceleran este bucle sin cambiar el código, por eso se mantiene simple.
#
# Nota: las novedades sintácticas de 3.14 (t-strings, except sin paréntesis,
# compression.zstd, subintérpretes) no aplican a una búsqueda binaria, así que
# no se fuerzan: usarlas aquí empeoraría la legibilidad sin ganar nada.

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


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14
# 1. `from typing import List` -> `from collections.abc import Sequence`: los
#    alias de typing para colecciones están obsoletos; lo idiomático es usar los
#    tipos nativos o los de collections.abc.
# 2. `arr: List[int]` -> `arr: Sequence[int]`: la anotación describe lo que la
#    función realmente necesita (solo lee por índice). En 3.14 se evalúa de forma
#    diferida (PEP 649), así que no tiene costo al definir la función. Es solo una
#    anotación: con una lista se obtienen exactamente los mismos resultados.
# 3. El algoritmo no cambia: mismas variables, mismas comparaciones y mismo
#    orden de ramas. Así se garantiza el mismo índice incluso con valores
#    repetidos, donde otra variante podría devolver otra aparición válida.
# 4. Se actualizó el comentario de cabecera, que describía el código como
#    "Python 3.9".
# 5. Formato PEP 8 en los tests ([1, 3, 5, 7, 9] con espacios). Los asserts
#    siguen a nivel de módulo, igual que en el original, para que se ejecuten en
#    los mismos momentos (también al importar el archivo).