def binary_search(arr: list[int], target: int) -> int:
    """Realiza una búsqueda binaria en una lista de enteros ordenada ascendentemente.

    Retorna el índice del valor objetivo si se encuentra; de lo contrario,
    retorna -1.
    """
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


# --- Casos de prueba ---

# Caso 1: El elemento existe en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Falló el Caso 1"

# Caso 2: El elemento no existe en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Falló el Caso 2"

# Caso 3: Lista vacía y búsqueda en los extremos (límite inferior/superior)
assert binary_search([], 10) == -1, "Falló el Caso 3a (Lista vacía)"
assert binary_search([10, 20, 30, 40], 10) == 0, (
    "Falló el Caso 3b (Primer elemento)"
)
assert binary_search([10, 20, 30, 40], 40) == 3, (
    "Falló el Caso 3c (Último elemento)"
)

print("¡Todos los casos de prueba pasaron correctamente!")