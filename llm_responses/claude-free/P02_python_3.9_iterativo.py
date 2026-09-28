def binary_search(arr, target):
    """
    Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
    retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
    retorna -1.

    Implementacion estrictamente iterativa (bucle while + variables
    mutables), sin usar bisect ni otra funcion de busqueda de la biblioteca
    estandar. Compatible con Python 3.9.

    Args:
        arr: Lista de enteros ordenada ascendentemente.
        target: Valor entero a buscar.

    Returns:
        El indice (int) donde se encuentra target dentro de arr, o -1 si
        target no esta presente.
    """
    izquierda = 0
    derecha = len(arr) - 1

    while izquierda <= derecha:
        medio = (izquierda + derecha) // 2

        if arr[medio] == target:
            return medio
        elif arr[medio] < target:
            izquierda = medio + 1
        else:
            derecha = medio - 1

    return -1


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, objetivo presente en el medio
    assert binary_search([1, 3, 5, 7, 9], 5) == 2

    # Caso 2: ejemplo del enunciado, objetivo ausente
    assert binary_search([1, 3, 5, 7, 9], 4) == -1

    # Caso 3: arreglo vacio -> siempre -1
    assert binary_search([], 1) == -1

    print("Todos los casos de prueba pasaron correctamente.")