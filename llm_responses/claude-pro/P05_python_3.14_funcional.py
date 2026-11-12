"""Transaction Pipeline - solución funcional para Python 3.14."""

from decimal import ROUND_HALF_UP, Decimal
from enum import StrEnum
from functools import reduce
from operator import add
from string.templatelib import Interpolation, Template
from types import MappingProxyType
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

_CONVERSIONS = MappingProxyType({"r": repr, "s": str, "a": ascii, None: lambda value: value})


def _is_valid(transaction: Transaction) -> bool:
    """Paso 1 (filtro): aprobada y con monto positivo."""
    return transaction["status"] == Status.APPROVED and transaction["amount"] > 0


def _net_amount(transaction: Transaction) -> Decimal:
    """Paso 2 (transformación): monto tras la comisión del 10 %.

    Se convierte con Decimal(str(...)), que conserva el valor tal como se escribió
    (0.95 -> 0.95). Decimal.from_number(), nuevo en 3.14, no sirve aquí: con un float
    conserva su aproximación binaria exacta (0.95 -> 0.94999999999999995559...).
    """
    return Decimal(str(transaction["amount"])) * COMMISSION_FACTOR


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


def _render_part(part: str | Interpolation) -> str:
    """Convierte una parte de un Template en texto, aplicando conversión y formato."""
    if isinstance(part, Interpolation):
        return format(_CONVERSIONS[part.conversion](part.value), part.format_spec)
    return part


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, sin bucles."""
    return "".join(map(_render_part, template))


def _check_case(case: tuple[str, tuple[Transaction, ...], float]) -> str | None:
    """Evalúa un caso y devuelve el mensaje de fallo, o None si pasa."""
    name, transactions, expected = case
    result = process_transactions(list(transactions))
    return None if result == expected else render(
        t"Caso {name!r}: esperado {expected:.2f}, obtenido {result:.2f}"
    )


if __name__ == "__main__":
    # Campos comunes: cada transacción de prueba es un diccionario nuevo creado con `|`,
    # sin modificar BASE.
    BASE = {"status": Status.APPROVED, "category": "general"}

    test_cases: tuple[tuple[str, tuple[Transaction, ...], float], ...] = (
        # Caso 1: ejemplo del enunciado, tal cual (sin `category`, que es NotRequired)
        ("ejemplo del enunciado",
         ({"id": 1, "amount": 100.0, "status": "approved"},
          {"id": 2, "amount": 200.0, "status": "rejected"}),
         90.0),
        # Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
        ("sin transacciones válidas",
         (BASE | {"id": 1, "amount": 50.0, "status": Status.REJECTED},
          BASE | {"id": 2, "amount": 0.0},
          BASE | {"id": 3, "amount": -30.0}),
         0.0),
        # Caso 3: decimales y redondeo (0.95 * 0.9 = 0.855 -> 0.86; 0.3 y 0.2 -> 0.45)
        ("redondeo de 0.855", (BASE | {"id": 1, "amount": 0.95},), 0.86),
        ("suma con decimales",
         (BASE | {"id": 1, "amount": 0.3},
          BASE | {"id": 2, "amount": 0.2},
          BASE | {"id": 3, "amount": 99.99, "status": Status.REJECTED}),
         0.45),
    )

    assert process_transactions([]) == 0.0, "lista vacía -> 0.0"

    # map evalúa todos los casos; filter se queda solo con los mensajes de fallo.
    failures: tuple[str, ...] = tuple(
        filter(lambda message: message is not None, map(_check_case, test_cases))
    )

    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")