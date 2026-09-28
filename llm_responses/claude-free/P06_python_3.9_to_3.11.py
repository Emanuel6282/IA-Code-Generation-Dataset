# P06: Max Subarray Sum (Kadane) — Python 3.11 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[int]) en lugar de typing.List.
#    Disponible desde PEP 585 (Python 3.9), pero es el estilo plenamente
#    idiomatico en 3.11; se elimina la dependencia del modulo "typing".
# 2. Nota de honestidad: revise tambien los grupos de excepciones (except*,
#    PEP 654) y los mensajes de error mas precisos en tracebacks (PEP 657).
#    Ninguna aplica de forma natural aqui, ya que no hay manejo de
#    excepciones en este algoritmo de Kadane, y la mejora de precision de
#    tracebacks es automatica (no requiere cambios de sintaxis).

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

assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
assert max_subarray_sum([1]) == 1
assert max_subarray_sum([-2, -1]) == -1

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se elimino "from typing import List" y se reemplazo la anotacion
#    List[int] -> list[int]. Desde Python 3.9 (PEP 585) los tipos
#    incorporados soportan subindices genericos de forma nativa, por lo que
#    ya no es necesario importar equivalentes desde "typing". En 3.11 esto
#    es el estilo estandar y reduce una dependencia innecesaria.
# 2. La logica interna (bucle for, comparacion "current_max + arr[i] >
#    arr[i]" para decidir entre extender o reiniciar, actualizacion de
#    "global_max") se mantuvo exactamente igual, ya que el requisito pedia
#    comportamiento identico; no habia ninguna caracteristica de 3.11 que
#    mejorara esta logica del algoritmo de Kadane sin alterarla
#    artificialmente.
# 3. El nombre de la funcion "max_subarray_sum" y los tres casos de prueba
#    con "assert" se conservaron sin modificaciones logicas (solo se agrego
#    espaciado uniforme tras las comas en las listas literales), ya que ya
#    eran compatibles con Python 3.11.