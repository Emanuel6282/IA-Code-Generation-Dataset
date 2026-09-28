# P04: Balanced Parentheses — Python 3.11
#
# Características utilizadas:
# 1. Built-in generics (list[str], dict[str, str]):
#    Permiten utilizar las colecciones incorporadas directamente en las
#    anotaciones de tipos, sin importar List o Dict desde typing.
#
# 2. Anotaciones de tipos modernas:
#    Se mantienen las anotaciones de Python 3.11 para mejorar la claridad
#    de los parámetros y del valor de retorno.
#
# Nota:
# Python 3.11 no aporta una característica específica que mejore de forma
# relevante este algoritmo de pila. Por ello, no se fuerza el uso de
# características innecesarias y se conserva la lógica original.

def is_balanced(s: str) -> bool:
    stack: list[str] = []
    pairs: dict[str, str] = {
        ')': '(',
        ']': '[',
        '}': '{'
    }

    for char in s:
        if char in '([{':
            stack.append(char)
        elif char in ')]}':
            if not stack or stack[-1] != pairs[char]:
                return False
            stack.pop()

    return len(stack) == 0


assert is_balanced("()") == True
assert is_balanced("([)]") == False
assert is_balanced("") == True


# CAMBIOS REALIZADOS:
#
# 1. list[str]:
#    Reemplaza la anotación implícita de la lista por la sintaxis moderna
#    de tipos incorporada en Python.
#
# 2. dict[str, str]:
#    Especifica que el diccionario contiene caracteres como claves y valores.
#
# 3. Se conserva el algoritmo de pila:
#    La lógica original ya es eficiente y adecuada para el problema, por lo
#    que no se modifica innecesariamente.
#
# 4. Se conserva el bucle for:
#    El recorrido de la cadena sigue siendo iterativo y mantiene exactamente
#    el comportamiento del código original.
#
# 5. Se conservan los tres tests:
#    - "()"     -> True
#    - "([)]"   -> False
#    - ""       -> True
#
# 6. No se utilizan características funcionales:
#    Se mantienen for, variables mutables y la pila para preservar la lógica
#    original.
#
# 7. Compatibilidad con Python 3.11:
#    list[str] y dict[str, str] son sintaxis moderna de anotaciones de tipos
#    disponible desde Python 3.9 y plenamente idiomática en Python 3.11.
#
# 8. No se fuerza una característica exclusiva de Python 3.11:
#    Aunque Python 3.11 incorpora mejoras importantes, ninguna resulta
#    necesaria o claramente beneficiosa para este algoritmo concreto.