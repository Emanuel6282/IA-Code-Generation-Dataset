def word_frequency(words: list[str]) -> dict[str, int]:
    frequency: dict[str, int] = {}

    for word in words:
        if word in frequency:
            frequency[word] += 1
        else:
            frequency[word] = 1

    return frequency


# Caso de prueba 1: Palabras repetidas
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

# Caso de prueba 2: Lista vacía
assert word_frequency([]) == {}

# Caso de prueba 3: Múltiples repeticiones
assert word_frequency(["apple", "banana", "apple", "apple"]) == {
    "apple": 3,
    "banana": 1
}

print("Todos los tests pasaron correctamente.")