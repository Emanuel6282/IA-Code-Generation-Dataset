def word_frequency(words: list[str]) -> dict[str, int]:
    frequency = {}

    for word in words:
        if word in frequency:
            frequency[word] += 1
        else:
            frequency[word] = 1

    return frequency


# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["apple", "banana", "apple"]) == {"apple": 2, "banana": 1}

print("Todos los tests pasaron correctamente.")