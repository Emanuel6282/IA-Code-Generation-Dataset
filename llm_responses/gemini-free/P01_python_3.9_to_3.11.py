# Características de Python 3.11+ utilizadas en este refactor:
# 1. PEP 585 (Anotaciones de tipo nativas):
#    Se eliminó 'from typing import List, Dict' para usar los tipos built-in 'list[str]'
#    y 'dict[str, int]', simplificando la sintaxis y reduciendo importaciones innecesarias.
# 2. Uso de dict.get() para un patrón más idiomatico y limpio dentro del bucle.
# 3. Optimización interna del intérprete CPython 3.11 (Specializing Adaptive Interpreter):
#    El código se mantiene claro e imperativo, aprovechando las mejoras de rendimiento
#    de CPython 3.11 para bucles y diccionarios sin recargar con abstracciones pesadas.


def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}
    for word in words:
        result[word] = result.get(word, 0) + 1
    return result


# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}

# --- Explicación de los cambios ---
# 1. Tipado nativo (PEP 585): En Python 3.9+ (y refinado como estándar en 3.11), `list` y `dict`
#    se pueden usar directamente en las anotaciones de tipo, eliminando la necesidad del módulo `typing`.
# 2. Simplificación con `dict.get()`: Sustituye el bloque `if/else` explícito. Si la clave no existe,
#    devuelve 0 y le suma 1, haciendo el código más idiomático y fácil de leer sin perder claridad.
# 3. Mantenimiento del estilo: Se conserva el paradigma imperativo/iterativo directo, que en Python 3.11
#    se beneficia sustancialmente del nuevo motor de ejecución adaptativo.