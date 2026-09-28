# Transaction Pipeline — Python 3.9

# Paradigma: estrictamente iterativo

# No se utilizan map(), filter(), reduce() ni recursión.

def process_transactions(transactions):
    total = 0.0

    for transaction in transactions:
        if transaction["status"] == "approved" and transaction["amount"] > 0:
            total += transaction["amount"] * 0.9

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
