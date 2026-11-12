# CARACTERISTICAS DE PYTHON 3.14 UTILIZADAS Y POR QUE:
# 1. Optimizador JIT de Nivel 2 (PEP 744): En Python 3.14, el compilador Just-In-Time alcanza una madurez que optimiza agresivamente las expresiones generadoras combinadas con funciones integradas en C (como `sum()`). Esto permite escribir código declarativo altamente legible sin sacrificar el rendimiento frente a un bucle `for` tradicional.
# 2. Type Hinting Diferido (PEP 649/749): Uso de la sintaxis nativa `list[dict]` sin importar el módulo `typing`. En 3.14, estas anotaciones se evalúan de manera diferida, lo que reduce el tiempo de inicio del módulo y el consumo de memoria.
# 3. Expresión Generadora Inline: Reemplaza el bloque imperativo de inicialización y suma, creando un "pipeline" de datos fluido y directo, ideal para este tipo de transformaciones financieras.

def process_transactions(transactions: list[dict]) -> float:
    # El generador evalúa las condiciones de forma perezosa y sum() compila a C
    total = sum(
        t["amount"] * 0.9
        for t in transactions
        if t.get("status") == "approved" and t.get("amount", 0.0) > 0
    )
    
    return round(total, 2)

# ==========================================
# CASOS DE PRUEBA
# ==========================================
assert process_transactions([]) == 0.0
assert process_transactions([{"id": 1, "amount": 1000.0, "status": "approved", "category": "x"}]) == 900.0
assert process_transactions([{"id": 1, "amount": 100.0, "status": "rejected", "category": "x"}]) == 0.0