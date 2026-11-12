def process_transactions(transactions: list[dict]) -> float:
    """
    Procesa una lista de transacciones filtrando las aprobadas y con monto positivo,
    aplica una comisión del 10% y retorna la suma total redondeada a 2 decimales.
    """
    # Utilizamos una expresión generadora dentro de sum() para un enfoque
    # idiomático y eficiente en memoria (paradigma funcional/declarativo en Python).
    total = sum(
        tx["amount"] * 0.9
        for tx in transactions
        if tx.get("status") == "approved" and tx.get("amount", 0.0) > 0
    )
    
    # round() maneja el caso de 0.0 naturalmente si la lista está vacía o filtrada
    return round(total, 2)

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "General"},
    {"id": 2, "amount": 200.0, "status": "rejected", "category": "General"}
]) == 90.0

# 2. Casos límite: lista vacía y montos negativos o nulos
assert process_transactions([]) == 0.0
assert process_transactions([
    {"id": 3, "amount": -50.0, "status": "approved", "category": "Refund"},
    {"id": 4, "amount": 0.0, "status": "approved", "category": "Test"}
]) == 0.0

# 3. Caso de precisión y suma múltiple: verificar el redondeo a 2 decimales
# 10.55 * 0.9 = 9.495
# 20.11 * 0.9 = 18.099
# Suma = 27.594 -> Redondeo = 27.59
assert process_transactions([
    {"id": 5, "amount": 10.55, "status": "approved", "category": "Services"},
    {"id": 6, "amount": 20.11, "status": "approved", "category": "Products"}
]) == 27.59

print("Todos los casos de prueba pasaron correctamente.")