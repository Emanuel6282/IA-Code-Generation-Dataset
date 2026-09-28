def is_balanced(s):
    """
    Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
    "{", "}", retorna True si todos los parentesis/corchetes estan
    correctamente balanceados y cerrados en orden. Retorna False en caso
    contrario.

    Implementacion mediante una pila: cada caracter de apertura se apila;
    cada caracter de cierre debe coincidir con el ultimo caracter de
    apertura apilado (el mas reciente y aun no cerrado), o la cadena no
    esta balanceada.

    Args:
        s: Cadena que contiene unicamente los caracteres de parentesis y
           corchetes indicados.

    Returns:
        True si la cadena esta correctamente balanceada, False en caso
        contrario. Una cadena vacia se considera balanceada (True).
    """
    pares_cierre = {
        ')': '(',
        ']': '[',
        '}': '{',
    }
    pila = []

    for caracter in s:
        if caracter in ('(', '[', '{'):
            pila.append(caracter)
        elif caracter in pares_cierre:
            if not pila or pila[-1] != pares_cierre[caracter]:
                return False
            pila.pop()

    return len(pila) == 0


if __name__ == "__main__":
    # Caso 1: balanceado simple
    assert is_balanced("()") == True

    # Caso 2: orden incorrecto de cierre -> no balanceado
    assert is_balanced("([)]") == False

    # Caso 3: anidamiento balanceado de varios tipos
    assert is_balanced("{[]}") == True

    print("Todos los casos de prueba pasaron correctamente.")