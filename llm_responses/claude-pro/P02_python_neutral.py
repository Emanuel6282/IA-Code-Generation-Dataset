def binary_search(arr, target):
    """Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está."""
    low, high = 0, len(arr) - 1
    while low <= high:
        mid = (low + high) // 2
        if arr[mid] == target:
            return mid
        if arr[mid] < target:
            low = mid + 1   # el objetivo solo puede estar en la mitad derecha
        else:
            high = mid - 1  # el objetivo solo puede estar en la mitad izquierda
    return -1


if __name__ == "__main__":
    # Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
    assert binary_search([1, 3, 5, 7, 9], 5) == 2

    # Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
    assert binary_search([1, 3, 5, 7, 9], 4) == -1

    # Caso 3: arreglo vacío y extremos del arreglo
    assert binary_search([], 1) == -1
    assert binary_search([1, 3, 5, 7, 9], 1) == 0
    assert binary_search([1, 3, 5, 7, 9], 9) == 4

    print("Todas las pruebas pasaron.")