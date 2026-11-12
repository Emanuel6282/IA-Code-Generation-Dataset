# Uso de la sintaxis de genéricos (Type Parameter Syntax - PEP 695) 
# consolidada en las versiones recientes para un tipado estricto y limpio.
def binary_search[T: int](arr: list[T], target: T) -> int:
    """
    Busca un valor objetivo en un arreglo ordenado ascendentemente.
    Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
    
    Implementación estrictamente iterativa.
    """
    left: int = 0
    right: int = len(arr) - 1
    
    # Bucle iterativo clásico: mutamos el estado de left y right explícitamente
    while left <= right:
        # En Python no hay desbordamiento de enteros (integer overflow),
        # por lo que (left + right) // 2 es matemáticamente seguro y más rápido.
        mid: int = (left + right) // 2
        mid_val: T = arr[mid]
        
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

# 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Error: El objetivo 5 debería estar en el índice 2"

# 2. Caso del ejemplo: el elemento no existe y debe retornar -1
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Error: El objetivo 4 no está, debería retornar -1"

# 3. Caso límite: el arreglo está vacío
assert binary_search([], 5) == -1, "Error: Arreglo vacío debería retornar -1"

print("Todos los tests iterativos en Python 3.14 pasaron correctamente.")