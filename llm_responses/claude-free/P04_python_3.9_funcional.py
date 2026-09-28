from functools import reduce


def is_balanced(s):
    """
    Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
    "{", "}", retorna True si todos los parentesis/corchetes estan
    correctamente balanceados y cerrados en orden. Retorna False en caso
    contrario.

    Implementacion en paradigma funcional: usa functools.reduce para
    recorrer la cadena y acumular el estado de forma inmutable. El
    acumulador es una tupla (pila, balanceado), donde "pila" es en si
    misma una tupla inmutable (nunca se muta, se reconstruye con "+" o
    slicing en cada paso) y "balanceado" es un booleano. No se usan
    bucles for/while con estado mutable.

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

    def procesar(estado, caracter):
        pila, balanceado = estado

        # Si ya se detecto un desbalance, se propaga el estado sin cambios
        # (cortocircuito funcional: no se sigue "trabajando" una vez que
        # el resultado ya esta decidido).
        if not balanceado:
            return estado

        if caracter in ('(', '[', '{'):
            # Nueva tupla inmutable: se concatena, no se muta la original.
            return pila + (caracter,), True

        if caracter in pares_cierre:
            if len(pila) == 0 or pila[-1] != pares_cierre[caracter]:
                return pila, False
            # Nueva tupla sin el ultimo elemento (slicing crea una copia).
            return pila[:-1], True

        # Caracter inesperado (fuera del alfabeto valido): se ignora,
        # manteniendo el estado igual.
        return estado

    pila_final, balanceado_final = reduce(procesar, s, ((), True))

    return balanceado_final and len(pila_final) == 0


if __name__ == "__main__":
    # Caso 1: balanceado simple
    assert is_balanced("()") == True

    # Caso 2: orden incorrecto de cierre -> no balanceado
    assert is_balanced("([)]") == False

    # Caso 3: anidamiento balanceado de varios tipos
    assert is_balanced("{[]}") == True

    print("Todos los casos de prueba pasaron correctamente.")