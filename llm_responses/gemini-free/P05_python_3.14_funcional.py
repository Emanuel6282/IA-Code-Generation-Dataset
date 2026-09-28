# Python 3.14 - Paradigma Funcional Estricto
# Utiliza `typing.TypedDict` con `NotRequired` (PEP 655) para un tipado estático inmutable,
# Pattern Matching estructural (`match-case`) para un filtrado declarativo puro,
# y una canalización basada en `filter()`, `map()` y `functools.reduce()`.

from functools import reduce
from typing import NotRequired, TypedDict


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: NotRequired[str]


def _is_valid_transaction(tx: Transaction) -> bool:
    """Predicado puro usando Pattern Matching (match-case)."""
    match tx:
        case {"status": "approved", "amount": float(amt) | int(amt)} if amt > 0:
            return True
        case _:
            return False


def _apply_commission(tx: Transaction) -> float:
    """Función de transformación pura."""
    match tx:
        case {"amount": float(amt) | int(amt)}:
            return float(amt) * 0.9
        case _:
            return 0.0


def process_transactions(transactions: list[Transaction]) -> float:
    """Procesa una lista de transacciones usando un pipeline de funciones de alto orden.

    Combina filter(), map() y reduce() con transformaciones inmutables.
    """
    if not transactions:
        return 0.0

    # 1. Etapa de filtrado inmutable
    valid_txs = filter(_is_valid_transaction, transactions)

    # 2. Etapa de transformación inmutable (descuento del 10%)
    discounted_amounts = map(_apply_commission, valid_txs)

    # 3. Etapa de reducción/acumulación inmutable
    total_amount = reduce(lambda acc, curr: acc + curr, discounted_amounts, 0.0)

    return round(total_amount, 2)


# --- Casos de prueba usando assert ---
if __name__ == "__main__":
    # Caso 1: Ejemplo base del enunciado (aprobada vs rechazada)
    t1: list[Transaction] = [
        {"id": 1, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 2, "amount": 200.0, "status": "rejected", "category": "food"},
    ]
    assert process_transactions(t1) == 90.0, f"Falló Caso 1: {process_transactions(t1)}"

    # Caso 2: Lista vacía o sin transacciones válidas
    assert process_transactions([]) == 0.0, "Falló Caso 2: Lista vacía"
    t2: list[Transaction] = [
        {"id": 3, "amount": -50.0, "status": "approved", "category": "tech"},
        {"id": 4, "amount": 150.0, "status": "rejected", "category": "books"},
    ]
    assert process_transactions(t2) == 0.0, "Falló Caso 2: Sin transacciones válidas"

    # Caso 3: Múltiples transacciones válidas y cálculo de redondeo
    t3: list[Transaction] = [
        {"id": 5, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 6, "amount": 50.55, "status": "approved", "category": "services"},
        {"id": 7, "amount": 0.0, "status": "approved", "category": "other"},
    ]
    # Total esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> round -> 135.5
    assert process_transactions(t3) == 135.5, f"Falló Caso 3: {process_transactions(t3)}"

    print("¡Todos los casos de prueba pasaron correctamente!")