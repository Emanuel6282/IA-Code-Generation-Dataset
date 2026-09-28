def flatten(nested_list):
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista
    plana con todos los enteros en el mismo orden de aparicion.

    Maneja cualquier nivel de anidamiento mediante recursion: para cada
    elemento, si es una lista, se aplana recursivamente y se extienden sus
    resultados; si es un entero, se agrega directamente.

    Args:
        nested_list: Lista que puede contener enteros y/o otras listas
                      anidadas a cualquier profundidad.

    Returns:
        Una lista plana (list[int]) con todos los enteros en el mismo orden
        de aparicion. Si la lista de entrada esta vacia, retorna una lista
        vacia.
    """
    resultado = []
    for elemento in nested_list:
        if isinstance(elemento, list):
            resultado.extend(flatten(elemento))
        else:
            resultado.append(elemento)
    return resultado


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, anidamiento de varios niveles
    assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

    # Caso 2: lista vacia -> lista vacia
    assert flatten([]) == []

    # Caso 3: anidamiento profundo con listas vacias intercaladas
    assert flatten([1, [], [2, [3, [4, [5]], []], 6]]) == [1, 2, 3, 4, 5, 6]

    print("Todos los casos de prueba pasaron correctamente.")