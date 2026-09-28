# P01: Word Frequency — Python 3.11 (migrado desde Python 3.9)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[str], dict[str, int]) en lugar de
#    typing.List / typing.Dict. Esto es una caracteristica de PEP 585, disponible
#    desde Python 3.9, pero en 3.11 es plenamente el estilo idiomatico y
#    recomendado, por lo que se elimina la dependencia del modulo "typing".
# 2. Nota de honestidad: Python 3.11 introdujo mejoras notables como el interprete
#    mas rapido (PEP 659, "Specializing Adaptive Interpreter"), grupos de
#    excepciones (except*, PEP 654) y mensajes de error mas precisos (PEP 657).
#    Ninguna de ellas aplica de forma natural a este problema: no hay manejo de
#    excepciones que se beneficie de "except*", y la mejora de rendimiento del
#    interprete es automatica (no requiere cambios en el codigo). Por eso no se
#    fuerzan cambios de sintaxis que no aporten valor real aqui.

def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}
    for word in words:
        if word in result:
            result[word] = result[word] + 1
        else:
            result[word] = 1
    return result


# Casos de prueba (identicos a la version 3.9)
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se elimino "from typing import List, Dict" y se reemplazaron las anotaciones
#    List[str] -> list[str] y Dict[str, int] -> dict[str, int]. Desde Python 3.9
#    (PEP 585) los tipos incorporados (list, dict, set, tuple, etc.) soportan
#    subindices genericos de forma nativa, por lo que ya no es necesario importar
#    equivalentes desde "typing". En 3.11 esto es el estilo estandar y reduce una
#    dependencia innecesaria del codigo.
# 2. Se anadio la anotacion de tipo explicita "result: dict[str, int] = {}" en la
#    variable local, aprovechando la misma sintaxis de genericos nativos, lo cual
#    mejora la legibilidad y ayuda a herramientas de analisis estatico (mypy,
#    pyright) sin cambiar el comportamiento en tiempo de ejecucion.
# 3. La logica interna (bucle for, condicional if/else, acumulacion en el
#    diccionario) se mantuvo exactamente igual, ya que el requisito pedia
#    comportamiento identico; no habia ninguna caracteristica de 3.11 que
#    mejorara esta logica sin alterarla artificialmente.
# 4. El nombre de la funcion "word_frequency" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones, ya que ya eran compatibles con
#    Python 3.11 (assert es parte del lenguaje base y no depende de version).