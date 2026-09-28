# Python 3.9 - Paradigma Iterativo Estricto
# Utiliza tipos built-in nativos para annotating (PEP 585: list[dict[str, Any]])
# y procesamiento imperativo basado en bucles `for` con variables mutables acumuladoras.

from typing import Any


def process_transactions(transactions: list[dict[str, Any]]) -> float:
    """Procesa una lista de transacciones financieras utilizando un bucle iterativo.

    Filtra las transacciones con estado 'approved' y monto positivo, aplica
    una comisión del 10% y acumula el total redondeado a 2 decimales.
    """
    if not transactions:
        return 0.0

    total_accumulated: float = 0.0

    for tx in transactions:
        status: str = tx.get("status", "")
        amount: float = tx.get("amount", 0.0)

        # Condición de filtrado imperativa
        if status == "approved" and amount > 0:
            # Aplicación de comisión del 10% y acumulación mutable
            discounted_amount: float = amount * 0.9
            total_accumulated += discounted_amount

    return round(total_accumulated, 2)


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

    # Caso 3: Múltiples transacciones válidas y cálculo con decimales
    t3 = [
        {"id": 5, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 6, "amount": 50.55, "status": "approved", "category": "services"},
        {"id": 7, "amount": 0.0, "status": "approved", "category": "other"},
    ]
    assert process_transactions(t3) == 135.5, f"Falló Caso 3: {process_transactions(t3)}"

    print("¡Todos los casos de prueba pasaron correctamente!")