# P04: Balanced Parentheses — Python 3.14
#
# CARACTERÍSTICAS DE PYTHON 3.14 UTILIZADAS:
#
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749):
#    Python 3.14 evalúa las anotaciones de forma diferida por defecto.
#    Esto reduce el coste de evaluar las anotaciones al definir la función
#    y permite mantener anotaciones modernas sin evaluarlas inmediatamente.
#
# 2. Anotaciones con tipos incorporados:
#    list[str] y dict[str, str] se utilizan para expresar los tipos
#    directamente con las colecciones incorporadas.
#
# Nota:
# No se fuerzan otras características nuevas de Python 3.14 porque no
# aportan una mejora real a este algoritmo. Se conserva la estructura
# iterativa original para garantizar exactamente el mismo comportamiento.

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
        elif char in ")]}":
            if not stack or stack[-1] != pairs[char]:
                return False
            stack.pop()

    return len(stack) == 0


assert is_balanced("()") == True
assert is_balanced("([)]") == False
assert is_balanced("") == True


# CAMBIOS REALIZADOS:
#
# 1. Se actualizó el código a Python 3.14.
#
# 2. Se mantienen las anotaciones:
#
#       s: str
#       -> bool
#
#    Python 3.14 utiliza evaluación diferida de anotaciones por defecto,
#    una mejora introducida mediante PEP 649 y PEP 749.
#
# 3. Se añadieron anotaciones explícitas a las estructuras:
#
#       stack: list[str]
#       pairs: dict[str, str]
#
#    Esto mejora la legibilidad y permite conocer inmediatamente qué tipos
#    contiene cada estructura.
#
# 4. Se mantiene la pila mutable:
#
#       stack.append(char)
#       stack.pop()
#
#    Esto conserva el algoritmo iterativo original y evita introducir
#    complejidad innecesaria.
#
# 5. Se mantiene el bucle for:
#
#       for char in s:
#
#    El comportamiento sigue siendo estrictamente iterativo.
#
# 6. Se mantiene la comprobación:
#
#       if not stack or stack[-1] != pairs[char]:
#
#    Esto garantiza que cada símbolo de cierre corresponda al último
#    símbolo de apertura almacenado.
#
# 7. Se mantiene:
#
#       return len(stack) == 0
#
#    Por tanto, una cadena vacía continúa considerándose balanceada.
#
# 8. No se utilizan map(), filter(), reduce() ni recursión.
#
# 9. No se utilizan bibliotecas externas.
#
# 10. Se conservan exactamente los tres casos de prueba originales:
#       - "()"   -> True
#       - "([)]" -> False
#       - ""     -> True
#
# 11. No se fuerzan características como template strings, PEP 758,
#     múltiples intérpretes o Zstandard, porque no tienen relación con
#     este algoritmo y no aportarían legibilidad ni rendimiento.