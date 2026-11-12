def binary_search(arr: list[int], target: int) -> int:
    """
    Busca un valor objetivo en un arreglo ordenado ascendentemente.
    Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
    
    Implementación estrictamente funcional: sin bucles, sin variables
    mutables, basada completamente en expresiones y recursividad pura.
    """
    
    # Función auxiliar pura para mantener la firma original intacta
    def search(left: int, right: int) -> int:
        # Condición de corte (caso base)
        if left > right:
            return -1
            
        mid: int = left + (right - left) // 2
        
        # Evaluación declarativa sin sentencias imperativas de mutación
        return mid if arr[mid] == target else (
            search(mid + 1, right) if arr[mid] < target else search(left, mid - 1)
        )

    # Invocación inicial
    return search(0, len(arr) - 1)


# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base donde el elemento existe
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Error: El objetivo 5 debería estar en el índice 2"

# 2. Caso donde el elemento no existe en el arreglo
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Error: El objetivo 4 no está, debería retornar -1"

# 3. Caso límite: el arreglo está vacío
assert binary_search([], 5) == -1, "Error: Arreglo vacío debería retornar -1"

print("Todos los tests funcionales pasaron correctamente.")