"""Transaction Pipeline - solución iterativa para Python 3.14."""

from decimal import ROUND_HALF_UP, Decimal
from enum import StrEnum
from string.templatelib import Interpolation, Template
from typing import NotRequired, TypedDict

# Sentencia `type` (PEP 695, Python 3.12+): alias de tipo explícito y perezoso.
type Amount = float | Decimal


class Status(StrEnum):
    """Estados posibles de una transacción. Sus miembros son str de verdad, así que
    Status.APPROVED == "approved" es True."""

    APPROVED = "approved"
    REJECTED = "rejected"


class Transaction(TypedDict):
    """Estructura de cada transacción. `category` es NotRequired: el enunciado la lista
    como campo, pero el ejemplo la omite, y la función no la necesita."""

    id: int
    amount: Amount
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
        # 2. Aplicar la comisión. Se convierte con Decimal(str(...)), que conserva el
        #    valor tal como se escribió (0.95 -> 0.95). Nota: Decimal.from_number(),
        #    nuevo en 3.14, no sirve aquí: con un float conserva su aproximación binaria
        #    exacta (0.95 -> 0.94999999999999995559...).
        net_amount = Decimal(str(transaction["amount"])) * COMMISSION_FACTOR
        # 3. Acumular
        total += net_amount
    # Redondeo comercial: los .5 se redondean hacia arriba (0.855 -> 0.86).
    return float(total.quantize(CENT, rounding=ROUND_HALF_UP))


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, de forma iterativa.

    A diferencia de una f-string, una t-string no produce un str directamente:
    produce un Template con sus partes literales e interpolaciones por separado.
    """
    parts: list[str] = []
    for item in template:
        if isinstance(item, Interpolation):
            value = item.value
            match item.conversion:
                case "r":
                    value = repr(value)
                case "s":
                    value = str(value)
                case "a":
                    value = ascii(value)
            parts.append(format(value, item.format_spec))
        else:
            parts.append(item)
    return "".join(parts)


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

    for name, transactions, expected in test_cases:
        result = process_transactions(transactions)
        # El mensaje solo se construye si el assert falla; se arma con una t-string y
        # muestra los montos con dos decimales mediante el especificador de formato.
        assert result == expected, render(
            t"Caso {name!r}: esperado {expected:.2f}, obtenido {result:.2f}"
        )

    print("Todas las pruebas pasaron.")