# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Optimizaciones JIT (Tier 2 Optimizer) para Lambdas: En Python 3.14, la mejora continua del compilador Just-In-Time reduce drásticamente el "overhead" (costo de ejecución) de las llamadas a funciones pequeñas. Esto hace que el uso intensivo de lambdas dentro de funciones de orden superior como map(), filter() y reduce() sea mucho más eficiente que en versiones anteriores, equiparando el rendimiento del paradigma funcional puro con el iterativo.
# 2. Evaluación Diferida de Anotaciones (PEP 649/749): Las anotaciones de tipo como `list[dict]` ahora se procesan de manera más limpia y eficiente bajo el capó, mejorando el tiempo de carga del módulo sin requerir importaciones especiales.
# 3. Paradigma Funcional Puro: Se construye un pipeline (tubería) de datos perezoso (lazy) e inmutable. No hay bucles for/while ni variables mutables de acumulación. El estado viaja a través de transformaciones puras.

from functools import reduce

def process_transactions(transactions: list[dict]) -> float:
    # 1. Filter: Generador perezoso que evalúa la inmutabilidad de los diccionarios,
    # reteniendo solo las transacciones aprobadas y con montos estrictamente positivos.
    valid_txs = filter(
        lambda tx: tx.get("status") == "approved" and tx.get("amount", 0.0) > 0,
        transactions
    )
    
    # 2. Map: Extrae y transforma proyectando el nuevo valor (aplicando la comisión del 10%).
    # Multiplicar por 0.9 es matemáticamente equivalente a deducir el 10%.
    commissions = map(
        lambda tx: tx.get("amount", 0.0) * 0.9,
        valid_txs
    )
    
    # 3. Reduce: Pliega la secuencia de valores proyectados en un único resultado final.
    # El inicializador 0.0 es crucial en la programación funcional pura para garantizar
    # un retorno seguro (identidad) en caso de que la colección original esté vacía
    # o ningún elemento haya sobrevivido al filtro previo.
    total = reduce(
        lambda acc, val: acc + val,
        commissions,
        0.0
    )
    
    # Retorno final aplicando la pureza referencial y el redondeo requerido
    return round(total, 2)


# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: filtrado correcto (descarta rejected) y aplicación de comisión
assert process_transactions([
    {"id": 1, "amount": 100.0, "status": "approved", "category": "General"},
    {"id": 2, "amount": 200.0, "status": "rejected", "category": "General"}
]) == 90.0

# 2. Casos límite: lista vacía y transacciones con montos negativos/nulos
assert process_transactions([]) == 0.0
assert process_transactions([
    {"id": 3, "amount": -50.0, "status": "approved", "category": "Refund"},
    {"id": 4, "amount": 0.0, "status": "approved", "category": "Test"}
]) == 0.0

# 3. Caso de precisión matemática y acumulación múltiple
# 10.55 * 0.9 = 9.495
# 20.11 * 0.9 = 18.099
# Total acumulado por reduce = 27.594 -> Redondeo final a 2 decimales = 27.59
assert process_transactions([
    {"id": 5, "amount": 10.55, "status": "approved", "category": "Services"},
    {"id": 6, "amount": 20.11, "status": "approved", "category": "Products"}
]) == 27.59

print("Todos los tests funcionales en Python 3.14 pasaron correctamente.")