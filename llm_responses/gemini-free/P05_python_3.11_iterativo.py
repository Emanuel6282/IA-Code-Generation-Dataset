# Python 3.11 - Paradigma Iterativo Estricto
# Utiliza `typing.TypedDict` de Python 3.11 con `NotRequired` (PEP 655)
# para anotaciones de tipo estructuradas y precisas, junto con un bucle `for`
# imperativo para el filtrado y acumulación con estado mutable.

from typing import NotRequired, TypedDict


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: NotRequired[str]


def process_transactions(transactions: list[Transaction]) -> float:
    """Procesa una lista de transacciones usando un bucle iterativo en Python 3.11.

    Filtra las transacciones con status 'approved' y monto > 0, aplica una
    comisión del 10% y retorna la suma redondeada a 2 decimales.
    """
    if not transactions:
        return 0.0

    total_accumulated: float = 0.0

    for tx in transactions:
        try:
            status: str = tx.get("status", "")
            amount: float = float(tx.get("amount", 0.0))

            if status == "approved" and amount > 0:
                discounted_amount: float = amount * 0.9
                total_accumulated += discounted_amount
        except (ValueError, TypeError) as e:
            # En Python 3.11 se pueden añadir notas explicativas a las excepciones
            e.add_note(f"Error procesando la transacción con ID: {tx.get('id')}")
            raise

    return round(total_accumulated, 2)


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
    assert process_transactions(t3) == 135.5, f"Falló Caso 3: {process_transactions(t3)}"

    print("¡Todos los casos de prueba pasaron correctamente!")