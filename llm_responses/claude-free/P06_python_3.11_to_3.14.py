# P06: Max Subarray Sum (Kadane) — Python 3.14 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[int]) en lugar de typing.List.
#    Disponible desde PEP 585 (Python 3.9), pero es el estilo plenamente
#    idiomatico en 3.14; se elimina la dependencia del modulo "typing".
# 2. PEP 649 (evaluacion diferida de anotaciones, activada por defecto en
#    3.14): la anotacion "list[int]" del parametro "arr" y el tipo de
#    retorno "int" ahora se evaluan de forma perezosa (lazy) por el
#    interprete de manera automatica, sin necesidad de
#    "from __future__ import annotations" ni comillas. No cambia la
#    sintaxis visible del codigo, pero es una mejora de comportamiento
#    nativa de 3.14 que ya beneficia a esta funcion.
# 3. Nota de honestidad: tambien revise las t-strings (PEP 750) y otras
#    mejoras internas del interprete en 3.14; ninguna aplica de forma
#    natural aqui, ya que no hay formateo de strings ni necesidad de
#    plantillas en este algoritmo de Kadane puramente numerico. Forzarlas
#    anadiria complejidad sin aportar valor real. Tambien se reviso
#    "except*" (PEP 654); no aplica porque no hay manejo de excepciones en
#    este algoritmo.

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
#    List[int] -> list[int], usando genericos nativos de los tipos
#    incorporados (PEP 585). Esto reduce una dependencia innecesaria del
#    modulo "typing" y es el estilo estandar en 3.14.
# 2. Gracias a PEP 649 (activo por defecto desde 3.14), las anotaciones de
#    tipo de la funcion se evaluan de forma diferida automaticamente por el
#    interprete; esto no requiere cambios de sintaxis adicionales en este
#    caso, pero es una caracteristica nativa de la version que ya opera en
#    segundo plano sobre las anotaciones existentes.
# 3. La logica interna (bucle for, comparacion "current_max + arr[i] >
#    arr[i]" para decidir entre extender o reiniciar, actualizacion de
#    "global_max") se mantuvo exactamente igual, cumpliendo el requisito
#    de comportamiento identico; no existia ninguna caracteristica de 3.14
#    que mejorara esta logica del algoritmo de Kadane sin alterarla
#    artificialmente.
# 4. El nombre de la funcion "max_subarray_sum" y los tres casos de prueba
#    con "assert" se conservaron sin modificaciones logicas (solo se agrego
#    espaciado uniforme tras las comas en las listas literales), ya que
#    "assert" es parte del lenguaje base y no depende de la version de
#    Python.