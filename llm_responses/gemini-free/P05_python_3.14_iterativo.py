# Python 3.14 - Paradigma Iterativo Estricto
# Utiliza `typing.TypedDict` con `NotRequired` (PEP 655) para anotación de estructuras
# y Pattern Matching (`match-case`) introducido/optimizado en Python moderno dentro de un
# bucle imperativo `for` con acumulación mutable.

from typing import NotRequired, TypedDict


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: NotRequired[str]


def process_transactions(transactions: list[Transaction]) -> float:
    """Procesa una lista de transacciones usando un bucle iterativo en Python 3.14.

    Combina la iteración explícita con desestructuración mediante Pattern Matching (`match-case`)
    para filtrar elementos válidos y acumular el estado de manera imperativa.
    """
    if not transactions:
        return 0.0

    total_accumulated: float = 0.0

    for tx in transactions:
        # Pattern matching estructural en bucle iterativo
        match tx:
            case {"status": "approved", "amount": float(amount)} if amount > 0:
                discounted_amount: float = amount * 0.9
                total_accumulated += discounted_amount
            case {"status": "approved", "amount": int(amount)} if amount > 0:
                discounted_amount: float = float(amount) * 0.9
                total_accumulated += discounted_amount
            case _:
                # Transacciones rechazadas, con monto <= 0 o no coincidentes
                pass

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

    # Caso 3: Múltiples transacciones válidas y cálculo con decimales
    t3: list[Transaction] = [
        {"id": 5, "amount": 100.0, "status": "approved", "category": "tech"},
        {"id": 6, "amount": 50.55, "status": "approved", "category": "services"},
        {"id": 7, "amount": 0.0, "status": "approved", "category": "other"},
    ]
    assert process_transactions(t3) == 135.5, f"Falló Caso 3: {process_transactions(t3)}"

    print("¡Todos los casos de prueba pasaron correctamente!")