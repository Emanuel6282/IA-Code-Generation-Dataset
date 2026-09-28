# Python 3.9 - Paradigma Funcional Estricto
# Utiliza tipos built-in nativos de Python 3.9 (PEP 585: list[dict[str, Any]]),
# junto con `filter()`, `map()` y `functools.reduce()` sin estados mutables ni bucles for/while.

from functools import reduce
from typing import Any


def process_transactions(transactions: list[dict[str, Any]]) -> float:
    """Procesa una lista de transacciones financieras mediante un pipeline funcional.

    Aplica filtrado con filter(), transformación con map() y acumulación
    inmutable con reduce().
    """
    if not transactions:
        return 0.0

    # 1. Predicado puro para filtrado: status == "approved" Y amount > 0
    is_valid_tx = lambda tx: tx.get("status") == "approved" and tx.get("amount", 0.0) > 0

    # 2. Función de transformación pura: aplica comisión del 10% (amount * 0.9)
    apply_commission = lambda tx: tx.get("amount", 0.0) * 0.9

    # Pipeline funcional: filter() -> map()
    filtered_txs = filter(is_valid_tx, transactions)
    discounted_amounts = map(apply_commission, filtered_txs)

    # 3. Acumulación inmutable mediante reduce() con valor inicial 0.0
    total = reduce(lambda acc, amount: acc + amount, discounted_amounts, 0.0)

    return round(total, 2)


# --- Casos de prueba usando assert ---
if __name__ == "__main__":
    # Caso 1: Ejemplo base del enunciado (transacción aprobada vs rechazada)
    t1 = [
        {"id": 1, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 2, "amount": 200.0, "status": "rejected", "category": "food"},
    ]
    assert process_transactions(t1) == 90.0, f"Falló Caso 1: {process_transactions(t1)}"

    # Caso 2: Lista vacía o sin transacciones válidas
    assert process_transactions([]) == 0.0, "Falló Caso 2: Lista vacía"
    t2 = [
        {"id": 3, "amount": -50.0, "status": "approved", "category": "tech"},
        {"id": 4, "amount": 150.0, "status": "rejected", "category": "books"},
    ]
    assert process_transactions(t2) == 0.0, "Falló Caso 2: Sin transacciones válidas"

    # Caso 3: Múltiples transacciones válidas y redondeo a 2 decimales
    t3 = [
        {"id": 5, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 6, "amount": 50.55, "status": "approved", "category": "services"},
        {"id": 7, "amount": 0.0, "status": "approved", "category": "other"},
    ]
    # Total esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> round -> 135.5
    assert process_transactions(t3) == 135.5, f"Falló Caso 3: {process_transactions(t3)}"

    print("¡Todos los casos de prueba pasaron correctamente!")