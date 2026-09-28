# P05: Transaction Pipeline — Python 3.11 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[dict[str, Any]]) en lugar de
#    typing.List/typing.Dict. Disponible desde PEP 585 (Python 3.9), pero es
#    el estilo plenamente idiomatico en 3.11; se elimina la dependencia de
#    "typing.List"/"typing.Dict". Se conserva "typing.Any" para el tipo de
#    los valores del diccionario, ya que el codigo base no restringia el
#    tipo de cada campo (id, amount, status, category tienen tipos
#    distintos) y no se introdujo aqui una estructura mas especifica como
#    TypedDict para no exceder el alcance de una simple migracion de
#    version con comportamiento identico.
# 2. Nota de honestidad: revise tambien los grupos de excepciones (except*,
#    PEP 654) y los mensajes de error mas precisos en tracebacks (PEP 657).
#    Ninguna aplica de forma natural aqui: no hay manejo de excepciones en
#    este algoritmo de filtrado y agregacion, y la mejora de precision de
#    tracebacks es automatica (no requiere cambios de sintaxis).

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
#    los tipos incorporados (PEP 585, disponible desde 3.9, estilo estandar
#    en 3.11). "Any" se mantuvo porque los valores del diccionario de cada
#    transaccion son heterogeneos (int, float, str) y el codigo base no
#    definia una estructura mas precisa como TypedDict; introducir una aqui
#    excederia el alcance de una migracion de version con comportamiento
#    identico.
# 2. La logica interna (bucle for, condicion "status == approved and
#    amount > 0", acumulacion "total = total + t[\"amount\"] * 0.9", y
#    "round(total, 2)" final) se mantuvo exactamente igual, cumpliendo el
#    requisito de comportamiento identico; no existia ninguna
#    caracteristica de 3.11 que mejorara esta logica de filtrado y
#    agregacion sin alterarla artificialmente.
# 3. El nombre de la funcion "process_transactions" y los tres casos de
#    prueba con "assert" se conservaron sin modificaciones logicas (solo se
#    agrego espaciado uniforme tras las comas en los diccionarios
#    literales), ya que ya eran compatibles con Python 3.11.