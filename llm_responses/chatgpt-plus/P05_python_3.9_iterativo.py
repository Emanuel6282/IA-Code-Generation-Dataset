# P05: Transaction Pipeline — Python 3.9
# Paradigma: Estrictamente iterativo
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos con list[dict] (PEP 585):
#    Introducidas en Python 3.9, permiten utilizar
#    tipos genéricos integrados sin importar typing.List.
#
# 2. Bucle for:
#    Recorre las transacciones de forma iterativa.
#
# 3. Variables mutables:
#    Permiten acumular el importe total.
#
# 4. Condicional if:
#    Filtra las transacciones según estado e importe.
#
# 5. round():
#    Redondea el resultado final a dos decimales.
#
# Compatible con Python 3.9.
# Sin map(), filter(), reduce() ni recursión.
# Biblioteca estándar únicamente.


def process_transactions(transactions: list[dict]) -> float:
    # Acumulador mutable del importe neto.
    total = 0.0

    # Recorrer las transacciones de forma iterativa.
    for transaction in transactions:

        # Obtener los campos necesarios.
        status = transaction["status"]
        amount = transaction["amount"]

        # Filtrar transacciones aprobadas y positivas.
        if status == "approved" and amount > 0:

            # Aplicar una comisión del 10 %.
            net_amount = amount * 0.9

            # Acumular el importe resultante.
            total += net_amount

    # Redondear únicamente al finalizar.
    return round(total, 2)


# Test 1: Una transacción aprobada y otra rechazada.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 200.0, "status": "rejected", "category": "sales"}
]) == 90.0

# Test 2: Varias transacciones aprobadas y una negativa.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 50.0, "status": "approved", "category": "services"},
    {"id": 3, "amount": -20.0, "status": "approved", "category": "refund"}
]) == 135.0

# Test 3: Lista vacía.
assert process_transactions([]) == 0.0

print("All tests passed")