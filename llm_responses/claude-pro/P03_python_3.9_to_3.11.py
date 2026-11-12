# P03: Flatten Nested List — migrado de Python 3.9 a Python 3.11
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Intérprete más rápido de CPython 3.11 (PEP 659, intérprete adaptativo
#    especializado): especializa en tiempo de ejecución las operaciones que más se
#    repiten en este bucle (isinstance, pop, append). El código se acelera sin
#    cambiar su estructura; por eso se conserva el bucle con pila explícita.
# 2. Sintaxis de unión `X | Y` en tipos (PEP 604, Python 3.10+) y typing.TypeAlias
#    (PEP 613, 3.10+): permiten describir con precisión el tipo recursivo de la
#    entrada en lugar de `Any`, sin importar Union.
#
# Nota: las novedades sintácticas propias de 3.11 (ExceptionGroup/except*,
# typing.Self, LiteralString, tomllib) no aplican a esta función, así que no se
# fuerzan: usarlas aquí empeoraría la legibilidad sin ganar nada.

from typing import TypeAlias

# Tipo recursivo: un entero o una lista de elementos del mismo tipo. Se escribe como
# cadena porque se refiere a sí mismo.
Nested: TypeAlias = "int | list[Nested]"


def flatten(nested: Nested) -> list[int]:
    result: list[int] = []
    stack: list[Nested] = [nested]
    while stack:
        item = stack.pop()
        if isinstance(item, list):
            stack.extend(reversed(item))
        else:
            result.append(item)
    return result


# Casos de prueba (los mismos tres del código original)
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, 2, 3]) == [1, 2, 3]


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11
# 1. `from typing import Any, List` -> `from typing import TypeAlias`: los alias de
#    typing para colecciones (List) están obsoletos; lo idiomático es list[...].
# 2. `nested: Any` -> `nested: Nested` (alias recursivo `int | list[Nested]`): Any
#    desactivaba la verificación de tipos. El nuevo tipo documenta exactamente lo
#    que la función acepta, incluido un entero suelto en el nivel superior, que el
#    original también admitía (flatten(5) == [5]). Es solo una anotación: el
#    comportamiento en tiempo de ejecución no cambia.
# 3. `List[int]` -> `list[int]` en el retorno, y se anotaron `result` y `stack`
#    (una lista vacía no permite a los verificadores inferir su tipo).
# 4. `for element in reversed(item): stack.append(element)` -> `stack.extend(reversed(item))`:
#    misma operación y mismo orden en la pila, pero en una sola llamada que se
#    ejecuta en C, más concisa y más rápida en sublistas grandes.
# 5. El algoritmo no cambia: misma pila, mismo orden de procesamiento y mismos
#    resultados (verificado contra el original con miles de listas aleatorias).
# 6. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en
#    el original, para que se ejecuten en los mismos momentos.