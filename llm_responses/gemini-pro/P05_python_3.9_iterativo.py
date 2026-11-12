# CARACTERISTICAS DE PYTHON 3.9 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Type Hinting Nativo (PEP 585): En Python 3.9 ya no es necesario importar 'List' o 'Dict' desde el módulo 'typing'. Se pueden usar las colecciones estándar nativas como 'list' y 'dict' directamente en las firmas de tipo, lo que hace el código más limpio.
# 2. Paradigma Iterativo Puro: Se utiliza un bucle 'for' explícito y una variable acumuladora mutable ('total'). No se utilizan expresiones generadoras, comprensiones de listas, ni funciones de orden superior como map/filter/reduce.

def process_transactions(transactions: list[dict]) -> float:
    # Variable mutable para acumular el estado durante la iteración
    total = 0.0
    
    # Bucle iterativo clásico
    for tx in transactions:
        # Validación de los campos según los criterios requeridos
        status = tx.get("status")
        amount = tx.get("amount", 0.0)
        
        # Filtro: status == "approved" Y amount > 0
        if status == "approved" and amount > 0:
            # Aplicación de la comisión y acumulación del resultado
            total += amount * 0.9
            
    # Retorno con redondeo a 2 decimales
    return round(total, 2)

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "General"},
    {"id": 2, "amount": 200.0, "status": "rejected", "category": "General"}
]) == 90.0

# 2. Casos límite: lista vacía y montos negativos/cero
assert process_transactions([]) == 0.0
assert process_transactions([
    {"id": 3, "amount": -50.0, "status": "approved", "category": "Refund"},
    {"id": 4, "amount": 0.0, "status": "approved", "category": "Test"}
]) == 0.0

# 3. Caso de precisión y acumulación múltiple
# 10.55 * 0.9 = 9.495
# 20.11 * 0.9 = 18.099
# Total acumulado iterativamente = 27.594 -> Redondeo a 2 decimales = 27.59
assert process_transactions([
    {"id": 5, "amount": 10.55, "status": "approved", "category": "Services"},
    {"id": 6, "amount": 20.11, "status": "approved", "category": "Products"}
]) == 27.59

print("Todos los tests iterativos en Python 3.9 pasaron correctamente.")