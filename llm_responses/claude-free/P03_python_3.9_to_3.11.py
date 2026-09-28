# P03: Flatten Nested List — Python 3.11 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[int]) en lugar de typing.List.
#    Disponible desde PEP 585 (Python 3.9), pero es el estilo plenamente
#    idiomatico en 3.11; se elimina la dependencia de "typing.List". Se
#    conserva "typing.Any" para el parametro de entrada, ya que este puede
#    ser un entero individual o una lista anidada a cualquier profundidad
#    (un tipo recursivo), y Python no ofrece una forma nativa simple de
#    expresar eso sin anadir complejidad de Union/alias que no aportaria
#    claridad real aqui.
# 2. Nota de honestidad: revise tambien los grupos de excepciones (except*,
#    PEP 654) y los mensajes de error mas precisos en tracebacks (PEP 657).
#    Ninguna aplica de forma natural aqui: no hay manejo de excepciones en
#    este algoritmo de aplanado con pila explicita, y la mejora de
#    precision de tracebacks es automatica (no requiere cambios de
#    sintaxis).

from typing import Any

def flatten(nested: Any) -> list[int]:
    result = []
    stack = [nested]
    while stack:
        item = stack.pop()
        if isinstance(item, list):
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)
    return result

assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, 2, 3]) == [1, 2, 3]

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se elimino "from typing import Any, List" y se reemplazo el import por
#    "from typing import Any" unicamente, ya que "List[int]" se convirtio en
#    "list[int]" usando el generico nativo del tipo incorporado (PEP 585,
#    disponible desde 3.9 y estilo estandar en 3.11). "Any" se mantuvo
#    porque el parametro "nested" sigue siendo de tipo heterogeneo/recursivo
#    (puede ser un entero o una lista anidada), y no existe un equivalente
#    nativo mas preciso sin introducir complejidad adicional ajena al
#    alcance de esta migracion.
# 2. La logica interna (pila explicita con stack.pop()/stack.append(),
#    recorrido en orden inverso con reversed() para preservar el orden de
#    aparicion, isinstance(item, list) para distinguir listas de enteros) se
#    mantuvo exactamente igual, cumpliendo el requisito de comportamiento
#    identico; no existia ninguna caracteristica de 3.11 que mejorara esta
#    logica de aplanado iterativo sin alterarla artificialmente.
# 3. El nombre de la funcion "flatten" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones logicas (solo se agrego
#    espaciado uniforme tras las comas en las listas literales), ya que ya
#    eran compatibles con Python 3.11.