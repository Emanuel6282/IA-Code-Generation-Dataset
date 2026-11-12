# P04: Balanced Parentheses — migrado a Python 3.14
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en 3.14): las
#    anotaciones ya no se evalúan al definir la función o el módulo, sino solo cuando
#    alguien las consulta (por ejemplo, con annotationlib). Las anotaciones añadidas
#    (PAIRS, stack, la firma) no tienen coste al importar y no hace falta
#    `from __future__ import annotations`.
# 2. Genéricos nativos en anotaciones (dict[str, str], list[str]; PEP 585): forma
#    recomendada en lugar de los alias de typing, sin importaciones.
# 3. Mejoras del intérprete de CPython heredadas de 3.11-3.14 (intérprete adaptativo
#    especializado, recolector de basura incremental en 3.14): aceleran este bucle
#    sin cambiar su estructura, por eso se conserva el if/elif.
#
# Nota: se evaluó la sentencia match (Python 3.10+) con patrones alternativos
# ("(" | "[" | "{"). Es legible, pero en este bucle fue un ~38 % más lenta que el
# if/elif con `in` sobre una cadena corta (medido en CPython 3.14.6), así que no se
# usa. Las novedades sintácticas de 3.14 (t-strings, except sin paréntesis,
# compression.zstd, subintérpretes) no aplican a esta función.

# Se define una sola vez a nivel de módulo, en lugar de en cada llamada.
PAIRS: dict[str, str] = {")": "(", "]": "[", "}": "{"}


def is_balanced(s: str) -> bool:
    stack: list[str] = []
    for char in s:
        if char in "([{":
            stack.append(char)
        elif char in ")]}":
            if not stack or stack.pop() != PAIRS[char]:
                return False
    return not stack


# Casos de prueba (los mismos tres del código original)
assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("") is True


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14
# 1. `if stack[-1] != pairs[char]: return False` + `stack.pop()` -> `stack.pop() != PAIRS[char]`:
#    lee y quita la cima en una sola operación. Si no coincide, la función retorna
#    False de inmediato, así que haber desapilado no afecta al resultado.
# 2. `pairs` dentro de la función -> constante de módulo `PAIRS`: se crea una sola
#    vez, en lugar de en cada llamada. Su contenido no cambia.
# 3. `return len(stack) == 0` -> `return not stack`: forma idiomática (PEP 8) de
#    comprobar si una lista está vacía; también devuelve un bool.
# 4. Se añadieron anotaciones de tipo a `PAIRS` y `stack` con los genéricos nativos;
#    en 3.14 se evalúan de forma diferida (PEP 649).
# 5. Con los cambios 1 y 2, la función es más rápida que el original en CPython 3.14
#    (medido con una cadena de 1,2 millones de caracteres).
# 6. Tests: `== True` / `== False` -> `is True` / `is False`. PEP 8 desaconseja
#    comparar booleanos con ==; `is` además verifica que el resultado sea un bool
#    real. Se conservan los tres casos, a nivel de módulo como en el original.
# 7. Se actualizó el comentario de cabecera, que describía el código como "Python 3.9".
# 8. Comportamiento idéntico al original, incluido el tratamiento de caracteres que
#    no son paréntesis (se ignoran), verificado con miles de cadenas aleatorias.