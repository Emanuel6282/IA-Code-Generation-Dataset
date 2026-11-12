def process_transactions(transactions):
    # 1. Filtrar transacciones aprobadas con importe positivo.
    valid_transactions = filter(
        lambda transaction:
            transaction["status"] == "approved"
            and transaction["amount"] > 0,
        transactions
    )

    # 2. Aplicar una comisión del 10 %.
    net_amounts = map(
        lambda transaction: transaction["amount"] * 0.9,
        valid_transactions
    )

    # 3. Sumar y redondear a dos decimales.
    return round(sum(net_amounts), 2)


# Caso 1: Una transacción aprobada y otra rechazada.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 200.0, "status": "rejected", "category": "sales"}
]) == 90.0

# Caso 2: Varias transacciones válidas y una con importe negativo.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 50.0, "status": "approved", "category": "services"},
    {"id": 3, "amount": -20.0, "status": "approved", "category": "refund"}
]) == 135.0

# Caso 3: Lista vacía.
assert process_transactions([]) == 0.0

print("All tests passed")