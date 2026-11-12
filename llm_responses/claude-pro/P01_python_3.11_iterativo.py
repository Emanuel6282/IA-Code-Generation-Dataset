"""Word Frequency - solución iterativa para Python 3.11."""


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
    test_cases: list[tuple[str, list[str], dict[str, int]]] = [
        # Caso 1: ejemplo del enunciado
        ("ejemplo del enunciado", ["the", "fox", "the"], {"the": 2, "fox": 1}),
        # Caso 2: lista vacía
        ("lista vacía", [], {}),
        # Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        ("mayúsculas y repeticiones", ["a", "A", "a", "b", "a"], {"a": 3, "A": 1, "b": 1}),
    ]

    # Se ejecutan todos los casos aunque alguno falle y se reportan juntos.
    failures: list[AssertionError] = []
    for name, words, expected in test_cases:
        try:
            result = word_frequency(words)
            assert result == expected, f"esperado {expected}, obtenido {result}"
        except AssertionError as error:
            # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
            error.add_note(f"Caso fallido: {name} | entrada: {words}")
            failures.append(error)

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): agrupa todos los fallos
        raise ExceptionGroup("Fallaron casos de prueba de word_frequency", failures)

    print("Todas las pruebas pasaron.")