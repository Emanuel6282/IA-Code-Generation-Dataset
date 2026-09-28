# Python 3.11 - Paradigma Funcional Estricto
# Utiliza `typing.TypedDict` con `NotRequired` (PEP 655 - Python 3.11) para un tipado inmutable y preciso,
# combinando `filter()`, `map()` y `functools.reduce()` en un pipeline puramente evaluativo.

from functools import reduce
from typing import NotRequired, TypedDict


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: NotRequired[str]


def process_transactions(transactions: list[Transaction]) -> float:
    """Procesa una lista de transacciones mediante un pipeline puramente funcional.

    Filtra elementos válidos con filter(), aplica el descuento del 10% con map()
    y reduce los montos a un total inmutable con functools.reduce().
    """
    if not transactions:
        return 0.0

    # 1. Predicado de filtrado puro: status == "approved" Y amount > 0
    is_valid: lambda tx: tx.get("status") == "approved" and float(tx.get("amount", 0.0)) > 0
    
    # 2. Transformación pura: aplica la comisión del 10%
    apply_commission = lambda tx: float(tx.get("amount", 0.0)) * 0.9

    # Pipeline de transformación inmutable (filter -> map)
    valid_transactions = filter(
        lambda tx: tx.get("status") == "approved" and float(tx.get("amount", 0.0)) > 0,
        transactions
    )
    discounted_amounts = map(
        lambda tx: float(tx.get("amount", 0.0)) * 0.9,
        valid_transactions
    )

    # 3. Acumulación inmutable con reduce() partiendo del valor 0.0
    total = reduce(lambda acc, amount: acc + amount, discounted_amounts, 0.0)

    return round(total, 2)


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