# Python 3.11 - Paradigma Funcional Estricto
# Utiliza functools.reduce, inmutabilidad mediante tuplas,
# y la sintaxis Structural Pattern Matching (match-case) introducida en Python 3.10/3.11
# dentro de las expresiones reductoras.

from functools import reduce


def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena con caracteres '()[]{}' está correctamente balanceada
    aplicando una reducción funcional sobre una pila inmutable de tuplas.
    """
    pairs: dict[str, str] = {')': '(', ']': '['} | {'}': '{'}

    def step(
        acc: tuple[bool, tuple[str, ...]], char: str
    ) -> tuple[bool, tuple[str, ...]]:
        is_valid, stack = acc
        if not is_valid:
            return False, stack

        # Structural Pattern Matching (Python 3.10+) en paradigma funcional
        match char:
            case ")" | "]" | "}":
                valid_pop = bool(stack) and stack[-1] == pairs[char]
                return valid_pop, stack[:-1] if stack else ()
            case "(" | "[" | "{":
                return True, stack + (char,)
            case _:
                return False, stack

    # Filtramos la cadena usando filter() para validar únicamente caracteres permitidos
    valid_chars = filter(lambda c: c in "()[]{}", s)

    # Estado inicial: (es_valido, pila_inmutable)
    is_valid, final_stack = reduce(step, valid_chars, (True, ()))

    return is_valid and len(final_stack) == 0


# --- Casos de prueba usando assert (Python 3.11 - Funcional) ---

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