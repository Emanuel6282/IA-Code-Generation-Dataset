# P05: Transaction Pipeline — Python 3.14
# Paradigma: Estrictamente iterativo
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[dict[str, int | float | str]]
#    Permiten documentar los tipos de entrada.
#
# 2. Bucle for:
#    Recorre las transacciones de forma iterativa.
#
# 3. Variables mutables:
#    Permiten acumular progresivamente el importe total.
#
# 4. Condicional if:
#    Filtra las transacciones aprobadas con importe positivo.
#
# 5. round():
#    Redondea el resultado final a dos decimales.
#
# CARACTERÍSTICAS DE PYTHON 3.14:
#
# - PEP 649 y PEP 749:
#   Introducen la evaluación diferida de anotaciones.
#   Las anotaciones de esta función son compatibles
#   con ese mecanismo.
#
# - Se conservan las construcciones iterativas estándar,
#   ya que Python 3.14 no requiere nuevas estructuras
#   de control para resolver este problema.
#
# Sin map(), filter(), reduce() ni recursión.
# Biblioteca estándar únicamente.


def process_transactions(
    transactions: list[dict[str, int | float | str]]
) -> float:

    # Inicializar el acumulador mutable.
    total = 0.0

    # Recorrer las transacciones.
    for transaction in transactions:

        # Obtener los datos necesarios.
        status = transaction["status"]
        amount = transaction["amount"]

        # Filtrar transacciones aprobadas con importe positivo.
        if status == "approved" and amount > 0:

            # Aplicar una comisión del 10 %.
            net_amount = amount * 0.9

            # Acumular el importe neto.
            total += net_amount

    # Redondear el total final a dos decimales.
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