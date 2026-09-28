def word_frequency(words: list[str]) -> dict[str, int]:
    """
    Dada una lista de palabras (strings), retorna un diccionario que asocia
    cada palabra unica con el numero de veces que aparece en la lista.

    Implementacion estrictamente iterativa (bucle for + variables mutables).
    Solo biblioteca estandar. Compatible con Python 3.14.

    Args:
        words: Lista de strings.

    Returns:
        Un diccionario {palabra: frecuencia}. Si la lista esta vacia,
        retorna un diccionario vacio.
    """
    frecuencias: dict[str, int] = {}

    for palabra in words:
        if palabra in frecuencias:
            frecuencias[palabra] = frecuencias[palabra] + 1
        else:
            frecuencias[palabra] = 1

    return frecuencias


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

    # Caso 2: lista vacia -> diccionario vacio
    assert word_frequency([]) == {}

    # Caso 3: todas las palabras distintas
    assert word_frequency(["a", "b", "c"]) == {"a": 1, "b": 1, "c": 1}

    print("Todos los casos de prueba pasaron correctamente.")