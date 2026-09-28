def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena con caracteres '()[]{}' está correctamente balanceada.
    Utiliza una pila (stack) para realizar el seguimiento en orden LIFO.
    """
    stack = []
    # Mapeo de caracteres de cierre a sus correspondientes de apertura
    matching_bracket = {')': '(', ']': '[', '}': '{'}

    for char in s:
        if char in matching_bracket:
            # Si es un carácter de cierre, verificar si coincide con el tope de la pila
            top_element = stack.pop() if stack else '#'
            if matching_bracket[char] != top_element:
                return False
        else:
            # Si es un carácter de apertura, agregarlo a la pila
            stack.append(char)

    # La cadena es válida si la pila queda completamente vacía
    return not stack


# --- Casos de prueba usando assert ---

if __name__ == "__main__":
    # Caso 1: Casos del problema y anidamiento válido
    assert is_balanced("()") == True, "Falló ()"
    assert is_balanced("([)]") == False, "Falló ([)]"
    assert is_balanced("{[]}") == True, "Falló {[]}"

    # Caso 2: Cadena vacía y secuencias válidas simples
    assert is_balanced("") == True, "Falló cadena vacía"
    assert is_balanced("()[]{}") == True, "Falló ()[]{}"

    # Caso 3: Desbalance por aperturas/cierres adicionales
    assert is_balanced("((()") == False, "Falló ((()"
    assert is_balanced("())") == False, "Falló ())"

    print("¡Todos los casos de prueba pasaron correctamente!")