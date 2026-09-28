# Python 3.11 - Paradigma Iterativo Estricto
# Características de Python 3.11 utilizadas:
# 1. Type Hinting Nativo (PEP 585): list[int] directamente.
# 2. Manejo preciso de excepciones con notas explicativas (add_note) en caso de lista vacía.

def max_subarray_sum(nums: list[int]) -> int:
    """
    Calcula la suma máxima de un subarreglo contiguo no vacío usando
    el algoritmo de Kadane bajo un paradigma iterativo estricto.

    Complejidad Temporal: O(n) - Un único ciclo iterativo.
    Complejidad Espacial: O(1) - Estado mutable localizado.
    """
    if not nums:
        err = ValueError("El arreglo debe contener al menos un elemento.")
        err.add_note("Detalle Python 3.11: Se requiere len(nums) > 0 para evitar subarreglos vacíos.")
        raise err

    # Punteros y variables mutables de acumulación
    max_so_far: int = nums[0]
    current_max: int = nums[0]
    n: int = len(nums)
    index: int = 1

    # Bucle iterativo explícito evitando funciones de orden superior o recursión
    while index < n:
        num: int = nums[index]
        
        # Evaluación condicional para extender el subarreglo o iniciar uno nuevo
        if current_max + num > num:
            current_max = current_max + num
        else:
            current_max = num

        # Actualización de la suma máxima global alcanzada
        if current_max > max_so_far:
            max_so_far = current_max

        index += 1

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

    # CASO 3: Elemento único positivo y elementos negativos
    # Subarreglo óptimo: [100] -> suma = 100
    tc3: list[int] = [-10, -20, 100, -50, -5]
    assert max_subarray_sum(tc3) == 100, f"Falló Caso 3: esperado 100, obtenido {max_subarray_sum(tc3)}"

    print("¡Todos los casos de prueba en Python 3.11 pasaron correctamente!")