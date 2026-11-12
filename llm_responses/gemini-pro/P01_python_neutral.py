def word_frequency(words):
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra.
    """
    freq = {}
    for word in words:
        freq[word] = freq.get(word, 0) + 1
    return freq


# --- Casos de prueba ---

# 1. Caso base del ejemplo
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}, "Error en el caso base"

# 2. Caso con una lista vacía (debe retornar un diccionario vacío)
assert word_frequency([]) == {}, "Error en el caso de lista vacía"

# 3. Caso con múltiples repeticiones y palabras únicas
assert word_frequency(["a", "b", "a", "c", "b", "a"]) == {"a": 3, "b": 2, "c": 1}, "Error en el caso de repeticiones múltiples"

print("Todos los tests pasaron correctamente.")