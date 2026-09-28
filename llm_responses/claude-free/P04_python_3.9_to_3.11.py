# P04: Balanced Parentheses — Python 3.11 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Nota de honestidad: el codigo base ya usaba anotaciones de tipo
#    nativas (str, bool) que no dependian de "typing", y no habia ningun
#    "typing.List"/"typing.Dict" que reemplazar por genericos nativos (la
#    variable "stack" y el diccionario "pairs" no estaban anotados). Se
#    revisaron otras novedades de 3.11: los grupos de excepciones (except*,
#    PEP 654) y los mensajes de error mas precisos en tracebacks (PEP 657).
#    Ninguna aplica de forma natural aqui, ya que no hay manejo de
#    excepciones en este algoritmo de balanceo con pila, y la mejora de
#    precision de tracebacks es automatica (no requiere cambios de
#    sintaxis). Por transparencia y consistencia con el resto de esta
#    serie, se añaden anotaciones de tipo explicitas con genericos nativos
#    (list[str], dict[str, str]) en las variables locales, ya que es el
#    estilo idiomatico vigente en 3.11 (PEP 585, disponible desde 3.9) y
#    mejora la legibilidad, aunque no estuvieran presentes en el codigo
#    base original.

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
# 1. Se agregaron anotaciones de tipo explicitas a las variables locales
#    "stack: list[str]" y "pairs: dict[str, str]", usando los genericos
#    nativos de los tipos incorporados (PEP 585, desde Python 3.9, estilo
#    estandar en 3.11), ya que el codigo base no las tenia en absoluto para
#    estas variables. Esto mejora la legibilidad y el soporte de
#    herramientas de analisis estatico, sin cambiar el comportamiento en
#    tiempo de ejecucion.
# 2. La logica interna (recorrido con for, verificacion de apertura/cierre
#    mediante pertenencia a los strings literales "([{"/")]}",  uso del
#    diccionario "pairs" para validar coincidencias, pila con
#    append/pop) se mantuvo exactamente igual, cumpliendo el requisito de
#    comportamiento identico; no existia ninguna caracteristica de 3.11 que
#    mejorara esta logica de balanceo con pila sin alterarla
#    artificialmente.
# 3. El nombre de la funcion "is_balanced" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones, ya que ya eran compatibles
#    con Python 3.11.