from functools import reduce


def word_frequency(words):
    def add_word(frequency, word):
        return {
            **frequency,
            word: frequency.get(word, 0) + 1
        }

    # map: normaliza cada elemento a un string
    mapped_words = map(lambda word: str(word), words)

    # filter: elimina palabras vacías
    filtered_words = filter(lambda word: word != "", mapped_words)

    # reduce: construye el diccionario de frecuencias
    return reduce(add_word, filtered_words, {})


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