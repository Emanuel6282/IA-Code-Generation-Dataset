from functools import reduce


def word_frequency(words: list[str]) -> dict[str, int]:
    return reduce(
        lambda acc, word: {
            **acc,
            word: acc.get(word, 0) + 1
        },
        map(lambda word: word, filter(lambda word: True, words)),
        {}
    )


# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["apple", "banana", "apple"]) == {"apple": 2, "banana": 1}

print("Todos los tests pasaron correctamente.")