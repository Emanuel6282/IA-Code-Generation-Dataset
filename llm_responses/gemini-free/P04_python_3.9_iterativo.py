# Python 3.9 - Paradigma Iterativo Estricto
# Hace uso de anotaciones de tipo mejoradas de Python 3.9 (PEP 585) utilizando list y dict
# directamente sin necesidad de importar desde el módulo `typing`.

def is_balanced(s: str) -> bool:
    """
    Verifica si los paréntesis/corchetes/llaves están balanceados en orden
    usando una pila mutable e iteración con bucle `for`.
    """
    stack: list[str] = []
    
    # Operador de unión de diccionarios | (PEP 584 - Python 3.9)
    brackets: dict[str, str] = {')': '(', ']': '['} | {'}': '{'}

    for char in s:
        if char in brackets:
            if not stack or stack.pop() != brackets[char]:
                return False
        else:
            stack.append(char)

    return len(stack) == 0


# --- Casos de prueba usando assert (Python 3.9 - Iterativo) ---

if __name__ == "__main__":
    # Caso 1: Ejemplos base del enunciado
    assert is_balanced("()") is True, "Falló ()"
    assert is_balanced("([)]") is False, "Falló ([)]"
    assert is_balanced("{[]}") is True, "Falló {[]}"

    # Caso 2: Cadena vacía y secuencias compuestas válidas
    assert is_balanced("") is True, "Falló cadena vacía"
    assert is_balanced("()[]{}") is True, "Falló ()[]{}"

    # Caso 3: Desbalance por cierres prematuros o símbolos no cerrados
    assert is_balanced("((()") is False, "Falló ((()"
    assert is_balanced("]") is False, "Falló ]"

    print("¡Todos los casos de prueba pasaron correctamente!")