# P05: Transaction Pipeline — Python 3.14
# Paradigma: Estrictamente funcional
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[dict[str, int | float | str]]
#    Utiliza genéricos integrados y tipos unión.
#
# 2. Evaluación diferida de anotaciones (Python 3.14):
#    PEP 649 y PEP 749 introducen un nuevo mecanismo
#    para evaluar anotaciones cuando se necesitan.
#    No requiere sintaxis especial en esta función.
#
# 3. filter():
#    Selecciona transacciones aprobadas con amount > 0.
#
# 4. map():
#    Aplica una comisión del 10 % a cada importe válido.
#
# 5. functools.reduce():
#    Suma los importes netos mediante una función pura.
#
# 6. Inmutabilidad:
#    No se modifican las transacciones originales.
#    No se utilizan variables acumuladoras mutables.
#
# 7. Evaluación perezosa:
#    filter() y map() producen iteradores, evitando
#    crear listas intermedias.
#
# Compatible con Python 3.14.
# Biblioteca estándar únicamente.
# Sin bucles for/while, recursión ni estado mutable.

from functools import reduce


def process_transactions(
    transactions: list[dict[str, int | float | str]]
) -> float:

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

    # 3. Sumar los importes netos de manera funcional.
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

# Test 2: Varias transacciones válidas y una negativa.
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
    {"id": 2, "amount": 50.0, "status": "approved", "category": "services"},
    {"id": 3, "amount": -20.0, "status": "approved", "category": "refund"}
]) == 135.0

# Test 3: Lista vacía.
assert process_transactions([]) == 0.0

print("All tests passed")