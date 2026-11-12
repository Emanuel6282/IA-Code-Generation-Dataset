# En Python 3.9 se introdujo PEP 585, permitiendo usar las colecciones nativas 
# como genéricos (ej. list[int]) sin necesidad de importar el módulo 'typing'.

def flatten(nested_list: list) -> list[int]:
    """
    Toma una lista arbitrariamente anidada de enteros y 
    retorna una lista plana unidimensional.
    
    Implementación estrictamente iterativa: sin recursión, 
    utilizando una pila (stack) manejada mediante un bucle while.
    """
    result: list[int] = []
    
    # Inicializamos la pila con los elementos de la lista base.
    # Invertimos la lista ([::-1]) al meterla en la pila para que, al 
    # hacer pop() (que extrae el último elemento), salgan en el orden correcto.
    stack = nested_list[::-1]
    
    while stack:
        # Extraemos el elemento en el tope de la pila
        item = stack.pop()
        
        if isinstance(item, list):
            # Si es una lista, desempaquetamos su contenido y lo metemos 
            # de nuevo a la pila (también invertido para mantener el orden)
            stack.extend(item[::-1])
        else:
            # Si es un entero, lo agregamos al resultado final
            result.append(item)
            
    return result

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Error: Falló el caso base del ejemplo"

# 2. Caso límite: lista completamente vacía
assert flatten([]) == [], "Error: Una lista vacía debería retornar una lista vacía"

# 3. Caso extremo: anidamiento profundo con listas vacías en el medio
assert flatten([1, [[], 2], [[[[3]]]]]) == [1, 2, 3], "Error: Falló ignorando listas vacías internas"

print("Todos los tests iterativos en Python 3.9 pasaron correctamente.")