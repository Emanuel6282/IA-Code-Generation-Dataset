def binary_search(arr: list[int], target: int) -> int:
    """Búsqueda binaria iterativa en Python 3.14.

    Aprovecha anotaciones de tipos nativas (PEP 585 / PEP 604) y evaluación diferida.
    """
    left: int = 0
    right: int = len(arr) - 1

    # Control del rango de búsqueda mediante bucle while con estado mutable
    while left <= right:
        mid: int = (left + right) // 2

        match target - arr[mid]:
            case 0:
                return mid
            case diff if diff > 0:
                left = mid + 1
            case _:
                right = mid - 1

    return -1


# --- Casos de prueba (Python 3.14) ---

# Caso 1: Elemento presente en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Falló el Caso 1"

# Caso 2: Elemento no presente en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Falló el Caso 2"

# Caso 3: Lista vacía y bordes/extremos
assert binary_search([], 10) == -1, "Falló el Caso 3 (Lista vacía)"
assert binary_search([10, 20, 30, 40], 10) == 0, (
    "Falló el Caso 3 (Primer elemento)"
)
assert binary_search([10, 20, 30, 40], 40) == 3, (
    "Falló el Caso 3 (Último elemento)"
)

print("¡Todos los casos de prueba pasaron correctamente!")