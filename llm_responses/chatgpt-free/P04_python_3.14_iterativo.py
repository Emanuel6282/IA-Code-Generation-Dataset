# Python 3.14 — Balanced Parentheses
#
# Características utilizadas:
# 1. list[str] y dict[str, str]:
#    Sintaxis moderna de anotaciones de tipos, disponible desde Python 3.9.
#
# 2. Pila mutable:
#    Se utiliza una lista para almacenar los símbolos de apertura y
#    comprobarlos al encontrar sus correspondientes cierres.
#
# 3. for:
#    El recorrido de la cadena se realiza de forma estrictamente iterativa.
#
# No se utilizan map(), filter(), reduce() ni recursión.
# Python 3.14 no aporta una característica específica necesaria para
# mejorar este algoritmo sin cambiar innecesariamente su diseño.

def is_balanced(s: str) -> bool:
    stack: list[str] = []

    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{",
    }

    for char in s:
        if char in "([{":
            stack.append(char)
        else:
            if not stack or stack.pop() != pairs[char]:
                return False

    return not stack


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True