# Python 3.9 - Paradigma Funcional Estricto
# Hace uso de functools.reduce, inmutabilidad de tuplas,
# y tipado nativo de Python 3.9 (PEP 585) sin necesidad de importaciones de `typing`.

from functools import reduce

def is_balanced(s: str) -> bool:
    """
    Verifica si los paréntesis están balanceados aplicando una plegado funcional (`reduce`)
    sobre una pila inmutable representada como una tupla.
    """
    pairs: dict[str, str] = {')': '(', ']': '['} | {'}': '{'}

    def step(acc: tuple[bool, tuple[str, ...]], char: str) -> tuple[bool, tuple[str, ...]]:
        is_valid, stack = acc
        if not is_valid:
            return False, stack
        if char in pairs:
            return (
                (bool(stack) and stack[-1] == pairs[char]),
                stack[:-1] if stack else stack
            )
        return True, stack + (char,)

    # Filtramos la cadena usando filter para garantizar solo caracteres válidos
    valid_chars = filter(lambda c: c in "()[]{}", s)

    # Acumulador inicial: (es_valido, pila_inmutable)
    valid, final_stack = reduce(step, valid_chars, (True, ()))
    
    return valid and len(final_stack) == 0


# --- Casos de prueba usando assert (Python 3.9 - Funcional) ---

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