# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Optimizaciones JIT (Tier 2 Optimizer) y PEP 744: En Python 3.14, el compilador Just-In-Time madura sustancialmente. Esto significa que los bucles iterativos puros (`for`/`while`) con variables locales mutables ahora se compilan a código de máquina mucho más eficiente, reduciendo o eliminando la histórica ventaja de rendimiento de delegar iteraciones a funciones en C como `map` o `filter`.
# 2. Structural Pattern Matching: Uso de `match/case` introducido en versiones previas pero altamente optimizado en el bytecode de 3.14. Permite la desestructuración segura de diccionarios y el uso de guardas (`if amount > 0`) en un solo paso semántico.
# 3. Anotaciones Diferidas (PEP 649/749): Las anotaciones como `list[dict]` se manejan de forma nativa y diferida, mejorando el tiempo de carga y la introspección.
# 4. Paradigma Iterativo Estricto: Manejo manual del estado con la variable mutable `total`, sin recurrir a funciones de orden superior, cierres (closures) ni recursión.

def process_transactions(transactions: list[dict]) -> float:
    # Estado mutable acumulador
    total = 0.0
    
    for tx in transactions:
        # Desestructuración nativa del diccionario de la transacción
        match tx:
            # El caso coincide solo si el status es "approved" y existe "amount".
            # La guarda 'if' filtra los montos negativos o cero en la misma instrucción.
            case {"status": "approved", "amount": amount} if amount > 0:
                total += amount * 0.9
                
            # Si no cumple el patrón o la guarda, la iteración continúa silenciosamente
            case _:
                pass
                
    # Retorno aplicando el redondeo requerido a 2 decimales
    return round(total, 2)

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: filtrado correcto y aplicación de comisión
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
# Total acumulado = 27.594 -> Redondeo a 2 decimales = 27.59
assert process_transactions([
    {"id": 5, "amount": 10.55, "status": "approved", "category": "Services"},
    {"id": 6, "amount": 20.11, "status": "approved", "category": "Products"}
]) == 27.59

print("Todos los tests iterativos en Python 3.14 pasaron correctamente.")