# P01: Word Frequency — migrado a Python 3.14
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en 3.14):
#    las anotaciones ya no se evalúan al definir la función, sino solo cuando
#    alguien las consulta (por ejemplo, con annotationlib). Definir la función
#    cuesta menos y ya no hace falta "from __future__ import annotations" ni
#    poner tipos entre comillas. No requiere cambiar el código: basta con
#    escribir anotaciones normales.
# 2. Genéricos nativos en anotaciones (list[str], dict[str, int], PEP 585):
#    los alias List/Dict de typing están obsoletos; con la evaluación diferida
#    tampoco hay ninguna razón de rendimiento para importarlos.
# 3. Mejoras del intérprete de CPython heredadas de 3.11-3.14 (intérprete
#    adaptativo especializado, recolector de basura incremental en 3.14):
#    aceleran este bucle sin cambiar el código, por eso se mantiene simple.
#
# Nota: las novedades sintácticas de 3.14 (t-strings, except sin paréntesis,
# compression.zstd, subintérpretes) no aplican a una función de conteo, así
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


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14
# 1. Se eliminó "from typing import List, Dict": los alias de typing para
#    colecciones están obsoletos; lo idiomático es usar los tipos nativos sin
#    importaciones.
# 2. List[str] -> list[str] y Dict[str, int] -> dict[str, int] en la firma.
#    En 3.14 estas anotaciones se evalúan de forma diferida (PEP 649), así que
#    no tienen costo al definir la función.
# 3. Se anotó la variable local (result: dict[str, int] = {}): un {} vacío no
#    permite a los verificadores de tipos inferir claves y valores.
# 4. result[word] = result[word] + 1 -> result[word] += 1: asignación
#    aumentada, más concisa y sin repetir result[word].
# 5. Se actualizó el comentario de cabecera, que describía el código como
#    "compatible con Python 3.9".
# 6. Lógica, nombre de la función y tests sin cambios: los asserts siguen a
#    nivel de módulo, igual que en el original, para que se ejecuten en los
#    mismos momentos (también al importar el archivo).