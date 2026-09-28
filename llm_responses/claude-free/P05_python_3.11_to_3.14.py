# P05: Transaction Pipeline — Python 3.14 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[dict[str, Any]]) en lugar de
#    typing.List/typing.Dict. Disponible desde PEP 585 (Python 3.9), pero es
#    el estilo plenamente idiomatico en 3.14; se elimina la dependencia de
#    "typing.List"/"typing.Dict". Se conserva "typing.Any" para el tipo de
#    los valores del diccionario, ya que el codigo base no restringia el
#    tipo de cada campo (id, amount, status, category tienen tipos
#    distintos) y no se introduce aqui una estructura mas especifica como
#    TypedDict, para no exceder el alcance de una simple migracion de
#    version con comportamiento identico.
# 2. PEP 649 (evaluacion diferida de anotaciones, activada por defecto en
#    3.14): las anotaciones "list[dict[str, Any]]" y "float" ahora se
#    evaluan de forma perezosa por el interprete de manera automatica, sin
#    necesidad de "from __future__ import annotations" ni comillas. No
#    cambia la sintaxis visible del codigo, pero es una mejora de
#    comportamiento nativa de 3.14 que ya beneficia a esta funcion.
# 3. Nota de honestidad: tambien revise las t-strings (PEP 750) y otras
#    mejoras internas del interprete en 3.14; ninguna aplica de forma
#    natural aqui, ya que no hay formateo de strings ni necesidad de
#    plantillas en este algoritmo de filtrado y agregacion. Forzarlas
#    anadiria complejidad sin aportar valor real. Tambien se reviso
#    "except*" (PEP 654); no aplica porque no hay manejo de excepciones en
#    este algoritmo.

from typing import Any

def process_transactions(transactions: list[dict[str, Any]]) -> float:
    total = 0.0
    for t in transactions:
        if t["status"] == "approved" and t["amount"] > 0:
            total = total + t["amount"] * 0.9
    return round(total, 2)

assert process_transactions([]) == 0.0
assert process_transactions([{"id": 1, "amount": 1000.0, "status": "approved", "category": "x"}]) == 900.0
assert process_transactions([{"id": 1, "amount": 100.0, "status": "rejected", "category": "x"}]) == 0.0

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se elimino "from typing import List, Dict, Any" y se reemplazo por
#    "from typing import Any" unicamente, ya que "List[Dict[str, Any]]" se
#    convirtio en "list[dict[str, Any]]" usando los genericos nativos de
#    los tipos incorporados (PEP 585). "Any" se mantuvo porque los valores
#    del diccionario de cada transaccion son heterogeneos (int, float,
#    str) y el codigo base no definia una estructura mas precisa como
#    TypedDict; introducir una aqui excederia el alcance de esta migracion.
# 2. Gracias a PEP 649 (activo por defecto desde 3.14), las anotaciones de
#    tipo de la funcion se evaluan de forma diferida automaticamente por el
#    interprete; esto no requiere cambios de sintaxis adicionales en este
#    caso, pero es una caracteristica nativa de la version que ya opera en
#    segundo plano sobre las anotaciones existentes.
# 3. La logica interna (bucle for, condicion "status == approved and
#    amount > 0", acumulacion "total = total + t[\"amount\"] * 0.9", y
#    "round(total, 2)" final) se mantuvo exactamente igual, cumpliendo el
#    requisito de comportamiento identico; no existia ninguna
#    caracteristica de 3.14 que mejorara esta logica de filtrado y
#    agregacion sin alterarla artificialmente.
# 4. El nombre de la funcion "process_transactions" y los tres casos de
#    prueba con "assert" se conservaron sin modificaciones logicas (solo se
#    agrego espaciado uniforme tras las comas en los diccionarios
#    literales), ya que "assert" es parte del lenguaje base y no depende de
#    la version de Python.