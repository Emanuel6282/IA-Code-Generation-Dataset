def binary_search(arr, target):
    """
    Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
    retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
    retorna -1.

    Implementacion en paradigma funcional: sin bucles for/while ni variables
    mutables. El "estado" (los limites izquierda/derecha) se modela como
    parametros inmutables de una funcion auxiliar recursiva, en lugar de
    variables reasignadas. Compatible con Python 3.9. Solo biblioteca
    estandar (de hecho, no se necesita ningun import).

    Args:
        arr: Lista de enteros ordenada ascendentemente.
        target: Valor entero a buscar.

    Returns:
        El indice (int) donde se encuentra target dentro de arr, o -1 si
        target no esta presente.
    """

    def buscar(izquierda, derecha):
        # Caso base: rango vacio -> no encontrado
        if izquierda > derecha:
            return -1

        medio = (izquierda + derecha) // 2

        if arr[medio] == target:
            return medio
        elif arr[medio] < target:
            return buscar(medio + 1, derecha)
        else:
            return buscar(izquierda, medio - 1)

    return buscar(0, len(arr) - 1)


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, objetivo presente en el medio
    assert binary_search([1, 3, 5, 7, 9], 5) == 2

    # Caso 2: ejemplo del enunciado, objetivo ausente
    assert binary_search([1, 3, 5, 7, 9], 4) == -1

    # Caso 3: arreglo vacio -> siempre -1
    assert binary_search([], 1) == -1

    print("Todos los casos de prueba pasaron correctamente.")