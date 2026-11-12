"""Transaction Pipeline - solución funcional para Python 3.9."""

from decimal import ROUND_HALF_UP, Decimal
from functools import reduce
from operator import add
from typing import TypedDict, Union


class Transaction(TypedDict):
    """Estructura de cada transacción (TypedDict documenta y tipa las claves)."""

    id: int
    amount: Union[float, Decimal]
    status: str        # "approved" o "rejected"
    category: str


COMMISSION_FACTOR = Decimal("0.9")   # se descuenta una comisión del 10 %
CENT = Decimal("0.01")


def _is_valid(transaction: Transaction) -> bool:
    """Paso 1 (filtro): aprobada y con monto positivo."""
    return transaction["status"] == "approved" and transaction["amount"] > 0


def _net_amount(transaction: Transaction) -> Decimal:
    """Paso 2 (transformación): monto tras la comisión del 10 %.

    str() conserva el valor decimal tal como se escribió (0.95 -> "0.95"), en lugar de
    la aproximación binaria del float.
    """
    return Decimal(str(transaction["amount"])) * COMMISSION_FACTOR


# Python 3.9 (PEP 585): genéricos nativos (list[...]) en lugar de typing.List.
def process_transactions(transactions: list[Transaction]) -> float:
    """Suma los montos de las transacciones aprobadas con monto positivo, tras
    descontar una comisión del 10 %, y retorna el total redondeado a 2 decimales.

    Es un pipeline puro que corresponde uno a uno con el enunciado:
    filter (paso 1) -> map (paso 2) -> reduce (paso 3). Los cálculos se hacen con
    Decimal para evitar los errores de representación de los float.
    """
    total = reduce(add, map(_net_amount, filter(_is_valid, transactions)), Decimal("0"))
    # Redondeo comercial: los .5 se redondean hacia arriba (0.855 -> 0.86).
    return float(total.quantize(CENT, rounding=ROUND_HALF_UP))


if __name__ == "__main__":
    # Campos comunes de los tests. El operador de unión de diccionarios `|`
    # (PEP 584, nuevo en Python 3.9) crea cada transacción como un diccionario nuevo
    # que combina la base con sus campos propios, sin modificar BASE.
    BASE = {"status": "approved", "category": "general"}

    test_cases: tuple[tuple[str, tuple[Transaction, ...], float], ...] = (
        # Caso 1: ejemplo del enunciado (solo la primera está aprobada)
        ("ejemplo del enunciado",
         (BASE | {"id": 1, "amount": 100.0},
          BASE | {"id": 2, "amount": 200.0, "status": "rejected"}),
         90.0),
        # Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
        ("sin transacciones válidas",
         (BASE | {"id": 1, "amount": 50.0, "status": "rejected"},
          BASE | {"id": 2, "amount": 0.0},
          BASE | {"id": 3, "amount": -30.0}),
         0.0),
        # Caso 3: decimales y redondeo (0.95 * 0.9 = 0.855 -> 0.86; 0.3 y 0.2 -> 0.45)
        ("redondeo de 0.855", (BASE | {"id": 1, "amount": 0.95},), 0.86),
        ("suma con decimales",
         (BASE | {"id": 1, "amount": 0.3},
          BASE | {"id": 2, "amount": 0.2},
          BASE | {"id": 3, "amount": 99.99, "status": "rejected"}),
         0.45),
    )

    assert process_transactions([]) == 0.0, "lista vacía -> 0.0"

    # map calcula el resultado de cada caso; filter se queda solo con los que fallan.
    results = map(lambda case: (case, process_transactions(list(case[1]))), test_cases)
    failures = tuple(filter(lambda pair: pair[1] != pair[0][2], results))

    assert not failures, "\n".join(
        map(lambda pair: f"{pair[0][0]}: esperado {pair[0][2]}, obtenido {pair[1]}", failures)
    )
    print("Todas las pruebas pasaron.")