def binary_search(arr: list[int], target: int) -> int:
    """
    Busca un valor objetivo en un arreglo ordenado ascendentemente.
    Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
    
    Implementación estrictamente iterativa.
    """
    left: int = 0
    right: int = len(arr) - 1
    
    # Bucle iterativo clásico: mutamos left y right en cada paso
    while left <= right:
        mid: int = left + (right - left) // 2
        mid_val: int = arr[mid]
        
        if mid_val == target:
            return mid
        elif mid_val < target:
            left = mid + 1
        else:
            right = mid - 1
            
    return -1


# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base donde el elemento existe (retorna su índice)
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Error: El objetivo 5 debería estar en el índice 2"

# 2. Caso donde el elemento no existe (retorna -1)
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Error: El objetivo 4 no está, debería retornar -1"

# 3. Caso límite: el arreglo está vacío
assert binary_search([], 5) == -1, "Error: Arreglo vacío debería retornar -1"

print("Todos los tests iterativos pasaron correctamente.")