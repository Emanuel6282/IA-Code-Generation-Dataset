# Python 3.9 - Paradigma Funcional Estricto
# Características de Python 3.9 utilizadas:
# 1. Type Hinting Generico nativo (PEP 585): list[int] y tuple[int, int] directamente sin requerir 'typing'.
# 2. Inmutabilidad y composición mediante functools.reduce, map() y tuplas inmutables.

from functools import reduce

def max_subarray_sum(nums: list[int]) -> int:
    """
    Calcula la suma máxima de un subarreglo contiguo no vacío usando el
    Algoritmo de Kadane adaptado al paradigma funcional con functools.reduce.

    Complejidad Temporal: O(n) - Una sola pasada inmutable sobre la lista.
    Complejidad Espacial: O(1) - Uso de acumuladores en tuplas inmutables.
    """
    if not nums:
        raise ValueError("El arreglo no puede estar vacío.")

    # Estado acumulador inmutable: (current_max, max_so_far)
    initial_state: tuple[int, int] = (nums[0], nums[0])

    # Función reductora pura sin efectos secundarios
    def step(acc: tuple[int, int], x: int) -> tuple[int, int]:
        curr_max, max_so_far = acc
        new_curr = max(x, curr_max + x)
        new_max = max(max_so_far, new_curr)
        return (new_curr, new_max)

    # Reducción funcional que procesa el resto de la lista (nums[1:])
    final_state: tuple[int, int] = reduce(step, nums[1:], initial_state)

    # Extraer el resultado global acumulado de la tupla final
    return final_state[1]


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

    # CASO 3: Todos los elementos son positivos
    # Subarreglo óptimo: [1, 2, 3, 4, 10] -> suma = 20
    tc3: list[int] = [1, 2, 3, 4, 10]
    assert max_subarray_sum(tc3) == 20, f"Falló Caso 3: esperado 20, obtenido {max_subarray_sum(tc3)}"

    print("¡Todos los casos de prueba en Python 3.9 (paradigma funcional) pasaron correctamente!")