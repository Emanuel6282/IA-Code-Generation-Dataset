# CARACTERISTICAS DE PYTHON 3.9 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Type Hinting Nativo (PEP 585): Uso de 'list[dict]' nativo sin necesidad de importar el módulo 'typing'.
# 2. Funciones de Orden Superior (Higher-Order Functions): Uso estricto de 'filter()', 'map()' y 'functools.reduce()' para construir un pipeline de procesamiento perezoso (lazy) e inmutable.
# 3. Funciones Anónimas Puras (Lambdas): Las transformaciones y evaluaciones se realizan sin efectos secundarios, evaluando directamente los diccionarios inmutables.
# 4. Ausencia de Estado Mutable: Se evita por completo el uso de bucles 'for', 'while' y variables acumuladoras. El valor inicial seguro se delega al tercer argumento de 'reduce'.

from functools import reduce

def process_transactions(transactions: list[dict]) -> float:
    # 1. Filter: Retiene solo transacciones aprobadas y con monto positivo
    valid_txs = filter(
        lambda tx: tx.get("status") == "approved" and tx.get("amount", 0.0) > 0, 
        transactions
    )
    
    # 2. Map: Extrae el monto de cada transacción filtrada y aplica el descuento (10% comisión)
    commissions = map(
        lambda tx: tx.get("amount", 0.0) * 0.9, 
        valid_txs
    )
    
    # 3. Reduce: Pliega (fold) el iterador sumando los montos. 
    # El valor inicial 0.0 garantiza que retorne 0.0 si la lista original estaba vacía o filtrada por completo.
    total = reduce(
        lambda acc, val: acc + val, 
        commissions, 
        0.0
    )
    
    # Retornamos el resultado final puro, redondeado a 2 decimales
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
# Total = 27.594 -> Redondeo a 2 decimales = 27.59
assert process_transactions([
    {"id": 5, "amount": 10.55, "status": "approved", "category": "Services"},
    {"id": 6, "amount": 20.11, "status": "approved", "category": "Products"}
]) == 27.59

print("Todos los tests funcionales en Python 3.9 pasaron correctamente.")