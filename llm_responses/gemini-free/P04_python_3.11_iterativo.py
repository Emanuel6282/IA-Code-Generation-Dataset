# Python 3.11 - Paradigma Iterativo Estricto
# Aprovecha la sintaxis de Python 3.10+ (Structural Pattern Matching / match-case) 
# y anotaciones de tipos nativas (PEP 585).

def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis está balanceada usando un bucle `for`
    y una pila mutable, estructurada con Pattern Matching (`match-case`).
    """
    stack: list[str] = []

    for char in s:
        match char:
            case "(" | "[" | "{":
                stack.append(char)
            case ")":
                if not stack or stack.pop() != "(":
                    return False
            case "]":
                if not stack or stack.pop() != "[":
                    return False
            case "}":
                if not stack or stack.pop() != "{":
                    return False
            case _:
                # Si se encontrara cualquier otro símbolo no esperado
                return False

    return len(stack) == 0


# --- Casos de prueba usando assert (Python 3.11 - Iterativo) ---

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