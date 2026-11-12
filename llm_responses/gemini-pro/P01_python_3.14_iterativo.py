def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra utilizando un enfoque estrictamente iterativo.
    """
    freq: dict[str, int] = {}
    
    # Bucle iterativo clásico mutando el estado del diccionario 'freq'
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
    # 1. Caso base del ejemplo proporcionado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}, "Error en el caso base"

    # 2. Caso con una lista vacía (debe retornar un diccionario vacío)
    assert word_frequency([]) == {}, "Error en el caso de lista vacía"

    # 3. Caso con múltiples repeticiones y palabras únicas
    assert word_frequency(["alpha", "beta", "alpha", "gamma", "beta", "alpha"]) == {
        "alpha": 3, 
        "beta": 2, 
        "gamma": 1
    }, "Error en repeticiones múltiples"
    
    print("Todos los tests iterativos pasaron correctamente.")

if __name__ == "__main__":
    test_word_frequency()