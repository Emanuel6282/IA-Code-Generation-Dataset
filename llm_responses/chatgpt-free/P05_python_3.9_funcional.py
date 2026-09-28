# Transaction Pipeline — Python 3.9

# Paradigma: estrictamente funcional

# Se utilizan filter(), map() y functools.reduce().

# No se utilizan for/while ni estado mutable.

from functools import reduce

def process_transactions(transactions):
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
