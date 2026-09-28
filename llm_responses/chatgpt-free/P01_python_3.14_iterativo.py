def word_frequency(words):
    frequency = {}

    for word in words:
        if word in frequency:
            frequency[word] += 1
        else:
            frequency[word] = 1

    return frequency


# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {
    "the": 2,
    "fox": 1
}

assert word_frequency([]) == {}

assert word_frequency(["hello", "world", "hello", "python", "world", "hello"]) == {
    "hello": 3,
    "world": 2,
    "python": 1
}