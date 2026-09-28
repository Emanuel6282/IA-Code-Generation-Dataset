from typing import List


def binary_search(arr: List[int], target: int) -> int:
    # Paradigma iterativo: control de punteros mediante bucle while con estado mutable
    left = 0
    right = len(arr) - 1

    while left <= right:
        mid = (left + right) // 2

        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1


# --- Casos de prueba (Python 3.9) ---

# Caso 1: Elemento presente en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Falló el Caso 1"

# Caso 2: Elemento no presente en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Falló el Caso 2"

# Caso 3: Lista vacía y elementos en bordes/extremos
assert binary_search([], 10) == -1, "Falló el Caso 3 (Lista vacía)"
assert binary_search([10, 20, 30, 40], 10) == 0, (
    "Falló el Caso 3 (Primer elemento)"
)
assert binary_search([10, 20, 30, 40], 40) == 3, (
    "Falló el Caso 3 (Último elemento)"
)

print("¡Todos los casos de prueba pasaron correctamente!")