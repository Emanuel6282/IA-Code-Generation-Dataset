# P05: Transaction Pipeline — Python 3.11
# Paradigma: Estrictamente iterativo
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Tipado moderno con list[dict[str, ...]]:
#    Permite describir las colecciones usando tipos integrados.
#    Disponible desde Python 3.9 y compatible con Python 3.11.
#
# 2. Bucle for:
#    Recorre las transacciones de forma iterativa.
#
# 3. Variables mutables:
#    Permiten acumular progresivamente el importe total.
#
# 4. Condicional if:
#    Filtra transacciones aprobadas con importe positivo.
#
# 5. round():
#    Redondea el total final a dos decimales.
#
# NOTA SOBRE PYTHON 3.11:
# Python 3.11 incorpora optimizaciones de ejecución,
# incluido un intérprete adaptativo especializado (PEP 659).
# Este algoritmo puede beneficiarse de esas optimizaciones
# sin necesidad de cambiar su lógica.
#
# Sin map(), filter(), reduce() ni recursión.
# Biblioteca estándar únicamente.


def process_transactions(
    transactions: list[dict[str, int | float | str]]
) -> float:

    # Acumulador mutable.
    total = 0.0

    # Recorrer todas las transacciones.
    for transaction in transactions:

        # Extraer los campos necesarios.
        status = transaction["status"]
        amount = transaction["amount"]

        # Filtrar transacciones válidas.
        if status == "approved" and amount > 0:

            # Aplicar una comisión del 10 %.
            net_amount = amount * 0.9

            # Acumular el importe neto.
            total += net_amount

    # Redondear únicamente el resultado final.
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