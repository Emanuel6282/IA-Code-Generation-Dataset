# P05: Transaction Pipeline — Python 3.11
# Paradigma: Estrictamente funcional
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Tipado moderno:
#    list[dict[str, int | float | str]]
#    Utiliza genéricos integrados (Python 3.9)
#    y tipos unión con | (Python 3.10).
#
# 2. filter():
#    Selecciona las transacciones aprobadas
#    cuyo importe sea mayor que cero.
#
# 3. map():
#    Aplica una comisión del 10 % a cada importe.
#
# 4. functools.reduce():
#    Acumula los importes netos sin estado mutable.
#
# 5. Funciones lambda:
#    Expresan transformaciones funcionales.
#
# 6. Inmutabilidad:
#    No modifica las transacciones originales.
#
# NOVEDAD DE PYTHON 3.11:
# El intérprete adaptativo especializado (PEP 659)
# puede optimizar la ejecución de estas operaciones.
# No requiere sintaxis adicional.
#
# Sin bucles for/while, variables acumuladoras
# mutables ni bibliotecas de terceros.

from functools import reduce


def process_transactions(
    transactions: list[dict[str, int | float | str]]
) -> float:

    # 1. Filtrar las transacciones válidas.
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

    # 3. Sumar los importes netos funcionalmente.
    total = reduce(
        lambda accumulated, amount: accumulated + amount,
        net_amounts,
        0.0
    )

    # 4. Redondear el resultado final.
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