# Python 3.9 - Paradigma Iterativo Estricto
# Características de Python 3.9 utilizadas:
# 1. Type Hinting Generico nativo (PEP 585): list[int] directamente sin requerir import de 'typing.List'.

def max_subarray_sum(nums: list[int]) -> int:
    """
    Suma máxima de un subarreglo contiguo no vacío usando el Algoritmo de Kadane.
    
    Complejidad Temporal: O(n) - Un solo recorrido iterativo.
    Complejidad Espacial: O(1) - Punteros/variables mutables únicamente.
    """
    if not nums:
        raise ValueError("El arreglo no puede estar vacío.")

    # Acumuladores e indicadores mutables
    max_so_far: int = nums[0]
    current_max: int = nums[0]

    # Bucle for iterativo explícito evitando combinadores funcionales (map, filter, reduce)
    for i in range(1, len(nums)):
        num: int = nums[i]
        
        # Decide si reiniciar la secuencia desde 'num' o continuar sumando al subarreglo
        if current_max + num > num:
            current_max = current_max + num
        else:
            current_max = num

        # Actualiza el máximo global acumulado
        if current_max > max_so_far:
            max_so_far = current_max

    return max_so_far


# ==========================================
# CASOS DE PRUEBA (ASSERTS)
# ==========================================

if __name__ == "__main__":
    # CASO 1: Ejemplo del enunciado (Subarreglo óptimo: [4, -1, 2, 1] -> 6)
    nums1: list[int] = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    assert max_subarray_sum(nums1) == 6, f"Falló Caso 1: esperado 6, obtenido {max_subarray_sum(nums1)}"

    # CASO 2: Todos los elementos son negativos (Subarreglo óptimo: [-1] -> el menos negativo)
    nums2: list[int] = [-5, -3, -8, -1, -4]
    assert max_subarray_sum(nums2) == -1, f"Falló Caso 2: esperado -1, obtenido {max_subarray_sum(nums2)}"

    # CASO 3: Arreglo con un solo elemento negativo y valores positivos
    nums3: list[int] = [1, 2, 3, 4, 10]
    assert max_subarray_sum(nums3) == 20, f"Falló Caso 3: esperado 20, obtenido {max_subarray_sum(nums3)}"

    print("¡Todos los casos de prueba en Python 3.9 pasaron correctamente!")