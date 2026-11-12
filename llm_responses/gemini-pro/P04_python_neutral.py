def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada.
    """
    stack = []
    # Diccionario para mapear los caracteres de cierre con sus respectivos de apertura
    matching_bracket = {')': '(', ']': '[', '}': '{'}
    
    for char in s:
        # Si es un caracter de cierre
        if char in matching_bracket:
            # Extraemos el último elemento de la pila (o asignamos un valor dummy si está vacía)
            top_element = stack.pop() if stack else '#'
            
            # Si el elemento extraído no coincide con el de apertura esperado, no está balanceado
            if matching_bracket[char] != top_element:
                return False
        else:
            # Si es un caracter de apertura, lo apilamos
            stack.append(char)
            
    # Si la pila queda vacía, todos los caracteres se cerraron correctamente
    return not stack

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base simple balanceado
assert is_balanced("()") == True, "Error: '()' debería retornar True"

# 2. Caso de anidamiento incorrecto
assert is_balanced("([)]") == False, "Error: '([)]' debería retornar False por cierre desordenado"

# 3. Caso de anidamiento profundo balanceado
assert is_balanced("{[]}") == True, "Error: '{[]}' debería retornar True"

# Caso adicional contemplado en los requisitos
assert is_balanced("") == True, "Error: Una cadena vacía debería retornar True"

print("Todos los casos de prueba pasaron correctamente.")