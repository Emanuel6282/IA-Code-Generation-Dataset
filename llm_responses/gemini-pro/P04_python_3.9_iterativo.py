def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada y cerrada en orden.
    
    Implementación estrictamente iterativa: basada en el uso de un 
    bucle 'for' y una pila (stack) mutable.
    """
    
    # CARACTERÍSTICA DE PYTHON 3.9 (PEP 585): 
    # Uso de colecciones estándar nativas para Type Hinting (list[str], dict[str, str])
    # Ya no es necesario importar 'List' o 'Dict' desde el módulo 'typing'.
    stack: list[str] = []
    
    matching_bracket: dict[str, str] = {')': '(', ']': '[', '}': '{'}
    
    for char in s:
        if char in matching_bracket:
            # Si es un caracter de cierre, verificamos el tope de la pila.
            # Si la pila está vacía o el tope no coincide, está desbalanceado.
            if not stack:
                return False
            
            top_element = stack.pop()
            if matching_bracket[char] != top_element:
                return False
        else:
            # Si es un caracter de apertura, mutamos el estado apilándolo
            stack.append(char)
            
    # Si al final de la iteración la pila quedó vacía, la cadena está balanceada
    # (Esto también maneja correctamente el caso de la cadena vacía)
    return len(stack) == 0

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: un solo par balanceado
assert is_balanced("()") is True, "Error: '()' debería retornar True"

# 2. Caso de anidamiento cruzado/incorrecto
assert is_balanced("([)]") is False, "Error: '([)]' debería retornar False por cierre desordenado"

# 3. Caso de anidamiento profundo balanceado
assert is_balanced("{[]}") is True, "Error: '{[]}' debería retornar True"

print("Todos los tests iterativos en Python 3.9 pasaron correctamente.")