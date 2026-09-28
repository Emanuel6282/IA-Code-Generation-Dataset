# P04: Balanced Parentheses — Python 3.14 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[str], dict[str, str]), PEP 585
#    (desde 3.9), estilo idiomatico vigente en 3.14. El codigo base no
#    tenia anotaciones en "stack"/"pairs"; se añaden por legibilidad y
#    consistencia con el resto de esta serie, sin cambiar el
#    comportamiento en tiempo de ejecucion.
# 2. PEP 649 (evaluacion diferida de anotaciones, activada por defecto en
#    3.14): las anotaciones "str", "bool", "list[str]" y "dict[str, str]"
#    ahora se evaluan de forma perezosa por el interprete de manera
#    automatica, sin necesidad de "from __future__ import annotations" ni
#    comillas. No cambia la sintaxis visible del codigo, pero es una
#    mejora de comportamiento nativa de 3.14 que ya beneficia a esta
#    funcion.
# 3. Nota de honestidad: tambien revise las t-strings (PEP 750) y otras
#    mejoras internas del interprete en 3.14; ninguna aplica de forma
#    natural aqui, ya que no hay formateo de strings ni necesidad de
#    plantillas en este algoritmo de balanceo con pila. Forzarlas anadiria
#    complejidad sin aportar valor real. Tambien se reviso "except*" (PEP
#    654); no aplica porque no hay manejo de excepciones en este algoritmo.

def is_balanced(s: str) -> bool:
    stack: list[str] = []
    pairs: dict[str, str] = {')': '(', ']': '[', '}': '{'}
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

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se agregaron anotaciones de tipo explicitas "stack: list[str]" y
#    "pairs: dict[str, str]", usando genericos nativos de los tipos
#    incorporados (PEP 585), ya que el codigo base no las tenia. Mejora la
#    legibilidad y el soporte de herramientas de analisis estatico, sin
#    cambiar el comportamiento en tiempo de ejecucion.
# 2. Gracias a PEP 649 (activo por defecto desde 3.14), todas las
#    anotaciones de tipo de la funcion y de las variables locales se
#    evaluan de forma diferida automaticamente por el interprete; esto no
#    requiere cambios de sintaxis adicionales en este caso, pero es una
#    caracteristica nativa de la version que ya opera en segundo plano.
# 3. La logica interna (recorrido con for, verificacion de apertura/cierre
#    mediante pertenencia a los strings literales "([{"/")]}",  uso del
#    diccionario "pairs" para validar coincidencias, pila con
#    append/pop) se mantuvo exactamente igual, cumpliendo el requisito de
#    comportamiento identico; no existia ninguna caracteristica de 3.14 que
#    mejorara esta logica de balanceo con pila sin alterarla
#    artificialmente.
# 4. El nombre de la funcion "is_balanced" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones, ya que "assert" es parte
#    del lenguaje base y no depende de la version de Python.