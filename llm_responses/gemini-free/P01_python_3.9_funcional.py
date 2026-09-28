from functools import reduce


def word_frequency(words: list[str]) -> dict[str, int]:
    # Paradigma funcional: uso de functools.reduce, inmutabilidad y el operador | (unión de dicts) introducido en Python 3.9
    return reduce(
        lambda acc, word: acc | {word: acc.get(word, 0) + 1},
        words,
        {},
    )


# --- Casos de prueba ---

# Caso 1: Ejemplo provisto con palabras repetidas
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

# Caso 2: Lista de entrada vacía
assert word_frequency([]) == {}

# Caso 3: Lista con palabras únicas
assert word_frequency(["lambda", "reduce", "map"]) == {
    "lambda": 1,
    "reduce": 1,
    "map": 1,
}