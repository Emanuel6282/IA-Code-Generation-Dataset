# Python 3.11 - Paradigma Funcional Estricto
# Características de Python 3.11 utilizadas:
# 1. Type Hinting Generico Nativo (PEP 585): Uso de list[int] y tuple[int, int].
# 2. Manejo funcional de errores con add_note() para enriquecer excepciones.
# 3. Inmutabilidad estricta: functools.reduce(), expresiones lambda y tuplas inmutables sin bucles for/while.

from functools import reduce

def max_subarray_sum(nums: list[int]) -> int:
    """
    Calcula la suma máxima de cualquier subarreglo contiguo usando
    el Algoritmo de Kadane implementado en paradigma funcional puro.
    
    Complejidad Temporal: O(n)
    Complejidad Espacial: O(1)
    """
    if not nums:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Paradigma funcional: Se requiere al menos un elemento para formar un subarreglo no vacío.")
        raise err

    # Estado acumulador inmutable: (current_max, max_so_far)
    initial_state: tuple[int, int] = (nums[0], nums[0])

    # Función pura reductora que calcula el siguiente estado inmutable
    step = lambda acc, x: (
        max(x, acc[0] + x),
        max(acc[1], max(x, acc[0] + x))
    )

    # Reducción inmutable sobre el resto de la secuencia
    final_state: tuple[int, int] = reduce(step, nums[1:], initial_state)

    return final_state[1]


# ==========================================
# CASOS DE PRUEBA (ASSERTS)
# ==========================================

if __name__ == "__main__":
    # CASO 1: Ejemplo del enunciado con números positivos y negativos
    # Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
    tc1: list[int] = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    assert max_subarray_sum(tc1) == 6, f"Falló Caso 1: esperado 6, obtenido {max_subarray_sum(tc1)}"

    # CASO 2: Todos los elementos son negativos
    # Subarreglo óptimo: [-1] -> retorna el menos negativo
    tc2: list[int] = [-5, -3, -8, -1, -4]
    assert max_subarray_sum(tc2) == -1, f"Falló Caso 2: esperado -1, obtenido {max_subarray_sum(tc2)}"

    # CASO 3: Arreglo con elementos positivos
    # Subarreglo óptimo: [10, 20, 30] -> suma = 60
    tc3: list[int] = [10, 20, 30]
    assert max_subarray_sum(tc3) == 60, f"Falló Caso 3: esperado 60, obtenido {max_subarray_sum(tc3)}"

    print("¡Todos los casos de prueba en Python 3.11 (Paradigma Funcional) pasaron correctamente!")