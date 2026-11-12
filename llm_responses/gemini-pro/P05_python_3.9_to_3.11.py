# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Structural Pattern Matching (match/case): Introducido en 3.10 y altamente optimizado en el intérprete adaptativo de Python 3.11 (PEP 659). Permite desestructurar el diccionario extrayendo exactamente los campos necesarios de forma declarativa, eliminando múltiples búsquedas de llaves.
# 2. Guardas Integradas (if amount > 0): Se evalúa la condición numérica directamente en la declaración del 'case', fusionando el ruteo lógico y la validación de negocio.
# 3. Type Hinting Nativo (PEP 585): Uso directo de 'list[dict]' sin necesidad de importar clases verbosas desde el módulo 'typing'.

def process_transactions(transactions: list[dict]) -> float:
    total = 0.0
    
    for t in transactions:
        # Desestructuración nativa altamente optimizada en Python 3.11
        match t:
            # Extraemos 'amount' solo si el 'status' coincide y pasa la guarda matemática
            case {"status": "approved", "amount": amount} if amount > 0:
                total += amount * 0.9
            
            case _:
                pass
                
    return round(total, 2)

# ==========================================
# CASOS DE PRUEBA
# ==========================================
assert process_transactions([]) == 0.0
assert process_transactions([{"id":1,"amount":1000.0,"status":"approved","category":"x"}]) == 900.0
assert process_transactions([{"id":1,"amount":100.0,"status":"rejected","category":"x"}]) == 0.0