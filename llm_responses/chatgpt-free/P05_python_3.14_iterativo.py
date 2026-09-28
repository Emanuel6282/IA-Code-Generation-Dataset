# Transaction Pipeline — Python 3.14

# Paradigma: estrictamente iterativo

#

# Características utilizadas:

# - Anotaciones de tipos con sintaxis de colecciones integrada.

# - Bucle for y acumulador mutable para procesar las transacciones.

#

# Nota: Python 3.14 no aporta una característica específica que mejore

# materialmente este algoritmo, por lo que se mantiene una implementación

# iterativa simple e idiomática.

def process_transactions(transactions: list[dict]) -> float:
    total = 0.0

    for transaction in transactions:
        if (
            transaction["status"] == "approved"
            and transaction["amount"] > 0
        ):
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
