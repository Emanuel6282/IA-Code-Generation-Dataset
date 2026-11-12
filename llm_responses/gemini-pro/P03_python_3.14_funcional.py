from functools import reduce
import operator

# Uso de Type Aliases modernos (PEP 695), fuertemente consolidados en Python 3.14,
# para definir una estructura recursiva de forma nativa y elegante sin importar 'typing'
type NestedList = list[int | 'NestedList']

def flatten(nested_list: NestedList) -> list[int]:
    """
    Toma una lista arbitrariamente anidada de enteros y 
    retorna una lista plana unidimensional.
    
    Implementación estrictamente funcional pura: inmutable y declarativa,
    basada en evaluación de expresiones en lugar de ejecución de instrucciones.
    """
    
    # map(): Transforma cada elemento del iterable. 
    # Si es una lista, se invoca a sí misma (recursión pura). 
    # Si es un escalar, lo envuelve en una lista (ej. 1 -> [1]).
    mapped = map(lambda item: flatten(item) if isinstance(item, list) else [item], nested_list)
    
    # reduce(): Pliega (fold) el iterable resultante concatenando las listas.
    # operator.add actúa como el '+' inmutable de las listas, creando nuevas 
    # estructuras en lugar de mutar memoria local.
    # El valor inicial '[]' es crucial para procesar limpiamente listas vacías.
    return reduce(operator.add, mapped, [])


# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Error: Falló el caso base del ejemplo"

# 2. Caso límite: lista completamente vacía
assert flatten([]) == [], "Error: Una lista vacía debería retornar una lista vacía"

# 3. Caso extremo: anidamiento profundo asimétrico y listas vacías internas
assert flatten([1, [[], 2], [[[[3]]]]]) == [1, 2, 3], "Error: Falló ignorando listas vacías internas"

print("Todos los tests funcionales en Python 3.14 pasaron correctamente.")