# P01: Word Frequency — Python 3.11
#
# Características de Python 3.11 utilizadas:
# 1. Tipado genérico incorporado: list[str] y dict[str, int].
#    Mejora la legibilidad y evita importar List y Dict desde typing.
# 2. Anotaciones de tipo más simples y modernas.
#
# El algoritmo se mantiene iterativo para conservar exactamente
# la lógica y el comportamiento del código original.


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
# 1. Se reemplazó List[str] por list[str].
#    Python 3.9+ permite utilizar tipos genéricos incorporados
#    directamente, evitando typing.List.
#
# 2. Se reemplazó Dict[str, int] por dict[str, int].
#    Esto hace las anotaciones más concisas y modernas.
#
# 3. Se añadió la anotación de tipo al diccionario result:
#    result: dict[str, int] = {}.
#    Esto documenta explícitamente el tipo del acumulador.
#
# 4. Se simplificó:
#    result[word] = result[word] + 1
#    a:
#    result[word] += 1
#    El comportamiento es idéntico y la segunda forma es idiomática
#    y más legible en Python.
#
# 5. Se mantuvo el algoritmo iterativo, la función word_frequency,
#    los mismos casos de prueba y los mismos resultados.