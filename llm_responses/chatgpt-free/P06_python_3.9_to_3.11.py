# P06: Max Subarray Sum (Kadane) — Python 3.11
#
# Características utilizadas:
#
# 1. list[int]:
#    Se utiliza la sintaxis de tipos genéricos incorporada en Python,
#    evitando la necesidad de importar List desde typing. Es más concisa
#    y es idiomática en Python 3.11.
#
# 2. Anotaciones de tipo modernas:
#    La función conserva una anotación explícita para indicar que recibe
#    una lista de enteros y retorna un entero.
#
# Nota:
# Python 3.11 incorpora mejoras de rendimiento en el intérprete, pero
# estas son transparentes para este algoritmo. No existe una característica
# sintáctica de Python 3.11 que mejore materialmente la implementación
# de Kadane, por lo que no se introduce ninguna de forma artificial.


def max_subarray_sum(arr: list[int]) -> int:
    current_max = arr[0]
    global_max = arr[0]

    for i in range(1, len(arr)):
        if current_max + arr[i] > arr[i]:
            current_max = current_max + arr[i]
        else:
            current_max = arr[i]

        if current_max > global_max:
            global_max = current_max

    return global_max


# Tests

assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
assert max_subarray_sum([1]) == 1
assert max_subarray_sum([-2, -1]) == -1


# Cambios realizados:
#
# 1. Se reemplazó:
#
#       from typing import List
#
#    por la sintaxis:
#
#       list[int]
#
#    Esta forma es más moderna y concisa, y evita una importación
#    innecesaria.
#
# 2. La firma pasó de:
#
#       def max_subarray_sum(arr: List[int]) -> int:
#
#    a:
#
#       def max_subarray_sum(arr: list[int]) -> int:
#
#    manteniendo exactamente el mismo contrato de la función.
#
# 3. Se mantuvo el algoritmo de Kadane sin modificaciones. Esto garantiza
#    exactamente los mismos resultados y la misma complejidad:
#
#       Tiempo: O(n)
#       Espacio adicional: O(1)
#
# 4. Se conservaron exactamente los tres casos de prueba del código base.
#
# 5. Se mantuvo el nombre max_subarray_sum.
#
# 6. No se introdujeron características como match/case, ya que no aportan
#    una mejora real a este algoritmo.
#
# 7. Las mejoras de rendimiento internas de Python 3.11 son transparentes
#    para el código y no requieren modificar el algoritmo.