# CARACTERISTICAS DE PYTHON 3.14 UTILIZADAS Y POR QUE:
# 1. Tipado nativo estandarizado (PEP 585): Se usan `list[str]` y `dict[str, int]`.
#    En Python 3.14, la importación de colecciones desde el módulo `typing` se 
#    considera completamente deprecada/obsoleta para código nuevo.
# 2. Optimización JIT (Just-In-Time) de CPython: Python 3.13 y 3.14 consolidan
#    el compilador JIT (Tier 2 optimizer). Esto hace que las operaciones 
#    de diccionario nativas integradas (como `.get()`) y los bucles `for` simples 
#    sean extremadamente rápidos, haciendo innecesarias optimizaciones complejas.

def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}
    for word in words:
        # Sustituimos el if/else por el idiomático .get()
        result[word] = result.get(word, 0) + 1
    return result

# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}

print("Todos los tests pasaron correctamente.")

# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.14:
# 
# 1. Eliminación de `from typing import List, Dict`: El ecosistema actual de Python 3.14 
#    adopta los genéricos de tipos integrados en minúscula (`list`, `dict`). Esto no solo 
#    limpia la cabecera del archivo, sino que mejora el tiempo de importación al no tener 
#    que cargar el módulo `typing` en memoria.
#
# 2. Anotación en línea de la variable `result`: Al usar `result: dict[str, int] = {}`, 
#    brindamos máxima claridad a los linters modernos y type-checkers (como mypy o pyright), 
#    los cuales son herramientas de uso casi obligatorio en el desarrollo actual.
#
# 3. Refactorización a `dict.get()`: El bloque de 4 líneas iterativas (`if word in result...`)
#    se contrae a una sola línea. Esta es la forma más "pitónica" de actualizar contadores 
#    sin usar bibliotecas externas, y en Python 3.14 aprovecha la optimización de bytecode 
#    y el JIT para ejecutarse a velocidades cercanas a C, sin penalizaciones por llamadas a métodos.