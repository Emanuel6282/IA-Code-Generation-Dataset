# Python 3.14 - Paradigma Funcional Estricto
# Hace uso de functools.reduce, inmutabilidad de tuplas,
# sintaxis de Pattern Matching (PEP 634) y evaluación diferida de anotaciones de tipo (PEP 649).

from functools import reduce


def is_balanced(s: str) -> bool:
    """
    Verifica si los paréntesis, corchetes y llaves están balanceados aplicando
    una reducción funcional (`functools.reduce`) sobre una pila inmutable representada
    por una tupla de caracteres.
    """
    pairs: dict[str, str] = {')': '(', ']': '['} | {'}': '{'}

    def step(
        acc: tuple[bool, tuple[str, ...]], char: str
    ) -> tuple[bool, tuple[str, ...]]:
        is_valid, stack = acc
        if not is_valid:
            return False, stack

        match char:
            case ")" | "]" | "}":
                valid_pop = bool(stack) and stack[-1] == pairs[char]
                return valid_pop, stack[:-1] if stack else ()
            case "(" | "[" | "{":
                return True, stack + (char,)
            case _:
                return False, stack

    # Filtrado explícito mediante filter() para procesar únicamente caracteres válidos
    valid_chars = filter(lambda c: c in "()[]{}", s)

    # Estado inicial: (es_valido, pila_inmutable)
    is_valid, final_stack = reduce(step, valid_chars, (True, ()))

    return is_valid and len(final_stack) == 0


# --- Casos de prueba usando assert (Python 3.14 - Funcional) ---

if __name__ == "__main__":
    # Caso 1: Ejemplos base del problema
    assert is_balanced("()") is True, "Falló ()"
    assert is_balanced("([)]") is False, "Falló ([)]"
    assert is_balanced("{[]}") is True, "Falló {[]}"

    # Caso 2: Cadena vacía y secuencias válidas intercaladas
    assert is_balanced("") is True, "Falló cadena vacía"
    assert is_balanced("()[]{}") is True, "Falló ()[]{}"

    # Caso 3: Desbalance por apertura o cierre desequilibrado
    assert is_balanced("((()") is False, "Falló ((()"
    assert is_balanced("]") is False, "Falló ]"

    print("¡Todos los casos de prueba pasaron correctamente!")