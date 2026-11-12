"""Transaction Pipeline - solución iterativa para Python 3.11."""

from decimal import ROUND_HALF_UP, Decimal
from enum import StrEnum
from typing import NotRequired, TypedDict


class Status(StrEnum):
    """Estados posibles de una transacción.

    enum.StrEnum (nuevo en Python 3.11): sus miembros son str de verdad, así que
    Status.APPROVED == "approved" es True y se comparan directamente con los datos de
    entrada, sin `.value` ni conversiones.
    """

    APPROVED = "approved"
    REJECTED = "rejected"


class Transaction(TypedDict):
    """Estructura de cada transacción.

    typing.NotRequired (PEP 655, nuevo en Python 3.11): marca `category` como opcional
    dentro del TypedDict. El enunciado la lista como campo, pero el ejemplo la omite, y
    la función no la necesita.
    """

    id: int
    amount: float | Decimal
    status: str
    category: NotRequired[str]


COMMISSION_FACTOR = Decimal("0.9")   # se descuenta una comisión del 10 %
CENT = Decimal("0.01")


def process_transactions(transactions: list[Transaction]) -> float:
    """Suma los montos de las transacciones aprobadas con monto positivo, tras
    descontar una comisión del 10 %, y retorna el total redondeado a 2 decimales.

    Los cálculos se hacen con Decimal para evitar los errores de representación de
    los float (por ejemplo, 0.95 * 0.9 redondeado con float da 0.85 en lugar de 0.86).
    """
    total = Decimal("0")
    for transaction in transactions:
        # 1. Filtrar
        if transaction["status"] != Status.APPROVED or transaction["amount"] <= 0:
            continue
        # 2. Aplicar la comisión. str() conserva el valor decimal tal como se escribió
        #    (0.95 -> "0.95"), en lugar de la aproximación binaria del float.
        net_amount = Decimal(str(transaction["amount"])) * COMMISSION_FACTOR
        # 3. Acumular
        total += net_amount
    # Redondeo comercial: los .5 se redondean hacia arriba (0.855 -> 0.86).
    return float(total.quantize(CENT, rounding=ROUND_HALF_UP))


if __name__ == "__main__":
    BASE = {"status": Status.APPROVED, "category": "general"}

    test_cases: list[tuple[str, list[Transaction], float]] = [
        # Caso 1: ejemplo del enunciado, tal cual (sin `category`, que es NotRequired)
        ("ejemplo del enunciado",
         [{"id": 1, "amount": 100.0, "status": "approved"},
          {"id": 2, "amount": 200.0, "status": "rejected"}],
         90.0),
        # Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
        ("sin transacciones válidas",
         [BASE | {"id": 1, "amount": 50.0, "status": Status.REJECTED},
          BASE | {"id": 2, "amount": 0.0},
          BASE | {"id": 3, "amount": -30.0}],
         0.0),
        # Caso 3: decimales y redondeo (0.95 * 0.9 = 0.855 -> 0.86; 0.3 y 0.2 -> 0.45)
        ("redondeo de 0.855", [BASE | {"id": 1, "amount": 0.95}], 0.86),
        ("suma con decimales",
         [BASE | {"id": 1, "amount": 0.3},
          BASE | {"id": 2, "amount": 0.2},
          BASE | {"id": 3, "amount": 99.99, "status": Status.REJECTED}],
         0.45),
    ]

    assert process_transactions([]) == 0.0, "lista vacía -> 0.0"

    # Se ejecutan todos los casos aunque alguno falle y se reportan juntos.
    failures: list[AssertionError] = []
    for name, transactions, expected in test_cases:
        try:
            result = process_transactions(transactions)
            assert result == expected, f"esperado {expected}, obtenido {result}"
        except AssertionError as error:
            # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
            error.add_note(f"Caso fallido: {name} | {len(transactions)} transacción(es)")
            failures.append(error)

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): agrupa todos los fallos
        raise ExceptionGroup("Fallaron casos de prueba de process_transactions", failures)

    print("Todas las pruebas pasaron.")