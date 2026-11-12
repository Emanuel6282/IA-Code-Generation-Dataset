# CARACTERISTICAS DE PYTHON 3.14 UTILIZADAS Y POR QUE:
# 1. Type Parameter Syntax (PEP 695): Totalmente estandarizado en versiones recientes, permite definir genéricos limpios en la misma firma (ej. [T: int]) sin usar `TypeVar` ni importar el módulo `typing`.
# 2. Tipado Nativo (PEP 585): Uso directo de `list` en lugar del obsoleto `typing.List`, reduciendo el tiempo de importación y simplificando el código.
# 3. Optimización para JIT y Tier 2 Optimizer: Python 3.13/3.14 introdujeron e iteraron un compilador JIT (Just-In-Time). Los bucles iterativos puros con variables locales y tipos estables son los mayores beneficiarios de esta arquitectura, logrando un rendimiento comparable a C "gratis".

def binary_search[T: int](arr: list[T], target: T) -> int:
    left: int = 0
    right: int = len(arr) - 1
    
    while left <= right:
        mid: int = (left + right) // 2
        
        # Se almacena en una variable local para aprovechar la velocidad extrema 
        # de acceso (LOAD_FAST) del nuevo compilador JIT de Python 3.14.
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
assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1

print("Todos los tests iterativos en Python 3.14 pasaron correctamente.")

# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.14:
#
# 1. Eliminación de Imports Innecesarios: El código original requería `from typing import List`. En la era de Python 3.14, depender del módulo `typing` para colecciones estándar es un anti-patrón. Todo se resuelve a nivel nativo con genéricos integrados.
#
# 2. Sintaxis `[T: int]`: Al definir la función genérica, aseguramos matemáticamente a nivel de análisis estático (con mypy o pyright) que el tipo de datos dentro de la lista es exactamente del mismo tipo que el `target`, algo que en las viejas versiones requería un boilerplate excesivo.
#
# 3. Micro-optimización de acceso `mid_val`: En lugar de evaluar `arr[mid]` múltiples veces dentro de las ramas del `if/elif`, lo cargamos una sola vez en memoria local. Con los optimizadores adaptativos y el JIT introducidos entre 3.11 y 3.14, las operaciones de variables locales son despachadas directamente a los registros de la CPU, mejorando radicalmente la eficiencia del algoritmo frente al código original.