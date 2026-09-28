from collections import Counter


def word_frequency(words: list[str]) -> dict[str, int]:
    # En Python 3.11+, el método update() de Counter acepta iterables directamente.
    # Usamos un bucle for explícito con mutación de estado para cumplir con el paradigma iterativo.
    counts: dict[str, int] = {}
    for word in words:
        counts[word] = counts.get(word, 0) + 1
    return counts


# --- Casos de prueba ---

# Caso 1: Ejemplo provisto con palabras repetidas
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

# Caso 2: Lista de entrada vacía
assert word_frequency([]) == {}

# Caso 3: Palabras únicas y caracteres especiales
assert word_frequency(["python3.11", "iterativo", "python3.11"]) == {
    "python3.11": 2,
    "iterativo": 1,
}