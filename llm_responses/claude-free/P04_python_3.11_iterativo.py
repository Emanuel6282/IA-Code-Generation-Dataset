def is_balanced(s: str) -> bool:
    """
    Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
    "{", "}", retorna True si todos los parentesis/corchetes estan
    correctamente balanceados y cerrados en orden. Retorna False en caso
    contrario.

    Implementacion estrictamente iterativa: usa una lista mutable como
    pila y un bucle for explicito, sin map(), filter(), reduce() ni
    recursion. Compatible con Python 3.11.

    Args:
        s: Cadena que contiene unicamente los caracteres de parentesis y
           corchetes indicados.

    Returns:
        True si la cadena esta correctamente balanceada, False en caso
        contrario. Una cadena vacia se considera balanceada (True).
    """
    pares_cierre: dict[str, str] = {
        ')': '(',
        ']': '[',
        '}': '{',
    }
    pila: list[str] = []
    balanceado: bool = True

    for caracter in s:
        if not balanceado:
            break
        if caracter == '(' or caracter == '[' or caracter == '{':
            pila.append(caracter)
        elif caracter in pares_cierre:
            if len(pila) == 0:
                balanceado = False
            elif pila[-1] != pares_cierre[caracter]:
                balanceado = False
            else:
                pila.pop()

    if len(pila) != 0:
        balanceado = False

    return balanceado


if __name__ == "__main__":
    # Caso 1: balanceado simple
    assert is_balanced("()") == True

    # Caso 2: orden incorrecto de cierre -> no balanceado
    assert is_balanced("([)]") == False

    # Caso 3: anidamiento balanceado de varios tipos
    assert is_balanced("{[]}") == True

    print("Todos los casos de prueba pasaron correctamente.")