def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra de forma puramente iterativa.
    """
    freq: dict[str, int] = {}
    
    # Uso estricto de bucle iterativo con una variable mutable
    for word in words:
        if word in freq:
            freq[word] += 1
        else:
            freq[word] = 1
            
    return freq

# --- Casos de prueba ---

# 1. Caso base del ejemplo proporcionado
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}, "Error en el caso base"

# 2. Caso con una lista vacía
assert word_frequency([]) == {}, "Error en el caso de lista vacía"

# 3. Caso con repeticiones múltiples y palabras únicas
assert word_frequency(["python", "java", "python", "c", "java", "python"]) == {"python": 3, "java": 2, "c": 1}, "Error en repeticiones múltiples"

print("Todos los tests pasaron correctamente.")