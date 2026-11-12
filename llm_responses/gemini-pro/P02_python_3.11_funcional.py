def binary_search(arr: list[int], target: int) -> int:
    """
    Busca un valor objetivo en un arreglo ordenado ascendentemente.
    Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
    
    Implementación estrictamente funcional: sin bucles, sin reasignación 
    de variables, apoyándose en pattern matching y recursión de cola emulada.
    """
    
    # Función pura auxiliar encapsulada
    def search(left: int, right: int) -> int:
        # Condición base (evaluación inmutable)
        if left > right:
            return -1
            
        mid: int = left + (right - left) // 2
        
        # Uso de Pattern Matching (declarativo) en lugar de if/elif mutables.
        # Esto emula el 'pattern matching' funcional de lenguajes como Scala/Elm.
        match arr[mid]:
            case val if val == target:
                return mid
            case val if val < target:
                return search(mid + 1, right)
            case _:
                return search(left, mid - 1)

    # Invocación inicial
    return search(0, len(arr) - 1)


# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base donde el elemento existe en la primera mitad
assert binary_search([1, 3, 5, 7, 9], 3) == 1, "Error: El objetivo 3 debería estar en el índice 1"

# 2. Caso donde el elemento no existe en el arreglo
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Error: El objetivo 4 no está, debería retornar -1"

# 3. Caso límite: el arreglo está vacío
assert binary_search([], 5) == -1, "Error: Arreglo vacío debería retornar -1"

print("Todos los tests funcionales en Python 3.11 pasaron correctamente.")