# P02: Binary Search — Python 3.14 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[int]) en lugar de typing.List.
#    Disponible desde PEP 585 (Python 3.9), pero es el estilo plenamente
#    idiomatico en 3.14; se elimina la dependencia del modulo "typing".
# 2. PEP 649 (evaluacion diferida de anotaciones, activada por defecto en
#    3.14): la anotacion "list[int]" del parametro "arr" y el tipo de
#    retorno "int" ahora se evaluan de forma perezosa (lazy) por el
#    interprete de manera automatica, sin necesidad de
#    "from __future__ import annotations" ni comillas. No cambia la sintaxis
#    visible del codigo, pero es una mejora de comportamiento/rendimiento
#    nativa de 3.14 que ya beneficia a esta funcion.
# 3. Nota de honestidad: tambien revise las t-strings (PEP 750) y otras
#    mejoras internas del interprete en 3.14; ninguna aplica de forma
#    natural aqui, ya que no hay formateo de strings ni necesidad de
#    plantillas en un algoritmo de busqueda binaria sobre enteros. Forzarlas
#    anadiria complejidad sin aportar valor real.

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
#    List[int] -> list[int], usando genericos nativos de los tipos
#    incorporados (PEP 585). Esto reduce una dependencia innecesaria del
#    modulo "typing" y es el estilo estandar en 3.14.
# 2. Gracias a PEP 649 (activo por defecto desde 3.14), las anotaciones de
#    tipo de la funcion se evaluan de forma diferida automaticamente por el
#    interprete; esto no requiere cambios de sintaxis adicionales en este
#    caso, pero es una caracteristica nativa de la version que ya opera en
#    segundo plano sobre las anotaciones existentes.
# 3. La logica interna (bucle while, calculo de "mid", comparaciones y
#    actualizacion de "left"/"right") se mantuvo exactamente igual,
#    cumpliendo el requisito de comportamiento identico; no existia ninguna
#    caracteristica de 3.14 que mejorara esta logica especifica sin
#    alterarla artificialmente.
# 4. El nombre de la funcion "binary_search" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones logicas (solo se agrego
#    espaciado uniforme tras las comas en las listas literales), ya que
#    "assert" es parte del lenguaje base y no depende de la version de
#    Python.