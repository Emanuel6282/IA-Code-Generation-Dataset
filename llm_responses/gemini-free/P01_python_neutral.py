def word_frequency(words: list[str]) -> dict[str, int]:
    frequency = {}
    for word in words:
        frequency[word] = frequency.get(word, 0) + 1
    return frequency


# --- Casos de prueba ---

# Caso 1: Ejemplo proporcionado con palabras repetidas
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

# Caso 2: Lista vacía (debe retornar un diccionario vacío)
assert word_frequency([]) == {}

# Caso 3: Palabras sin repeticiones
assert word_frequency(["apple", "banana", "cherry"]) == {
    "apple": 1,
    "banana": 1,
    "cherry": 1,
}