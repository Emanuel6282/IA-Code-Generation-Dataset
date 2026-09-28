# Python 3.14 - Paradigma Iterativo Estricto
# Incorpora Structural Pattern Matching (PEP 634), tipado nativo (PEP 585) 
# y sintaxis refinada de Python 3.14 con evaluación de anotaciones deferred/lazy.

def is_balanced(s: str) -> bool:
    """
    Verifica si los paréntesis, corchetes y llaves de una cadena están correctamente
    balanceados utilizando una pila mutable (list) y control de flujo iterativo con `match-case`.
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
                # Ignorar o fallar si se reciben caracteres fuera del conjunto esperado
                return False

    return len(stack) == 0


# --- Casos de prueba usando assert (Python 3.14 - Iterativo) ---

if __name__ == "__main__":
    # Caso 1: Ejemplos base provistos en el enunciado
    assert is_balanced("()") is True, "Falló ()"
    assert is_balanced("([)]") is False, "Falló ([)]"
    assert is_balanced("{[]}") is True, "Falló {[]}"

    # Caso 2: Cadena vacía y combinaciones múltiples válidas
    assert is_balanced("") is True, "Falló cadena vacía"
    assert is_balanced("()[]{}") is True, "Falló ()[]{}"

    # Caso 3: Desbalance por símbolos de apertura o cierre sobrantes
    assert is_balanced("((()") is False, "Falló ((()"
    assert is_balanced("]") is False, "Falló ]"

    print("¡Todos los casos de prueba pasaron correctamente!")