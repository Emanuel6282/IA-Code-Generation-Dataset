# P04: Balanced Parentheses — Python 3.11
# Migración desde Python 3.9
#
# CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
#
# 1. Anotaciones de tipos con genéricos integrados:
#    list[str] y dict[str, str].
#    Mejoran la legibilidad y permiten documentar
#    los tipos sin importar List o Dict de typing.
#    Esta sintaxis existe desde Python 3.9.
#
# 2. Especialización adaptativa del intérprete (PEP 659):
#    Introducida en Python 3.11.
#    Puede optimizar operaciones frecuentes como
#    accesos a listas, diccionarios y llamadas a métodos,
#    sin modificar el algoritmo.
#
# 3. Mensajes de error mejorados en Python 3.11:
#    Facilitan la depuración cuando se produce
#    una excepción.
#
# 4. Se mantiene el paradigma iterativo:
#    for, pila mutable y operaciones append()/pop().
#
# Biblioteca estándar únicamente.


def is_balanced(s: str) -> bool:
    # Pila para almacenar símbolos de apertura.
    stack: list[str] = []

    # Correspondencia entre símbolos de cierre y apertura.
    pairs: dict[str, str] = {
        ')': '(',
        ']': '[',
        '}': '{'
    }

    # Recorrer cada carácter de la cadena.
    for char in s:
        if char in '([{':
            stack.append(char)

        elif char in ')]}':
            # Verificar que exista una apertura correspondiente.
            if not stack or stack[-1] != pairs[char]:
                return False

            # Eliminar la apertura que acaba de cerrarse.
            stack.pop()

    # La pila debe estar vacía al finalizar.
    return len(stack) == 0


# TESTS ORIGINALES — CONSERVADOS SIN CAMBIOS

assert is_balanced("()") == True
assert is_balanced("([)]") == False
assert is_balanced("") == True


# CAMBIOS REALIZADOS — PYTHON 3.9 A PYTHON 3.11
#
# 1. Se añadió la anotación:
#       stack: list[str] = []
#    Motivo: documentar explícitamente el tipo de
#    elementos almacenados en la pila.
#
# 2. Se añadió la anotación:
#       pairs: dict[str, str]
#    Motivo: mejorar la claridad y facilitar el
#    análisis estático del código.
#
# 3. Se mantuvo el bucle for.
#    Motivo: sigue siendo la forma idiomática de
#    recorrer una cadena en Python 3.11.
#
# 4. Se conservaron append() y pop().
#    Motivo: permiten implementar eficientemente
#    una pila utilizando una lista.
#
# 5. Se mantuvo la condición:
#       if not stack or stack[-1] != pairs[char]:
#    Motivo: conserva la validación original y
#    aprovecha la evaluación de cortocircuito.
#
# 6. Se mantuvo:
#       return len(stack) == 0
#    Motivo: garantiza exactamente el mismo resultado.
#
# 7. Se conservaron los tres assert originales.
#    Motivo: comprobar la compatibilidad funcional.
#
# 8. Se aprovechan automáticamente las optimizaciones
#    del intérprete de Python 3.11 (PEP 659).
#    Motivo: mejorar potencialmente el rendimiento
#    sin modificar la lógica del algoritmo.
#
# 9. No se introdujo recursión ni se reemplazó la pila.
#    Motivo: mantener el comportamiento original.