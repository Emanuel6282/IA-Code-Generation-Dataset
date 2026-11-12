# P01: Word Frequency — Python 3.14
#
# Características utilizadas:
# 1. Tipos genéricos nativos (list[str] y dict[str, int]):
#    mejoran la legibilidad y eliminan importaciones de typing.
#    Disponibles desde Python 3.9.
#
# 2. Anotaciones de tipos modernas:
#    Python 3.14 incorpora evaluación diferida de anotaciones
#    (PEP 649 y PEP 749), facilitando su manejo en tiempo
#    de ejecución sin modificar la lógica del algoritmo.
#
# 3. Optimizaciones internas del intérprete:
#    Python 3.14 incorpora mejoras de rendimiento que pueden
#    beneficiar al código sin cambiar su implementación.


def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}

    for word in words:
        if word in result:
            result[word] += 1
        else:
            result[word] = 1

    return result


# Casos de prueba originales
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}


# Cambios realizados:
#
# 1. List[str] -> list[str]:
#    utiliza tipos genéricos nativos, evitando typing.List.
#
# 2. Dict[str, int] -> dict[str, int]:
#    simplifica la anotación del tipo de retorno.
#
# 3. Eliminación de "from typing import List, Dict":
#    reduce importaciones innecesarias.
#
# 4. Anotación explícita de result:
#    facilita el análisis estático y documenta el acumulador.
#
# 5. result[word] = result[word] + 1 -> result[word] += 1:
#    utiliza una asignación aumentada más concisa e idiomática.
#
# 6. Conservación del algoritmo iterativo:
#    mantiene la misma lógica y los mismos resultados.
#
# 7. Compatibilidad con Python 3.14:
#    aprovecha las mejoras del intérprete sin introducir
#    complejidad innecesaria.