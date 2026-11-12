from functools import reduce

def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra utilizando un enfoque estrictamente funcional e inmutable.
    """
    # reduce aplica la función lambda de forma acumulativa de izquierda a derecha.
    # El operador `|` (novedad en Python 3.9) retorna un NUEVO diccionario, 
    # evitando la mutación del acumulador (acc).
    return reduce(
        lambda acc, word: acc | {word: acc.get(word, 0) + 1},
        words,
        {}
    )


# --- Casos de prueba ---

# 1. Caso base del ejemplo proporcionado
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}, "Error en el caso base"

# 2. Caso con una lista vacía (reduce retorna el inicializador {})
assert word_frequency([]) == {}, "Error en el caso de lista vacía"

# 3. Caso con múltiples palabras para verificar la acumulación funcional
assert word_frequency(["funcional", "python", "funcional", "puro"]) == {"funcional": 2, "python": 1, "puro": 1}, "Error en palabras múltiples"

print("Todos los tests pasaron correctamente.")