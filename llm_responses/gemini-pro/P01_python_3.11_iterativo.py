def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra de forma iterativa.
    """
    freq: dict[str, int] = {}
    
    # Bucle puramente iterativo: mutamos el diccionario actualizando el estado
    for word in words:
        if word in freq:
            freq[word] += 1
        else:
            freq[word] = 1
            
    return freq


# ==========================================
# CASOS DE PRUEBA
# ==========================================

def test_word_frequency():
    # 1. Caso base (ejemplo proporcionado)
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}, "Error en el caso base"

    # 2. Caso con una lista vacía
    assert word_frequency([]) == {}, "Error en el caso de lista vacía"

    # 3. Caso con repeticiones múltiples y varias palabras
    assert word_frequency(["apple", "banana", "apple", "orange", "banana", "apple"]) == {
        "apple": 3, 
        "banana": 2, 
        "orange": 1
    }, "Error en repeticiones múltiples"
    
    print("Todos los tests pasaron correctamente.")

if __name__ == "__main__":
    test_word_frequency()