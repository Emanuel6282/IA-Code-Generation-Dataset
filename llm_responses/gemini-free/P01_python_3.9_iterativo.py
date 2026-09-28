from collections import defaultdict
from typing import List, Dict


def word_frequency(words: List[str]) -> Dict[str, int]:
    # Uso de defaultdict de la biblioteca estándar para el paradigma iterativo
    frequency: dict[str, int] = defaultdict(int)

    for word in words:
        frequency[word] += 1

    # Conversión explícita a un diccionario estándar
    return dict(frequency)


# --- Casos de prueba ---

# Caso 1: Ejemplo provisto con palabras repetidas
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

# Caso 2: Lista de entrada vacía
assert word_frequency([]) == {}

# Caso 3: Palabras únicas sin repeticiones
assert word_frequency(["python", "iterativo", "código"]) == {
    "python": 1,
    "iterativo": 1,
    "código": 1,
}