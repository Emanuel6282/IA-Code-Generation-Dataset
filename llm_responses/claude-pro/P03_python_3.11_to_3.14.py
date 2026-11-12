# P03: Flatten Nested List — migrado a Python 3.14
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en 3.14): las
#    anotaciones ya no se evalúan al definir la función, sino solo cuando alguien
#    las consulta (por ejemplo, con annotationlib). Definir la función cuesta menos
#    y ya no hace falta `from __future__ import annotations` ni poner tipos entre
#    comillas. No requiere cambiar el código: basta con anotar normalmente.
# 2. Sentencia `type` para alias de tipo (PEP 695, Python 3.12+): declara el tipo
#    recursivo de la entrada sin comillas ni TypeAlias, porque su valor también se
#    evalúa de forma perezosa y puede referirse a sí mismo. Sustituye a `Any`.
# 3. Mejoras del intérprete de CPython heredadas de 3.11-3.14 (intérprete
#    adaptativo especializado, recolector de basura incremental en 3.14): aceleran
#    este bucle sin cambiar su estructura, por eso se conserva la pila explícita.
#
# Nota: las novedades sintácticas de 3.14 (t-strings, except sin paréntesis,
# compression.zstd, subintérpretes) no aplican a esta función, así que no se
# fuerzan: usarlas aquí empeoraría la legibilidad sin ganar nada.

# Tipo recursivo: un entero o una lista de elementos del mismo tipo.
type Nested = int | list[Nested]


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


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14
# 1. Se eliminó `from typing import Any, List`: con la sentencia `type` y los
#    genéricos nativos (list[...]) ya no hace falta importar nada de typing.
# 2. `nested: Any` -> `nested: Nested` (alias recursivo `int | list[Nested]`): Any
#    desactivaba la verificación de tipos. El nuevo tipo documenta exactamente lo
#    que la función acepta, incluido un entero suelto en el nivel superior, que el
#    original también admitía (flatten(5) == [5]). Es solo una anotación: el
#    comportamiento en tiempo de ejecución no cambia.
# 3. `List[int]` -> `list[int]` en el retorno, y se anotaron `result` y `stack`
#    (una lista vacía no permite a los verificadores inferir su tipo). En 3.14
#    todas estas anotaciones se evalúan de forma diferida (PEP 649).
# 4. `for element in reversed(item): stack.append(element)` -> `stack.extend(reversed(item))`:
#    misma operación y mismo orden en la pila, pero en una sola llamada que se
#    ejecuta en C, más concisa y más rápida en sublistas grandes.
# 5. El algoritmo no cambia: misma pila, mismo orden de procesamiento y mismos
#    resultados (verificado contra el original con miles de listas aleatorias).
# 6. Se actualizó el comentario de cabecera, que describía el código como
#    "Python 3.9".
# 7. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en
#    el original, para que se ejecuten en los mismos momentos.