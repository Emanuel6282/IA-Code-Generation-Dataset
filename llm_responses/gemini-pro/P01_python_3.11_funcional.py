from functools import reduce

def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra utilizando un enfoque estrictamente funcional e inmutable.
    """
    # reduce aplica la función lambda de forma acumulativa.
    # El operador `|` retorna un NUEVO diccionario en cada iteración, 
    # evitando la mutación de estado del acumulador (acc).
    return reduce(
        lambda acc, word: acc | {word: acc.get(word, 0) + 1},
        words,
        {}
    )


# ==========================================
# CASOS DE PRUEBA
# ==========================================

def test_word_frequency():
    # 1. Caso base del ejemplo proporcionado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}, "Error en el caso base"

    # 2. Caso con una lista vacía (reduce devuelve el valor inicializador {})
    assert word_frequency([]) == {}, "Error en el caso de lista vacía"

    # 3. Caso con repeticiones múltiples de diferentes elementos
    assert word_frequency(["funcional", "python", "funcional", "inmutable", "python", "funcional"]) == {
        "funcional": 3, 
        "python": 2, 
        "inmutable": 1
    }, "Error en palabras múltiples"
    
    print("Todos los tests funcionales pasaron correctamente.")

if __name__ == "__main__":
    test_word_frequency()