from functools import reduce
import operator
from typing import Any

def flatten(nested_list: list[Any]) -> list[int]:
    """
    Toma una lista arbitrariamente anidada de enteros y 
    retorna una lista plana unidimensional.
    
    Implementación estrictamente funcional: inmutable, recursiva, 
    y evaluada mediante composición de map() y reduce().
    """
    
    # map(): Aplica una función pura a cada elemento. Si es una lista, se llama
    # a sí misma (recursión pura). Si es un escalar (entero), lo envuelve en 
    # una lista. Transforma: [1, [2, 3]] -> [[1], [2, 3]]
    mapped = map(lambda x: flatten(x) if isinstance(x, list) else [x], nested_list)
    
    # reduce(): Toma el flujo de listas generado por map() y las concatena 
    # usando operator.add (que en listas actúa como el operador '+'). 
    # Esto genera listas nuevas y puras sin mutar memoria local.
    # El valor inicial '[]' garantiza que una lista vacía de entrada no falle.
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

print("Todos los tests funcionales en Python 3.11 pasaron correctamente.")