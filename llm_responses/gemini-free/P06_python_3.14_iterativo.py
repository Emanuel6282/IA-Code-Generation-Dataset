# Python 3.14 - Paradigma Iterativo Estricto
# Características de Python 3.14 utilizadas:
# 1. Type Hinting Nativo (PEP 585/PEP 695): Annotations directas list[int] sin imports adicionales.
# 2. Manejo detallado de excepciones mediante 'add_note()' para enriquecer diagnósticos de error en la VM de Python 3.14.

def max_subarray_sum(nums: list[int]) -> int:
    """
    Calcula la suma máxima de un subarreglo contiguo no vacío utilizando el
    Algoritmo de Kadane bajo un paradigma iterativo estricto.

    Complejidad Temporal: O(n) - Recorrido iterativo de una sola pasada.
    Complejidad Espacial: O(1) - Punteros y variables mutables locales.
    """
    if not nums:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Detalle Python 3.14: Se requiere len(nums) >= 1 para garantizar un subarreglo no vacío.")
        raise err

    # Variables acumuladoras mutables
    max_so_far: int = nums[0]
    current_max: int = nums[0]

    # Recorrido iterativo explícito evitando map(), filter(), reduce() y recursión
    for i in range(1, len(nums)):
        num: int = nums[i]

        # Decisión iterativa: extender el subarreglo actual o comenzar uno nuevo desde 'num'
        if current_max + num > num:
            current_max = current_max + num
        else:
            current_max = num

        # Actualización del máximo global hallado
        if current_max > max_so_far:
            max_so_far = current_max

    return max_so_far


# ==========================================
# CASOS DE PRUEBA (ASSERTS)
# ==========================================

if __name__ == "__main__":
    # CASO 1: Ejemplo del enunciado con números mixtos
    # Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
    tc1: list[int] = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    assert max_subarray_sum(tc1) == 6, f"Falló Caso 1: esperado 6, obtenido {max_subarray_sum(tc1)}"

    # CASO 2: Todos los elementos son negativos
    # Subarreglo óptimo: [-1] -> retorna el menos negativo
    tc2: list[int] = [-5, -3, -8, -1, -4]
    assert max_subarray_sum(tc2) == -1, f"Falló Caso 2: esperado -1, obtenido {max_subarray_sum(tc2)}"

    # CASO 3: Subarreglo único positivo entre valores altamente negativos
    # Subarreglo óptimo: [100] -> suma = 100
    tc3: list[int] = [-10, -20, 100, -50, -5]
    assert max_subarray_sum(tc3) == 100, f"Falló Caso 3: esperado 100, obtenido {max_subarray_sum(tc3)}"

    print("¡Todos los casos de prueba en Python 3.14 (Paradigma Iterativo) pasaron correctamente!")