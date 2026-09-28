def process_transactions(transactions):
    """
    Procesa una lista de transacciones financieras: filtra las aprobadas
    con monto positivo, aplica una comision del 10%, y retorna la suma
    total resultante redondeada a 2 decimales.

    Pasos:
    1. Filtrar: mantener solo transacciones con status == "approved" Y
       amount > 0.
    2. Aplicar: una comision del 10% (multiplicar cada amount por 0.9).
    3. Retornar: la suma total de los amounts resultantes, redondeada a 2
       decimales.

    Args:
        transactions: Lista de diccionarios, cada uno con las claves "id",
                       "amount", "status" y "category".

    Returns:
        Un float con la suma total de los montos aprobados y positivos,
        despues de aplicar la comision del 10%, redondeado a 2 decimales.
        Si la lista esta vacia o no hay transacciones validas, retorna 0.0.
    """
    total = 0.0

    for transaccion in transactions:
        if transaccion.get("status") == "approved" and transaccion.get("amount", 0) > 0:
            total += transaccion["amount"] * 0.9

    return round(total, 2)


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, solo una transaccion valida
    transacciones1 = [
        {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
        {"id": 2, "amount": 200.0, "status": "rejected", "category": "sales"},
    ]
    assert process_transactions(transacciones1) == 90.0

    # Caso 2: lista vacia -> 0.0
    assert process_transactions([]) == 0.0

    # Caso 3: varias transacciones validas, incluyendo un monto negativo
    # (que debe excluirse) y una rechazada (que tambien debe excluirse)
    transacciones3 = [
        {"id": 1, "amount": 50.0, "status": "approved", "category": "food"},
        {"id": 2, "amount": -20.0, "status": "approved", "category": "food"},
        {"id": 3, "amount": 30.0, "status": "rejected", "category": "food"},
        {"id": 4, "amount": 25.5, "status": "approved", "category": "travel"},
    ]
    # (50.0 * 0.9) + (25.5 * 0.9) = 45.0 + 22.95 = 67.95
    assert process_transactions(transacciones3) == 67.95

    print("Todos los casos de prueba pasaron correctamente.")