def binary_search(arr: list[int], target: int) -> int:
    # Paradigma iterativo: control de punteros mediante bucle while con estado mutable
    left: int = 0
    right: int = len(arr) - 1

    while left <= right:
        mid: int = (left + right) // 2
        
        # En Python 3.11, la sentencia match-case permite estructurar el control
        # de flujo de comparaciones de forma limpia y expresiva
        match arr[mid] == target:
            case True:
                return mid
            case False if arr[mid] < target:
                left = mid + 1
            case False:
                right = mid - 1

    return -1


# --- Casos de prueba (Python 3.11) ---

# Caso 1: Elemento presente en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Falló el Caso 1"

# Caso 2: Elemento no presente en la lista (ejemplo provisto)
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Falló el Caso 2"

# Caso 3: Lista vacía y búsqueda en los extremos
assert binary_search([], 10) == -1, "Falló el Caso 3 (Lista vacía)"
assert binary_search([10, 20, 30, 40], 10) == 0, (
    "Falló el Caso 3 (Primer elemento)"
)
assert binary_search([10, 20, 30, 40], 40) == 3, (
    "Falló el Caso 3 (Último elemento)"
)

print("¡Todos los casos de prueba pasaron correctamente!")