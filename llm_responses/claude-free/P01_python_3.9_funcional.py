from functools import reduce


def word_frequency(words):
    """
    Dada una lista de palabras (strings), retorna un diccionario que asocia
    cada palabra unica con el numero de veces que aparece en la lista.

    Implementacion en paradigma funcional: usa functools.reduce para
    acumular las frecuencias de forma inmutable (en cada paso se construye
    un diccionario nuevo, sin mutar el acumulador previo), sin bucles for/while
    con estado mutable. Solo biblioteca estandar (functools). Compatible con
    Python 3.9.

    Args:
        words: Lista de strings.

    Returns:
        Un diccionario {palabra: frecuencia}. Si la lista esta vacia,
        retorna un diccionario vacio.
    """
    return reduce(
        lambda acumulado, palabra: {**acumulado, palabra: acumulado.get(palabra, 0) + 1},
        words,
        {}
    )


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}

    # Caso 2: lista vacia -> diccionario vacio
    assert word_frequency([]) == {}

    # Caso 3: todas las palabras distintas
    assert word_frequency(["a", "b", "c"]) == {"a": 1, "b": 1, "c": 1}

    print("Todos los casos de prueba pasaron correctamente.")