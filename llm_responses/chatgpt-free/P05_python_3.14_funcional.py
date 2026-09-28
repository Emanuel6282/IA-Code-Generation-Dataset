# Transaction Pipeline — Python 3.14

# Paradigma: estrictamente funcional

#

# Características utilizadas:

# - Anotaciones de tipos con la sintaxis integrada de colecciones.

# - filter() para seleccionar las transacciones válidas.

# - map() para aplicar la comisión del 10%.

# - functools.reduce() para calcular la suma.

#

# Nota: Python 3.14 no aporta una característica específica que mejore

# materialmente este algoritmo, por lo que se mantiene una solución

# funcional e idiomática.

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
