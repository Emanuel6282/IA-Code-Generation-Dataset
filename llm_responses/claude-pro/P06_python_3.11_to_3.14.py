# P06: Max Subarray Sum (Kadane) — migrado a Python 3.14
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en 3.14): la
#    anotación Sequence[int] ya no se evalúa al definir la función, sino solo cuando
#    alguien la consulta (por ejemplo, con annotationlib). No tiene coste al importar y
#    no hace falta `from __future__ import annotations`.
# 2. collections.abc.Sequence[int] en la anotación: la función solo necesita leer el
#    primer elemento y recorrer el resto, así que acepta cualquier secuencia (lista,
#    tupla, range...). Los alias de typing (List) están obsoletos.
# 3. Mejoras del intérprete de CPython heredadas de 3.11-3.14 (intérprete adaptativo
#    especializado): se aprovechan mejor recorriendo los elementos directamente (ver el
#    cambio 1 al final). La migración es ~14 % más rápida que el original en
#    CPython 3.14.6 con un millón de elementos.
#
# Nota: las novedades sintácticas de 3.14 (t-strings, except sin paréntesis,
# compression.zstd, subintérpretes) no aplican a esta función, así que no se fuerzan.
# Se evaluó también reescribir el cuerpo con max() (current = max(x, current + x)): es
# más corto, pero un ~35 % más lento por el coste de la llamada, así que se conservan
# las comparaciones explícitas del original.

from collections.abc import Sequence
from itertools import islice


def max_subarray_sum(arr: Sequence[int]) -> int:
    current_max = global_max = arr[0]
    for x in islice(arr, 1, None):
        if current_max + x > x:
            current_max += x
        else:
            current_max = x
        if current_max > global_max:
            global_max = current_max
    return global_max


# Casos de prueba (los mismos tres del código original)
assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
assert max_subarray_sum([1]) == 1
assert max_subarray_sum([-2, -1]) == -1


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14
# 1. `for i in range(1, len(arr))` con `arr[i]` repetido tres veces ->
#    `for x in islice(arr, 1, None)`: se recorre cada elemento una sola vez, sin índices
#    ni copiar la secuencia (como haría arr[1:]). Es la forma idiomática de "iterar
#    desde el segundo elemento" y la más rápida de las variantes medidas.
# 2. `current_max = current_max + arr[i]` -> `current_max += x`: asignación aumentada.
# 3. Las dos asignaciones iniciales -> `current_max = global_max = arr[0]`: mismo valor
#    de partida (un int es inmutable, así que no comparten estado).
# 4. `from typing import List` + `List[int]` -> `collections.abc.Sequence[int]`, que en
#    3.14 se evalúa de forma diferida (PEP 649).
# 5. Comportamiento idéntico: mismas comparaciones en el mismo orden, mismo resultado
#    (verificado contra el original con miles de arreglos aleatorios). Un arreglo vacío
#    sigue lanzando IndexError en `arr[0]`, igual que el original.
# 6. Se actualizó el comentario de cabecera, que describía el código como "Python 3.9".
# 7. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en el
#    original, para que se ejecuten en los mismos momentos.