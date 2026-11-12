def word_frequency(words):
    """Retorna un diccionario que asocia cada palabra única con su número de apariciones."""
    frequencies = {}
    for word in words:
        frequencies[word] = frequencies.get(word, 0) + 1
    return frequencies


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

    # Caso 2: lista vacía
    assert word_frequency([]) == {}

    # Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
    assert word_frequency(["a", "A", "a", "b", "a"]) == {"a": 3, "A": 1, "b": 1}

    print("Todas las pruebas pasaron.")