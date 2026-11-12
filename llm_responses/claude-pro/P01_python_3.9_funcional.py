"""Word Frequency - solución funcional para Python 3.9."""

from functools import reduce


# Python 3.9 (PEP 585): genéricos nativos en anotaciones, sin typing.Dict/List.
def _merge_counts(acc: dict[str, int], single: dict[str, int]) -> dict[str, int]:
    """Combina dos conteos sin mutar ninguno y devuelve un diccionario nuevo.

    Usa el operador de unión "|" (PEP 584, nuevo en Python 3.9), que crea
    un diccionario nuevo en lugar de modificar los existentes.
    """
    return acc | dict(map(lambda item: (item[0], acc.get(item[0], 0) + item[1]), single.items()))


def word_frequency(words: list[str]) -> dict[str, int]:
    """Retorna un diccionario que asocia cada palabra única con su número de apariciones."""
    # Cada palabra se transforma en un conteo unitario {palabra: 1} y luego se
    # pliegan todos con reduce, partiendo de un diccionario vacío.
    return reduce(_merge_counts, map(lambda word: {word: 1}, words), {})


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

    # Caso 2: lista vacía
    assert word_frequency([]) == {}

    # Caso 3: distingue mayúsculas/minúsculas; filter extrae solo las palabras
    # repetidas para verificar los conteos mayores a 1
    result = word_frequency(["a", "A", "a", "b", "a"])
    assert result == {"a": 3, "A": 1, "b": 1}
    assert dict(filter(lambda item: item[1] > 1, result.items())) == {"a": 3}

    print("Todas las pruebas pasaron.")