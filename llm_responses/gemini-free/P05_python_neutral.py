# P05: Process Transactions — Python 3
from typing import Any, Dict, List


def process_transactions(transactions: List[Dict[str, Any]]) -> float:
    """Filtra transacciones aprobadas y con monto positivo, aplica comisión del 10%

    y retorna la suma total redondeada a 2 decimales.
    """
    if not transactions:
        return 0.0

    total = sum(
        t.get("amount", 0.0) * 0.9
        for t in transactions
        if t.get("status") == "approved" and t.get("amount", 0.0) > 0
    )

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

    # Caso 3: Múltiples transacciones válidas y cálculo de redondeo
    t3 = [
        {"id": 5, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 6, "amount": 50.55, "status": "approved", "category": "services"},
        {"id": 7, "amount": 0.0, "status": "approved", "category": "other"},
    ]
    # Total esperado: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> round -> 135.5
    assert process_transactions(t3) == 135.5, f"Falló Caso 3: {process_transactions(t3)}"

    print("¡Todos los casos de prueba pasaron correctamente!")