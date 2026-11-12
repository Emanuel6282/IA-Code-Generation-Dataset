# Uso de la sintaxis de genéricos (Type Parameters - PEP 695) introducida 
# en versiones recientes para un tipado estático, limpio y moderno.
def binary_search[T: int](arr: list[T], target: T) -> int:
    """
    Busca un valor objetivo en un arreglo ordenado ascendentemente.
    Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
    
    Implementación estrictamente funcional: sin bucles iterativos,
    sin estado mutable, empleando recursión pura y pattern matching.
    """
    
    # Función pura auxiliar para mantener intacta la firma principal
    def search(left: int, right: int) -> int:
        # Condición de corte (evaluación declarativa base)
        if left > right:
            return -1
            
        # Cálculo inmutable del punto medio
        mid: int = (left + right) // 2
        
        # Structural Pattern Matching: Evaluación declarativa y pura
        # en lugar de bloques if/elif/else condicionales y mutables.
        match arr[mid]:
            case val if val == target:
                return mid
            case val if val < target:
                # Retorno directo del nuevo marco en la pila (inmutabilidad pura)
                return search(mid + 1, right)
            case _:
                return search(left, mid - 1)

    # Invocación inicial
    return search(0, len(arr) - 1)


# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: el elemento existe (retorna su índice 2)
assert binary_search([1, 3, 5, 7, 9], 5) == 2, "Error: El objetivo 5 debería estar en el índice 2"

# 2. Caso del ejemplo: el elemento no existe (retorna -1)
assert binary_search([1, 3, 5, 7, 9], 4) == -1, "Error: El objetivo 4 no está, debería retornar -1"

# 3. Caso límite: el arreglo está vacío
assert binary_search([], 5) == -1, "Error: Arreglo vacío debería retornar -1"

print("Todos los tests funcionales en Python 3.14 pasaron correctamente.")