# P04: Balanced Parentheses — Python 3.14
# Migración desde Python 3.11
#
# CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
#
# 1. Evaluación diferida de anotaciones (PEP 649 y PEP 749):
#    Introducida en Python 3.14.
#    Las anotaciones se evalúan cuando se necesitan,
#    en lugar de evaluarse inmediatamente al definir
#    una función.
#
# 2. Anotaciones modernas: list[str] y dict[str, str].
#    Mejoran la legibilidad y el análisis estático.
#    Esta sintaxis existe desde Python 3.9.
#
# 3. Optimizaciones del intérprete de Python 3.14:
#    Pueden mejorar el rendimiento de operaciones
#    habituales sin cambiar el algoritmo.
#
# 4. Se conserva el paradigma iterativo:
#    Bucle for, pila mutable y operaciones append()/pop().
#
# Biblioteca estándar únicamente.


def is_balanced(s: str) -> bool:
    # Pila mutable para almacenar símbolos de apertura.
    stack: list[str] = []

    # Correspondencia entre cierres y aperturas.
    pairs: dict[str, str] = {
        ')': '(',
        ']': '[',
        '}': '{'
    }

    # Recorrer los caracteres en orden.
    for char in s:
        if char in '([{':
            stack.append(char)

        elif char in ')]}':
            # Comprobar que exista una apertura correspondiente.
            if not stack or stack[-1] != pairs[char]:
                return False

            # Eliminar la apertura que acaba de cerrarse.
            stack.pop()

    # La pila debe quedar vacía.
    return len(stack) == 0


# TESTS ORIGINALES — CONSERVADOS SIN CAMBIOS

assert is_balanced("()") == True
assert is_balanced("([)]") == False
assert is_balanced("") == True


# CAMBIOS REALIZADOS — PYTHON 3.11 A PYTHON 3.14
#
# 1. Se actualizó la documentación a Python 3.14.
#    Motivo: identificar claramente la versión de destino.
#
# 2. Se añadió:
#       stack: list[str] = []
#    Motivo: documentar el tipo de los elementos de la pila.
#
# 3. Se añadió:
#       pairs: dict[str, str]
#    Motivo: mejorar la claridad y facilitar el
#    análisis estático.
#
# 4. Se aprovecha la evaluación diferida de anotaciones
#    introducida en Python 3.14.
#    Motivo: utilizar el comportamiento moderno del
#    sistema de anotaciones sin alterar el algoritmo.
#
# 5. Se conservó el bucle for.
#    Motivo: es una forma idiomática y eficiente de
#    recorrer cadenas en Python.
#
# 6. Se conservaron append() y pop().
#    Motivo: implementan eficientemente una pila LIFO.
#
# 7. Se mantuvo la condición:
#       if not stack or stack[-1] != pairs[char]:
#    Motivo: conservar la validación y el cortocircuito
#    de la expresión original.
#
# 8. Se mantuvo:
#       return len(stack) == 0
#    Motivo: garantizar exactamente el mismo resultado.
#
# 9. Se conservaron los tres assert originales.
#    Motivo: comprobar los mismos escenarios.
#
# 10. No se introdujeron recursión ni dependencias externas.
#     Motivo: preservar la lógica original.