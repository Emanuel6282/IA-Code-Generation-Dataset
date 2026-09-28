# P01: Word Frequency — Python 3.14 (migrado desde el codigo base)
#
# CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
# 1. Genericos nativos en anotaciones (list[str], dict[str, int]) en lugar de
#    typing.List / typing.Dict. Disponible desde PEP 585 (Python 3.9), pero es
#    el estilo plenamente idiomatico en 3.14; se elimina la dependencia de
#    "typing" para este caso simple.
# 2. PEP 649 (evaluacion diferida de anotaciones, activada por defecto en 3.14):
#    las anotaciones de tipo como "list[str]" y "dict[str, int]" ahora se evaluan
#    de forma perezosa (lazy) automaticamente por el interprete, sin necesidad de
#    "from __future__ import annotations" ni comillas. Esto no cambia el codigo
#    visualmente, pero es una mejora de rendimiento/comportamiento nativa de 3.14
#    que ya beneficia a esta funcion sin requerir sintaxis adicional.
# 3. Nota de honestidad: tambien revise las t-strings (PEP 750) y otras mejoras
#    del interprete en 3.14; ninguna aplica de forma natural aqui, ya que no hay
#    formateo de strings ni necesidad de plantillas en este problema. Forzarlas
#    anadiria complejidad sin aportar valor real.

def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}
    for word in words:
        if word in result:
            result[word] = result[word] + 1
        else:
            result[word] = 1
    return result


# Casos de prueba (identicos al codigo base)
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}

# CAMBIOS REALIZADOS Y JUSTIFICACION:
# 1. Se elimino "from typing import List, Dict" y se reemplazaron las anotaciones
#    List[str] -> list[str] y Dict[str, int] -> dict[str, int], usando genericos
#    nativos de los tipos incorporados (PEP 585). Esto reduce una dependencia
#    innecesaria del modulo "typing" y es el estilo estandar en 3.14.
# 2. Se anadio la anotacion explicita "result: dict[str, int] = {}" en la
#    variable local, mejorando la legibilidad y el soporte para herramientas de
#    analisis estatico (mypy, pyright), sin alterar el comportamiento en tiempo
#    de ejecucion.
# 3. Gracias a PEP 649 (activo por defecto desde 3.14), las anotaciones de tipo
#    de la funcion se evaluan de forma diferida automaticamente por el
#    interprete; esto no requiere cambios de sintaxis adicionales en este caso,
#    pero es una caracteristica nativa de la version que ya opera en segundo
#    plano.
# 4. La logica interna (bucle for, condicional if/else, acumulacion en el
#    diccionario) se mantuvo exactamente igual, cumpliendo el requisito de
#    comportamiento identico; no existia ninguna caracteristica de 3.14 que
#    mejorara esta logica especifica sin alterarla artificialmente.
# 5. El nombre de la funcion "word_frequency" y los tres casos de prueba con
#    "assert" se conservaron sin modificaciones, ya que "assert" es parte del
#    lenguaje base y no depende de la version de Python.