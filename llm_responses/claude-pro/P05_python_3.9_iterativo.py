"""Transaction Pipeline - solución iterativa para Python 3.9."""

from decimal import ROUND_HALF_UP, Decimal
from typing import TypedDict, Union


class Transaction(TypedDict):
    """Estructura de cada transacción (TypedDict documenta y tipa las claves)."""

    id: int
    amount: Union[float, Decimal]
    status: str        # "approved" o "rejected"
    category: str


COMMISSION_FACTOR = Decimal("0.9")   # se descuenta una comisión del 10 %
CENT = Decimal("0.01")


# Python 3.9 (PEP 585): genéricos nativos (list[...]) en lugar de typing.List.
def process_transactions(transactions: list[Transaction]) -> float:
    """Suma los montos de las transacciones aprobadas con monto positivo, tras
    descontar una comisión del 10 %, y retorna el total redondeado a 2 decimales.

    Los cálculos se hacen con Decimal para evitar los errores de representación de
    los float (por ejemplo, 0.95 * 0.9 redondeado con float da 0.85 en lugar de 0.86).
    """
    total = Decimal("0")
    for transaction in transactions:
        # 1. Filtrar
        if transaction["status"] != "approved" or transaction["amount"] <= 0:
            continue
        # 2. Aplicar la comisión. str() conserva el valor decimal tal como se escribió
        #    (0.95 -> "0.95"), en lugar de la aproximación binaria del float.
        net_amount = Decimal(str(transaction["amount"])) * COMMISSION_FACTOR
        # 3. Acumular
        total += net_amount
    # Redondeo comercial: los .5 se redondean hacia arriba (0.855 -> 0.86).
    return float(total.quantize(CENT, rounding=ROUND_HALF_UP))


if __name__ == "__main__":
    # Campos comunes de los tests. El operador de unión de diccionarios `|`
    # (PEP 584, nuevo en Python 3.9) crea cada transacción combinando la base con
    # sus campos propios, sin modificar BASE.
    BASE = {"status": "approved", "category": "general"}

    test_cases: list[tuple[str, list[Transaction], float]] = [
        # Caso 1: ejemplo del enunciado (solo la primera está aprobada)
        ("ejemplo del enunciado",
         [BASE | {"id": 1, "amount": 100.0},
          BASE | {"id": 2, "amount": 200.0, "status": "rejected"}],
         90.0),
        # Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
        ("sin transacciones válidas",
         [BASE | {"id": 1, "amount": 50.0, "status": "rejected"},
          BASE | {"id": 2, "amount": 0.0},
          BASE | {"id": 3, "amount": -30.0}],
         0.0),
        # Caso 3: decimales y redondeo (0.95 * 0.9 = 0.855 -> 0.86; 0.3 y 0.2 -> 0.45)
        ("redondeo de 0.855", [BASE | {"id": 1, "amount": 0.95}], 0.86),
        ("suma con decimales",
         [BASE | {"id": 1, "amount": 0.3},
          BASE | {"id": 2, "amount": 0.2},
          BASE | {"id": 3, "amount": 99.99, "status": "rejected"}],
         0.45),
    ]

    assert process_transactions([]) == 0.0, "lista vacía -> 0.0"

    for name, transactions, expected in test_cases:
        result = process_transactions(transactions)
        assert result == expected, f"{name}: esperado {expected}, obtenido {result}"

    print("Todas las pruebas pasaron.")