def binary_search(arr, target):
    """
    Busca un valor objetivo en un arreglo ordenado ascendentemente.
    Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
    """
    left = 0
    right = len(arr) - 1
    
    while left <= right:
        # Calcular el punto medio de forma segura para evitar overflow en arreglos muy grandes
        mid = left + (right - left) // 2
        
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
            
    return -1


# Casos de prueba
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Fallo: el objetivo 5 debería estar en el índice 2"
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Fallo: el objetivo 4 no está, debería retornar -1"
assert binary_search([10, 20, 30, 40, 50, 60], 60) == 5, "Fallo: el objetivo 60 debería estar en el índice 5"

print("Todos los casos de prueba pasaron correctamente.")