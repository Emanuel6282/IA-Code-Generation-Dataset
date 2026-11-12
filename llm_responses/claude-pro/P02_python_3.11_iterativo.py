"""Binary Search - solución iterativa para Python 3.11."""


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

    # Se ejecutan todos los casos aunque alguno falle y se reportan juntos.
    failures: list[AssertionError] = []
    for name, arr, target, expected in test_cases:
        try:
            result = binary_search(arr, target)
            assert result == expected, f"esperado {expected}, obtenido {result}"
        except AssertionError as error:
            # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
            error.add_note(f"Caso fallido: {name} | arr={arr}, target={target}")
            failures.append(error)

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): agrupa todos los fallos
        raise ExceptionGroup("Fallaron casos de prueba de binary_search", failures)

    print("Todas las pruebas pasaron.")