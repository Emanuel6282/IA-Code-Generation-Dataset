from functools import reduce
import operator

# En Python 3.9 (PEP 585) podemos usar 'list' directamente para el tipado genérico,
# sin necesidad de importar 'List' del módulo 'typing'.
def flatten(nested_list: list) -> list[int]:
    """
    Toma una lista arbitrariamente anidada de enteros y 
    retorna una lista plana unidimensional.
    
    Implementación estrictamente funcional usando map(), reduce() y recursión.
    """
    
    # 1. map(): Transforma cada elemento. Si es una lista, aplica flatten() recursivamente.
    #    Si es un entero puro, lo envuelve en una lista (ej. 1 -> [1]).
    #    Esto garantiza que 'mapped' sea siempre un iterable de listas planas.
    mapped = map(lambda x: flatten(x) if isinstance(x, list) else [x], nested_list)
    
    # 2. reduce(): Toma el iterable de listas y las concatena (operator.add) de 
    #    forma inmutable (creando nuevas listas en lugar de mutar una existente).
    #    El tercer argumento '[]' es el inicializador, lo que maneja elegantemente 
    #    el caso de una lista vacía de forma declarativa.
    return reduce(operator.add, mapped, [])

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Error: Falló el caso base del ejemplo"

# 2. Caso límite: lista completamente vacía
assert flatten([]) == [], "Error: Una lista vacía debería retornar una lista vacía"

# 3. Caso extremo: anidamiento profundo con listas vacías en el medio
assert flatten([1, [[], 2], [[[[3]]]]]) == [1, 2, 3], "Error: Falló ignorando listas vacías internas"

print("Todos los tests funcionales en Python 3.9 pasaron correctamente.")