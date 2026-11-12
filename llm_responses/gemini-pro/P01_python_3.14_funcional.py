from functools import reduce

def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras, retorna un diccionario con la frecuencia 
    de cada palabra utilizando un enfoque estrictamente funcional e inmutable.
    """
    # Se utiliza reduce para acumular el conteo sin usar bucles con estado.
    # El operador de unión de diccionarios `|` crea un nuevo diccionario en 
    # cada evaluación, garantizando la inmutabilidad del acumulador (acc).
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

    # 2. Caso con una lista vacía (reduce devuelve el inicializador {} sin iterar)
    assert word_frequency([]) == {}, "Error en el caso de lista vacía"

    # 3. Caso con múltiples repeticiones para asegurar la pureza de la acumulación funcional
    assert word_frequency(["lambda", "map", "reduce", "lambda", "filter", "map"]) == {
        "lambda": 2, 
        "map": 2, 
        "reduce": 1,
        "filter": 1
    }, "Error en el conteo múltiple"
    
    print("Todos los tests funcionales pasaron correctamente.")

if __name__ == "__main__":
    test_word_frequency()