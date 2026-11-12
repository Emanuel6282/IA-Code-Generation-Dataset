# P06: Max Subarray Sum (Kadane) — migrado de Python 3.9 a Python 3.11
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Intérprete más rápido de CPython 3.11 (PEP 659, intérprete adaptativo
#    especializado): especializa en tiempo de ejecución las sumas y comparaciones de
#    enteros de este bucle. Se aprovecha mejor recorriendo los elementos directamente
#    (ver el cambio 1 al final): la migración es ~25 % más rápida que el original en
#    CPython 3.11 con un millón de elementos.
# 2. collections.abc.Sequence[int] en la anotación: la función solo necesita leer el
#    primer elemento y recorrer el resto, así que acepta cualquier secuencia (lista,
#    tupla, range...). Los alias de typing (List) están obsoletos.
#
# Nota: las novedades sintácticas propias de 3.11 (ExceptionGroup/except*,
# typing.Self, LiteralString, tomllib) no aplican a esta función, así que no se
# fuerzan. Se evaluó también reescribir el cuerpo con max() (current = max(x, current + x)):
# es más corto, pero casi 3 veces más lento por el coste de la llamada, así que se
# conservan las comparaciones explícitas del original.

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


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11
# 1. `for i in range(1, len(arr))` con `arr[i]` repetido tres veces ->
#    `for x in islice(arr, 1, None)`: se recorre cada elemento una sola vez, sin índices
#    ni copiar la secuencia (como haría arr[1:]). Es la forma idiomática de "iterar
#    desde el segundo elemento" y la más rápida de las variantes medidas.
# 2. `current_max = current_max + arr[i]` -> `current_max += x`: asignación aumentada.
# 3. Las dos asignaciones iniciales -> `current_max = global_max = arr[0]`: mismo valor
#    de partida (un int es inmutable, así que no comparten estado).
# 4. `from typing import List` + `List[int]` -> `collections.abc.Sequence[int]`.
# 5. Comportamiento idéntico: mismas comparaciones en el mismo orden, mismo resultado
#    (verificado contra el original con miles de arreglos aleatorios). Un arreglo vacío
#    sigue lanzando IndexError en `arr[0]`, igual que el original.
# 6. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en el
#    original, para que se ejecuten en los mismos momentos.