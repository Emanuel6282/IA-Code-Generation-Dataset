# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Especialización Adaptativa (PEP 659): Python 3.11 introdujo un intérprete mucho más rápido. En el paradigma funcional, donde delegamos el trabajo iterativo a funciones de orden superior (map/filter/reduce) pasándoles lambdas repetitivamente, esta optimización reduce drásticamente el costo de las llamadas a estas pequeñas funciones (function call overhead), haciendo el enfoque funcional competitivo en rendimiento.
# 2. Type Hinting Moderno: Uso directo de `list[dict]` de manera nativa.
# 3. Paradigma Funcional Estricto: Ausencia total de sentencias `for`, `while` o reasignación de variables. Se ensambla un pipeline de procesamiento perezoso (lazy) puramente declarativo.

from functools import reduce

def process_transactions(transactions: list[dict]) -> float:
    # 1. Filter: Retiene transacciones estrictamente aprobadas y con monto positivo
    valid_txs = filter(
        lambda tx: tx.get("status") == "approved" and tx.get("amount", 0.0) > 0,
        transactions
    )
    
    # 2. Map: Extrae el monto de cada transacción válida y aplica la deducción (multiplica por 0.9)
    commissions = map(
        lambda tx: tx.get("amount", 0.0) * 0.9,
        valid_txs
    )
    
    # 3. Reduce: Pliega el iterador acumulando los valores.
    # El tercer parámetro (0.0) asegura un retorno correcto si la lista está vacía o si ningún elemento pasó el filtro.
    total = reduce(
        lambda acc, val: acc + val,
        commissions,
        0.0
    )
    
    # Retorna el resultado inmutable, redondeado a 2 decimales
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
# Total acumulado = 27.594 -> Redondeo a 2 decimales = 27.59
assert process_transactions([
    {"id": 5, "amount": 10.55, "status": "approved", "category": "Services"},
    {"id": 6, "amount": 20.11, "status": "approved", "category": "Products"}
]) == 27.59

print("Todos los tests funcionales en Python 3.11 pasaron correctamente.")