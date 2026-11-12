from decimal import ROUND_HALF_UP, Decimal

COMMISSION_FACTOR = Decimal("0.9")   # se descuenta una comisión del 10 %
CENT = Decimal("0.01")


def process_transactions(transactions):
    """Suma los montos de las transacciones aprobadas con monto positivo, tras
    descontar una comisión del 10 %, y retorna el total redondeado a 2 decimales.

    Los cálculos se hacen con Decimal para evitar los errores de representación de
    los float (por ejemplo, 0.1 + 0.2 != 0.3). El resultado se devuelve como float.
    """
    total = Decimal("0")
    for t in transactions:
        if t["status"] == "approved" and t["amount"] > 0:
            # str() conserva el valor decimal tal como se escribió (100.1 -> "100.1"),
            # en lugar de la aproximación binaria exacta del float.
            total += Decimal(str(t["amount"])) * COMMISSION_FACTOR
    # Redondeo comercial: los .5 se redondean hacia arriba (0.045 -> 0.05).
    return float(total.quantize(CENT, rounding=ROUND_HALF_UP))


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado (solo la primera está aprobada)
    assert process_transactions([
        {"id": 1, "amount": 100.0, "status": "approved", "category": "food"},
        {"id": 2, "amount": 200.0, "status": "rejected", "category": "travel"},
    ]) == 90.0

    # Caso 2: lista vacía y lista sin transacciones válidas (rechazada, cero, negativa)
    assert process_transactions([]) == 0.0
    assert process_transactions([
        {"id": 1, "amount": 50.0, "status": "rejected", "category": "food"},
        {"id": 2, "amount": 0.0, "status": "approved", "category": "food"},
        {"id": 3, "amount": -30.0, "status": "approved", "category": "refund"},
    ]) == 0.0

    # Caso 3: decimales y redondeo. 0.95 * 0.9 = 0.855 exacto, que debe redondear a 0.86;
    # con float, 0.95 * 0.9 se muestra como 0.855 pero internamente es un poco menor,
    # así que round(0.95 * 0.9, 2) devuelve 0.85.
    assert process_transactions([
        {"id": 1, "amount": 0.95, "status": "approved", "category": "fees"},
    ]) == 0.86
    assert process_transactions([
        {"id": 1, "amount": 0.3, "status": "approved", "category": "food"},
        {"id": 2, "amount": 0.2, "status": "approved", "category": "food"},
        {"id": 3, "amount": 99.99, "status": "rejected", "category": "food"},
    ]) == 0.45

    print("Todas las pruebas pasaron.")