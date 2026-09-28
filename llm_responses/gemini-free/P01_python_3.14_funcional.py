from functools import reduce


def word_frequency(words: list[str]) -> dict[str, int]:
    # Paradigma funcional: acumulación inmutable con reduce y el operador de unión de diccionarios (|)
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

# Caso 3: Palabras únicas sin repeticiones
assert word_frequency(["functional", "python", "3.14"]) == {
    "functional": 1,
    "python": 1,
    "3.14": 1,
}