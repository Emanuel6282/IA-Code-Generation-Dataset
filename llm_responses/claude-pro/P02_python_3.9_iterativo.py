"""Binary Search - solución iterativa para Python 3.9."""


# Python 3.9 (PEP 585): genéricos nativos en anotaciones, sin importar typing.List.
def binary_search(arr: list[int], target: int) -> int:
    """Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está."""
    low = 0
    high = len(arr) - 1
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
    test_cases: list[tuple[str, list[int], int, int]] = [
        # Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        ("objetivo presente", [1, 3, 5, 7, 9], 5, 2),
        # Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        ("objetivo ausente", [1, 3, 5, 7, 9], 4, -1),
        # Caso 3: arreglo vacío
        ("arreglo vacío", [], 1, -1),
    ]

    for name, arr, target, expected in test_cases:
        result = binary_search(arr, target)
        assert result == expected, f"{name}: esperado {expected}, obtenido {result}"

    print("Todas las pruebas pasaron.")