# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Tipado Nativo Estándar (PEP 585): Se utiliza `list[int]` directamente, eliminando la necesidad de importar `List` desde el módulo `typing`. En Python 3.11, importar tipos genéricos en mayúscula desde typing se considera obsoleto y menos eficiente.
# 2. Faster CPython (PEP 659 - Adaptive Specializing Interpreter): Aunque es una mejora a nivel de máquina virtual y no de sintaxis, este bucle iterativo puro saca ventaja masiva de la especialización de instrucciones en tiempo de ejecución de Python 3.11, haciendo que las operaciones `+`, `//` y las comparaciones numéricas sean entre un 10% y un 25% más rápidas "gratis".

def binary_search(arr: list[int], target: int) -> int:
    left: int = 0
    right: int = len(arr) - 1
    
    while left <= right:
        mid: int = (left + right) // 2
        
        # Almacenamos arr[mid] para evitar la penalización de buscar en la 
        # estructura de datos múltiples veces en el mismo ciclo.
        mid_val: int = arr[mid]
        
        if mid_val == target:
            return mid
        elif mid_val < target:
            left = mid + 1
        else:
            right = mid - 1
            
    return -1

assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1

print("Todos los tests pasaron correctamente.")

# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.11:
#
# 1. Eliminación de `from typing import List`: A partir de Python 3.9 (pero fuertemente estandarizado e incentivado por las herramientas de linting en 3.11), las colecciones base como `list` soportan suscripción (genéricos) directamente. Eliminar importaciones innecesarias reduce el tiempo de arranque del script.
#
# 2. Variable local `mid_val`: En el código de Python 3.9 se accedía a `arr[mid]` hasta dos veces por iteración. En Python 3.11, las operaciones de acceso a variables locales (`LOAD_FAST`) fueron altamente optimizadas por el nuevo intérprete. Almacenar el valor en `mid_val` mejora la legibilidad y aprovecha esta optimización de rendimiento, realizando la lectura del array una sola vez por ciclo.
#
# 3. Anotaciones de tipo explícitas (`left: int`, `right: int`, etc.): En los ecosistemas modernos de Python 3.11, el tipado de variables internas es idiomático para trabajar con herramientas como `mypy` o `pyright`, garantizando que no se introduzcan errores de tipo sutiles sin afectar en lo absoluto el rendimiento en tiempo de ejecución.