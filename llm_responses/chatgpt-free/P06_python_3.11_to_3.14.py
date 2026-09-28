# P06: Max Subarray Sum (Kadane) — Python 3.14
#
# Características utilizadas:
#
# 1. list[int]:
#    Se utiliza la sintaxis moderna de tipos genéricos para anotar la
#    lista de entrada sin importar List desde typing.
#
# 2. Anotaciones de tipo modernas:
#    Mantienen explícito que la función recibe una lista de enteros
#    y retorna un entero.
#
# Nota:
# Python 3.14 incorpora nuevas características del lenguaje, pero ninguna
# mejora materialmente este algoritmo de Kadane. Por ello, no se introducen
# características nuevas de forma artificial que compliquen o alteren
# una implementación ya óptima.
#
# Complejidad:
# - Tiempo: O(n)
# - Espacio adicional: O(1)


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
# 1. Se eliminó:
#
#       from typing import List
#
#    porque ya no es necesario para expresar el tipo de la lista.
#
# 2. Se reemplazó:
#
#       List[int]
#
#    por:
#
#       list[int]
#
#    Esta sintaxis es más concisa y apropiada para código Python moderno.
#
# 3. Se mantuvo exactamente la función:
#
#       max_subarray_sum
#
# 4. Se mantuvo intacto el algoritmo de Kadane. No se cambia la lógica
#    porque ya tiene una complejidad óptima de O(n) en tiempo y O(1)
#    en espacio adicional.
#
# 5. Se conservaron exactamente los tres casos de prueba originales.
#
# 6. Se mantiene el comportamiento para arreglos cuyos elementos son
#    todos negativos, ya que current_max y global_max comienzan con
#    arr[0] en lugar de utilizar 0 como valor inicial.
#
# 7. No se introducen características de Python 3.14 de manera artificial.
#    Las nuevas características de una versión solo deben utilizarse
#    cuando aportan una mejora real al código.