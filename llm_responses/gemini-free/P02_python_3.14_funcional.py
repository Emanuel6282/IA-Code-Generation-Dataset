from collections.abc import Sequence


def binary_search(arr: Sequence[int], target: int) -> int:
    """Búsqueda binaria funcional en Python 3.14 usando recursión pura e inmutabilidad."""

    def search_range(left: int, right: int) -> int:
        if left > right:
            return -1

        mid: int = (left + right) // 2

        # Pattern matching (match/case) con guards para ramificación funcional limpia
        match target - arr[mid]:
            case 0:
                return mid
            case diff if diff > 0:
                return search_range(mid + 1, right)
            case _:
                return search_range(left, mid - 1)

    return search_range(0, len(arr) - 1)


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