# P05: Transaction Pipeline — migrado a Python 3.14
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en 3.14): las
#    anotaciones (el TypedDict y la firma) ya no se evalúan al definir la clase o la
#    función, sino solo cuando alguien las consulta (por ejemplo, con annotationlib).
#    No tienen coste al importar y no hace falta `from __future__ import annotations`.
# 2. enum.StrEnum (Python 3.11+): da nombre al estado "approved" sin cambiar la
#    comparación. Sus miembros son str de verdad, así que
#    `t["status"] == Status.APPROVED` se evalúa exactamente igual que
#    `t["status"] == "approved"`, pero un error de escritura en el nombre del estado
#    pasa a ser un AttributeError en lugar de un filtro que no deja pasar nada.
# 3. typing.NotRequired en un TypedDict (PEP 655, Python 3.11+): sustituye a
#    Dict[str, Any] por una descripción precisa de cada transacción, y marca
#    `category` como opcional, ya que la función no la usa. Es solo una anotación.
# 4. Mejoras del intérprete de CPython heredadas de 3.11-3.14: aceleran este bucle sin
#    cambiar su estructura, por eso se conserva el for.
#
# Nota: no se usan t-strings, `except` sin paréntesis ni Decimal.from_number (todas
# nuevas en 3.14) porque no aportan nada aquí; from_number, además, cambiaría el
# comportamiento si se pasara a Decimal (ver la nota sobre precisión al final).

from enum import StrEnum
from typing import NotRequired, TypedDict


class Status(StrEnum):
    APPROVED = "approved"
    REJECTED = "rejected"


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: NotRequired[str]


COMMISSION_FACTOR = 0.9   # se descuenta una comisión del 10 %


def process_transactions(transactions: list[Transaction]) -> float:
    total = 0.0
    for t in transactions:
        if t["status"] == Status.APPROVED and t["amount"] > 0:
            total += t["amount"] * COMMISSION_FACTOR
    return round(total, 2)


# Casos de prueba (los mismos tres del código original)
assert process_transactions([]) == 0.0
assert process_transactions([{"id": 1, "amount": 1000.0, "status": "approved", "category": "x"}]) == 900.0
assert process_transactions([{"id": 1, "amount": 100.0, "status": "rejected", "category": "x"}]) == 0.0


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14
# 1. `from typing import List, Dict, Any` -> TypedDict con NotRequired y genéricos
#    nativos (list[...]): los alias de typing están obsoletos y Dict[str, Any] no
#    documentaba qué campos tiene una transacción. En 3.14 estas anotaciones se
#    evalúan de forma diferida (PEP 649).
# 2. "approved" literal -> Status.APPROVED (StrEnum): mismo resultado de la
#    comparación, con un nombre comprobable.
# 3. Factor 0.9 literal -> constante con nombre COMMISSION_FACTOR, que documenta qué es.
# 4. `total = total + ...` -> `total += ...`: asignación aumentada, más concisa.
#    Las operaciones de coma flotante se hacen en el mismo orden, así que el
#    resultado es idéntico al bit.
# 5. Se conserva el bucle for en lugar de sum(): desde Python 3.12, sum() con float usa
#    suma compensada (Neumaier) y da resultados distintos en el último bit en muchos
#    casos, lo que rompería el requisito de comportamiento idéntico.
# 6. Se actualizó el comentario de cabecera, que describía el código como "Python 3.9".
# 7. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en el
#    original. Comportamiento verificado contra el original con miles de listas
#    aleatorias (igualdad exacta de los float devueltos).
#
# NOTA SOBRE PRECISIÓN (no se cambia para respetar el comportamiento idéntico):
# el original, y por tanto esta migración, calcula con float. Con dinero eso da
# resultados incorrectos en algunos redondeos: por ejemplo, para amount = 0.95 el valor
# exacto es 0.855, que debería redondearse a 0.86, pero devuelve 0.85. Para corregirlo
# habría que calcular con decimal.Decimal(str(amount)) (no con Decimal.from_number,
# que conserva el error del float), lo que cambiaría el resultado en esos casos.