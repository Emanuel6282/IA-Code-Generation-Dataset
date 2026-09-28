# CARACTERÍSTICAS UTILIZADAS DE PYTHON 3.11:
# 1. Type Hinting Generico Nativo (PEP 585): Se reemplaza 'typing.List' por el tipo builtin 'list[int]'.
# 2. Manejo de Excepciones Enriquecido (add_note): Permite adjuntar contexto útil a excepciones si la lista recibida está vacía.
# 3. Optimización del Motor CPython 3.11 (Specialized Adaptive Interpreter):
#    El reemplazo de 'if/else' explícitos por las funciones integradas en C 'max()' y la iteración sobre
#    'nums[1:]' aprovecha el bytecode especializado e instrucciones adaptativas de Python 3.11, logrando una ejecución más rápida.

def max_subarray_sum(arr: list[int]) -> int:
    """
    Calcula la suma máxima de un subarreglo contiguo no vacío (Algoritmo de Kadane).
    Refactorizado e idiomático para Python 3.11.
    """
    if not arr:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Se requiere al menos 1 elemento para formar un subarreglo válido.")
        raise err

    current_max = arr[0]
    global_max = arr[0]

    # Iteración directa sobre los elementos a partir del índice 1
    for x in arr[1:]:
        current_max = max(x, current_max + x)
        global_max = max(global_max, current_max)

    return global_max


# ==========================================
# CASOS DE PRUEBA (ASSERTS ORIGINALES)
# ==========================================

if __name__ == "__main__":
    assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
    assert max_subarray_sum([1]) == 1
    assert max_subarray_sum([-2, -1]) == -1

    print("¡Todos los casos de prueba pasaron correctamente en Python 3.11!")


# EXPLICACIÓN DE CAMBIOS RESPECTO A PYTHON 3.9:
#
# 1. Eliminación de 'from typing import List':
#    En Python 3.11 es estándar y preferido utilizar directamente la colección nativa 'list[int]'
#    para la anotación de tipos (Type Hinting), reduciendo imports innecesarios.
#
# 2. Reemplazo de bloques 'if/else' por 'max()':
#    Sustituir la lógica de acumulación condicional manual por 'max(x, current_max + x)' y
#    'max(global_max, current_max)' simplifica notablemente la lectura del código y delega la
#    comparación a nivel de C en el intérprete de Python.
#
# 3. Iteración directa con Slicing ('for x in arr[1:]'):
#    Elimina la necesidad de indexación manual 'arr[i]' y el uso de 'range(1, len(arr))',
#    haciendo el bucle más pythónico y limpio.
#
# 4. Aprovechamiento del 'Specialized Adaptive Interpreter' (PEP 659):
#    El nuevo motor de ejecución de Python 3.11 analiza los tipos en tiempo de ejecución
#    y sustituye el bytecode genérico por instrucciones especializadas muy rápidas cuando
#    detecta tipos primitivos homogéneos como 'int'.
#
# 5. Validación con 'add_note()':
#    Se agrega una validación explícita para evitar errores al intentar acceder a 'arr[0]'
#    si la lista está vacía, utilizando la característica 'add_note()' nativa introducida en 3.11.