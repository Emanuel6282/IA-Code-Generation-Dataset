# P02: Binary Search — Python 3.11 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[int]) en lugar de typing.List.
#    Disponible desde PEP 585 (Python 3.9), pero es el estilo plenamente
#    idiomatico en 3.11; se elimina la dependencia del modulo "typing" para
#    este caso simple.
# 2. Nota de honestidad: revise otras novedades de 3.11, como los grupos de
#    excepciones (except*, PEP 654) y los mensajes de error mas precisos en
#    tracebacks (PEP 657). Ninguna aplica de forma natural aqui: no hay
#    manejo de excepciones en este algoritmo, y la mejora de precision de
#    tracebacks es automatica (no requiere cambios de sintaxis). Tampoco se
#    fuerza el uso de "except*" al no existir ningun escenario de excepciones
#    multiples que manejar en una busqueda binaria sobre una lista.

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

assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se elimino "from typing import List" y se reemplazo la anotacion
#    List[int] -> list[int]. Desde Python 3.9 (PEP 585) los tipos
#    incorporados soportan subindices genericos de forma nativa, por lo que
#    ya no es necesario importar equivalentes desde "typing". En 3.11 esto
#    es el estilo estandar y reduce una dependencia innecesaria.
# 2. La logica interna (bucle while, calculo de "mid", comparaciones y
#    actualizacion de "left"/"right") se mantuvo exactamente igual, ya que
#    el requisito pedia comportamiento identico; no habia ninguna
#    caracteristica de 3.11 que mejorara esta logica de busqueda binaria sin
#    alterarla artificialmente.
# 3. El nombre de la funcion "binary_search" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones logicas (solo se agrego
#    espaciado uniforme tras las comas en las listas literales), ya que ya
#    eran compatibles con Python 3.11.