# P05: Transaction Pipeline — Python 3.9
# Paradigma: Estrictamente funcional
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. list[dict] (PEP 585, Python 3.9):
#    Permite utilizar tipos genéricos integrados.
#
# 2. filter():
#    Selecciona transacciones aprobadas con importe positivo.
#
# 3. map():
#    Transforma cada importe aplicando una comisión del 10 %.
#
# 4. functools.reduce():
#    Acumula los importes netos mediante una función pura.
#
# 5. Funciones lambda:
#    Expresan operaciones sin modificar datos existentes.
#
# 6. Inmutabilidad:
#    No se modifican los diccionarios de entrada ni
#    se utilizan variables acumuladoras mutables.
#
# Compatible con Python 3.9.
# Biblioteca estándar únicamente.
# Sin bucles for/while ni estado mutable.

from functools import reduce


def process_transactions(transactions: list[dict]) -> float:
    # 1. Filtrar transacciones aprobadas con importe positivo.
    valid_transactions = filter(
        lambda transaction: (
            transaction["status"] == "approved"
            and transaction["amount"] > 0
        ),
        transactions
    )

    # 2. Aplicar una comisión del 10 %.
    net_amounts = map(
        lambda transaction: transaction["amount"] * 0.9,
        valid_transactions
    )

    # 3. Acumular los importes netos funcionalmente.
    total = reduce(
        lambda accumulated, amount: accumulated + amount,
        net_amounts,
        0.0
    )

    # 4. Redondear el resultado final a dos decimales.
    return round(total, 2)


# Test 1: Una transacción aprobada y otra rechazada.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 200.0, "status": "rejected", "category": "sales"}
]) == 90.0

# Test 2: Varias transacciones válidas y una con importe negativo.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 50.0, "status": "approved", "category": "services"},
    {"id": 3, "amount": -20.0, "status": "approved", "category": "refund"}
]) == 135.0

# Test 3: Lista vacía.
assert process_transactions([]) == 0.0

print("All tests passed")