def flatten(nested_list: list) -> list[int]:
    """
    Toma una lista arbitrariamente anidada de enteros y 
    retorna una lista plana unidimensional.
    
    Implementación estrictamente iterativa: sin recursión, manejando
    el estado mutable a través de una pila (stack) local y un bucle while.
    """
    result: list[int] = []
    
    # Inicializamos la pila invirtiendo la lista original.
    # Al invertirla, el primer elemento queda en el tope de la pila 
    # y saldrá primero al hacer pop().
    stack = nested_list[::-1]
    
    while stack:
        item = stack.pop()
        
        # Uso de Structural Pattern Matching (optimizado en Python 3.11)
        # Reemplaza los bloques imperativos if isinstance(item, list):
        match item:
            case list():
                # Si es una lista, la invertimos y la inyectamos en la pila
                stack.extend(item[::-1])
            case _:
                # Si es un entero, lo agregamos al resultado mutando la lista
                result.append(item)
                
    return result

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Error: Falló el caso base"

# 2. Caso límite: lista completamente vacía
assert flatten([]) == [], "Error: Una lista vacía debería retornar una lista vacía"

# 3. Caso extremo: anidamiento profundo, asimétrico y con listas vacías internas
assert flatten([1, [[], 2], [[[[3]]]]]) == [1, 2, 3], "Error: Falló ignorando listas vacías internas"

print("Todos los tests iterativos en Python 3.11 pasaron correctamente.")