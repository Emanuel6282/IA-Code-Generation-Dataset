# P01: Word Frequency — migrado de Python 3.9 a Python 3.11
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Intérprete más rápido de CPython 3.11 (PEP 659, intérprete adaptativo
#    especializado): especializa en tiempo de ejecución operaciones repetidas
#    como el acceso a diccionarios y la suma de enteros dentro del bucle.
#    El código se acelera sin cambiar ni una línea; por eso se mantiene un
#    bucle simple, que es justo el patrón que más se beneficia.
# 2. Genéricos nativos en anotaciones (list[str], dict[str, int]): disponibles
#    desde 3.9 (PEP 585), pero los alias List/Dict de typing están obsoletos;
#    en una base de código que exige 3.11 ya no hay motivo para mantenerlos.
#
# Nota: las novedades sintácticas propias de 3.11 (ExceptionGroup/except*,
# typing.Self, LiteralString, tomllib) no aplican a una función de conteo, así
# que no se fuerzan: usarlas aquí empeoraría la legibilidad sin ganar nada.


def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}
    for word in words:
        if word in result:
            result[word] += 1
        else:
            result[word] = 1
    return result


# Casos de prueba (los mismos tres del código original)
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11
# 1. Se eliminó "from typing import List, Dict": los alias de typing para
#    colecciones están obsoletos desde 3.9; lo idiomático es usar los tipos
#    nativos directamente, sin importaciones.
# 2. List[str] -> list[str] y Dict[str, int] -> dict[str, int] en la firma,
#    por el mismo motivo.
# 3. Se anotó la variable local (result: dict[str, int] = {}): un {} vacío no
#    permite a los verificadores de tipos inferir el tipo de claves y valores;
#    la anotación lo deja explícito.
# 4. result[word] = result[word] + 1 -> result[word] += 1: asignación
#    aumentada, más concisa y sin repetir la expresión result[word].
# 5. Lógica, nombre de la función y tests sin cambios: los asserts siguen a
#    nivel de módulo, igual que en el original, para que se ejecuten en los
#    mismos momentos (también al importar el archivo).