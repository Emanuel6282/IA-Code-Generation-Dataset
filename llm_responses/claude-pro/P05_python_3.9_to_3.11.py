# P05: Transaction Pipeline — migrado de Python 3.9 a Python 3.11
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. enum.StrEnum (nuevo en Python 3.11): da nombre al estado "approved" sin cambiar
#    la comparación. Sus miembros son str de verdad, así que
#    `t["status"] == Status.APPROVED` se evalúa exactamente igual que
#    `t["status"] == "approved"`, pero un error de escritura en el nombre del estado
#    pasa a ser un AttributeError en lugar de un filtro que no deja pasar nada.
# 2. typing.NotRequired en un TypedDict (PEP 655, nuevo en Python 3.11): sustituye a
#    Dict[str, Any] por una descripción precisa de cada transacción, y marca
#    `category` como opcional, ya que la función no la usa. Es solo una anotación: no
#    cambia el comportamiento en tiempo de ejecución.
# 3. Intérprete más rápido de CPython 3.11 (PEP 659): acelera este bucle sin cambiar
#    su estructura, por eso se conserva el for.
#
# Nota: ExceptionGroup/except*, typing.Self, LiteralString y tomllib no aplican a esta
# función, así que no se fuerzan.

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


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11
# 1. `from typing import List, Dict, Any` -> TypedDict con NotRequired y genéricos
#    nativos (list[...]): los alias de typing están obsoletos y Dict[str, Any] no
#    documentaba qué campos tiene una transacción.
# 2. "approved" literal -> Status.APPROVED (StrEnum): mismo resultado de la
#    comparación, con un nombre comprobable.
# 3. Factor 0.9 literal -> constante con nombre COMMISSION_FACTOR, que documenta qué es.
# 4. `total = total + ...` -> `total += ...`: asignación aumentada, más concisa.
#    Las operaciones de coma flotante se hacen en el mismo orden, así que el
#    resultado es idéntico al bit.
# 5. Se conserva el bucle for en lugar de sum(): a partir de Python 3.12, sum() con
#    float usa suma compensada y puede dar un resultado distinto en el último bit, lo
#    que rompería el requisito de comportamiento idéntico si el código se ejecuta en
#    una versión posterior.
# 6. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en el
#    original. Comportamiento verificado contra el original con miles de listas
#    aleatorias (igualdad exacta de los float devueltos).
#
# NOTA SOBRE PRECISIÓN (no se cambia para respetar el comportamiento idéntico):
# el original, y por tanto esta migración, calcula con float. Con dinero eso da
# resultados incorrectos en algunos redondeos: por ejemplo, para amount = 0.95 el valor
# exacto es 0.855, que debería redondearse a 0.86, pero devuelve 0.85. Para corregirlo
# habría que calcular con decimal.Decimal, lo que cambiaría el resultado en esos casos.