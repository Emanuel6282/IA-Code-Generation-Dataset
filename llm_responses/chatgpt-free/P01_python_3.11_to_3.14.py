# P01: Word Frequency — Python 3.14
#
# Características modernas utilizadas:
# 1. Tipos genéricos incorporados (list[str] y dict[str, int]),
#    disponibles desde Python 3.9. En Python 3.14 siguen siendo
#    la forma idiomática de expresar estos tipos.
# 2. Anotaciones de tipo integradas, sin necesidad de importar
#    List y Dict desde typing.
#
# Python 3.14 no incorpora una característica específica para este
# algoritmo que mejore su rendimiento o legibilidad de forma
# significativa, por lo que se conserva la implementación iterativa.


def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}

    for word in words:
        if word in result:
            result[word] += 1
        else:
            result[word] = 1

    return result


# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}


# Cambios realizados:
#
# 1. Se eliminó:
#       from typing import List, Dict
#
#    y se utilizaron directamente:
#       list[str]
#       dict[str, int]
#
#    Esta sintaxis es más moderna y legible y es compatible con
#    Python 3.14.
#
# 2. Se añadió la anotación:
#       result: dict[str, int] = {}
#
#    Esto hace explícito que el diccionario contiene palabras como
#    claves y enteros como valores.
#
# 3. Se simplificó:
#       result[word] = result[word] + 1
#
#    a:
#       result[word] += 1
#
#    Ambas expresiones producen exactamente el mismo resultado,
#    pero += es más idiomático y legible en Python.
#
# 4. Se mantuvo el bucle for y el diccionario mutable porque el
#    comportamiento original es iterativo e imperativo. No se
#    introdujeron map(), filter(), reduce() ni otras abstracciones
#    que cambiarían innecesariamente el estilo del código.
#
# 5. Se conservaron exactamente los tres casos de prueba y sus
#    resultados.
#
# 6. La complejidad permanece:
#       Tiempo: O(n) en promedio.
#       Espacio: O(k), donde k es el número de palabras únicas.
#
# 7. No se utiliza ninguna librería externa ni ningún recurso
#    específico que sea incompatible con Python 3.14.