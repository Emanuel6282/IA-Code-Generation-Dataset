# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Structural Pattern Matching (match/case): Introducido en 3.10 y altamente optimizado en el compilador de Python 3.11 (con saltos de tabla más rápidos). Permite validar la estructura del diccionario y extraer sus valores ('status', 'amount') simultáneamente de forma segura, eliminando la necesidad de encadenar múltiples llamadas a `.get()` o validaciones de claves nulas.
# 2. Guardas (Guards): Se utiliza `if amount > 0` directamente en la declaración del `case`. Esto permite aplicar lógica condicional sobre los valores extraídos antes de confirmar que el caso coincide.
# 3. Paradigma Iterativo Estricto: Acumulación de estado mediante una variable mutable (`total`) operada dentro de un clásico bucle `for`, sin hacer uso de funciones de orden superior, comprensiones ni recursión.

def process_transactions(transactions: list[dict]) -> float:
    # Estado mutable acumulador
    total = 0.0
    
    for tx in transactions:
        # Enrutamiento y extracción estructural nativa (Pattern Matching)
        match tx:
            # Coincide solo si existe la llave "status" con valor "approved", 
            # existe la llave "amount" vinculando su valor a la variable local 'amount', 
            # y finalmente pasa la guarda iterativa (amount > 0).
            case {"status": "approved", "amount": amount} if amount > 0:
                total += amount * 0.9
            
            # Cualquier otra estructura, monto negativo/cero o status distinto es ignorado
            case _:
                pass
                
    # Retorno final aplicando la regla de redondeo
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

print("Todos los tests iterativos en Python 3.11 pasaron correctamente.")