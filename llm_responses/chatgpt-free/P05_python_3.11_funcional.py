# Transaction Pipeline — Python 3.11

# Paradigma: estrictamente funcional

#

# Características utilizadas:

# - list[dict] y -> float para anotaciones de tipos modernas.

# - filter() para seleccionar transacciones válidas.

# - map() para aplicar la comisión.

# - functools.reduce() para acumular los importes.

#

# Nota: Python 3.11 no introduce una característica específica que mejore

# materialmente este algoritmo, por lo que se utilizan las características

# modernas de tipado disponibles en esta versión sin forzar cambios artificiales.

from functools import reduce

def process_transactions(transactions: list[dict]) -> float:
    valid_transactions = filter(
    lambda transaction: (
    transaction["status"] == "approved"
    and transaction["amount"] > 0
    ),
    transactions
    )

    processed_amounts = map(
        lambda transaction: transaction["amount"] * 0.9,
        valid_transactions
    )

    total = reduce(
        lambda accumulated, amount: accumulated + amount,
        processed_amounts,
        0.0
    )

    return round(total, 2)

# Casos de prueba

assert process_transactions([
{"id": 1, "amount": 100.0, "status": "approved", "category": "food"},
{"id": 2, "amount": 200.0, "status": "rejected", "category": "shopping"}
]) == 90.0

assert process_transactions([]) == 0.0

assert process_transactions([
{"id": 1, "amount": -50.0, "status": "approved", "category": "food"},
{"id": 2, "amount": 100.0, "status": "rejected", "category": "transport"},
{"id": 3, "amount": 50.0, "status": "approved", "category": "shopping"}
]) == 45.0
