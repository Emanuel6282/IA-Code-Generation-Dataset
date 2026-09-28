# P03: Flatten Nested List — Python 3.14 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[int]) en lugar de typing.List.
#    Disponible desde PEP 585 (Python 3.9), pero es el estilo plenamente
#    idiomatico en 3.14; se elimina la dependencia de "typing.List". Se
#    conserva "typing.Any" para el parametro de entrada, ya que este puede
#    ser un entero individual o una lista anidada a cualquier profundidad
#    (un tipo recursivo), y Python no ofrece una forma nativa simple de
#    expresar eso sin anadir complejidad de Union/alias ajena al alcance de
#    esta migracion.
# 2. PEP 649 (evaluacion diferida de anotaciones, activada por defecto en
#    3.14): las anotaciones "Any" y "list[int]" ahora se evaluan de forma
#    perezosa por el interprete de manera automatica, sin necesidad de
#    "from __future__ import annotations" ni comillas. No cambia la
#    sintaxis visible del codigo, pero es una mejora de comportamiento
#    nativa de 3.14 que ya beneficia a esta funcion.
# 3. Nota de honestidad: tambien revise las t-strings (PEP 750) y otras
#    mejoras internas del interprete en 3.14; ninguna aplica de forma
#    natural aqui, ya que no hay formateo de strings ni necesidad de
#    plantillas en este algoritmo de aplanado con pila explicita. Forzarlas
#    anadiria complejidad sin aportar valor real.

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
#    "list[int]" usando el generico nativo del tipo incorporado (PEP 585).
#    "Any" se mantuvo porque "nested" sigue siendo de tipo
#    heterogeneo/recursivo (entero o lista anidada), sin equivalente nativo
#    mas preciso que no anada complejidad ajena a esta migracion.
# 2. Gracias a PEP 649 (activo por defecto desde 3.14), las anotaciones de
#    tipo de la funcion se evaluan de forma diferida automaticamente por el
#    interprete; esto no requiere cambios de sintaxis adicionales en este
#    caso, pero es una caracteristica nativa de la version que ya opera en
#    segundo plano sobre las anotaciones existentes.
# 3. La logica interna (pila explicita con stack.pop()/stack.append(),
#    recorrido en orden inverso con reversed() para preservar el orden de
#    aparicion, isinstance(item, list) para distinguir listas de enteros) se
#    mantuvo exactamente igual, cumpliendo el requisito de comportamiento
#    identico; no existia ninguna caracteristica de 3.14 que mejorara esta
#    logica de aplanado iterativo sin alterarla artificialmente.
# 4. El nombre de la funcion "flatten" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones logicas (solo se agrego
#    espaciado uniforme tras las comas en las listas literales), ya que
#    "assert" es parte del lenguaje base y no depende de la version de
#    Python.