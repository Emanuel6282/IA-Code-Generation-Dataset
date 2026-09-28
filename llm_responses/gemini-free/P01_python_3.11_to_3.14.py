# Características de Python 3.14 (y evolución desde 3.9/3.11) utilizadas en este refactor:
# 1. Evaluacion diferida de anotaciones de tipo (PEP 649 / PEP 563 adaptado en 3.14):
#    A partir de Python 3.14, las anotaciones de tipo no se evalúan durante la definición de la función
#    sino cuando se inspeccionan, mejorando el rendimiento de carga e inicio del módulo.
# 2. Uso de colecciones integradas para genéricos (PEP 585):
#    Uso directo de `list[str]` y `dict[str, int]` eliminando las importaciones de `typing.List` y `typing.Dict`.
# 3. Métodos idiomáticos en diccionarios (`dict.get()`):
#    Simplificación de la búsqueda de claves dentro del bucle iterativo, aprovechando la aceleración
#    continuada del intérprete CPython (JIT / Tail Call / Specializing Adaptive Interpreter).


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
# 1. Eliminación del módulo `typing`: En versiones modernas de Python (estandarizado completamente en 3.14),
#    los tipos integrados como `list` y `dict` se usan directamente en el typado de variables y funciones.
# 2. Beneficios de PEP 649 (Deferred Evaluation of Annotations): Python 3.14 implementa la evaluación
#    diferida de type hints, reduciendo el overhead en tiempo de importación/ejecución al no evaluar
#    las expresiones de tipo a menos que se invoque `annotationlib`.
# 3. Simplificación con `dict.get()`: Sustituye las comprobaciones explícitas `if word in result` por una
#    expresión imperativa limpia, concisa y más rápida.