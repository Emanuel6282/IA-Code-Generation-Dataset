"""Word Frequency - solución iterativa para Python 3.9."""


# Python 3.9 (PEP 585): genéricos nativos en anotaciones, sin importar typing.List/Dict.
def word_frequency(words: list[str]) -> dict[str, int]:
    """Retorna un diccionario que asocia cada palabra única con su número de apariciones."""
    frequencies: dict[str, int] = {}
    for word in words:
        if word in frequencies:
            frequencies[word] += 1
        else:
            frequencies[word] = 1
    return frequencies


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

    # Caso 2: lista vacía
    assert word_frequency([]) == {}

    # Caso 3: distingue mayúsculas/minúsculas; se arma el esperado con el
    # operador de unión de diccionarios "|" (PEP 584, nuevo en Python 3.9)
    expected: dict[str, int] = {"a": 3} | {"A": 1, "b": 1}
    assert word_frequency(["a", "A", "a", "b", "a"]) == expected

    print("Todas las pruebas pasaron.")