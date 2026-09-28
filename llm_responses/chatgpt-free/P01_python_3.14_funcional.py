from functools import reduce


def word_frequency(words):
    # map(): procesa las palabras de entrada
    mapped_words = map(lambda word: word, words)

    # filter(): conserva únicamente strings
    filtered_words = filter(lambda word: isinstance(word, str), mapped_words)

    def add_word(frequency, word):
        # Se crea un nuevo diccionario en lugar de modificar el anterior.
        return {
            **frequency,
            word: frequency.get(word, 0) + 1
        }

    # reduce(): acumula las frecuencias de forma inmutable.
    return reduce(add_word, filtered_words, {})


# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {
    "the": 2,
    "fox": 1
}

assert word_frequency([]) == {}

assert word_frequency(
    ["hello", "world", "hello", "python", "world", "hello"]
) == {
    "hello": 3,
    "world": 2,
    "python": 1
}