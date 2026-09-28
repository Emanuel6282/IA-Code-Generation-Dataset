def word_frequency(words: list[str]) -> dict[str, int]:
    # Paradigma iterativo: acumulación mutable mediante un bucle for
    counts: dict[str, int] = {}
    for word in words:
        counts[word] = counts.get(word, 0) + 1
    return counts


# --- Casos de prueba ---

# Caso 1: Ejemplo provisto con palabras repetidas
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

# Caso 2: Lista de entrada vacía (debe retornar un diccionario vacío)
assert word_frequency([]) == {}

# Caso 3: Palabras únicas y cadenas con números/versión
assert word_frequency(["python", "3.14", "iterativo", "python"]) == {
    "python": 2,
    "3.14": 1,
    "iterativo": 1,
}