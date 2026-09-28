# Python 3.14 - Paradigma Funcional Estricto
# Características de Python 3.14 / 3.12+ utilizadas:
# 1. Type Hinting Generico Nativo (PEP 585 / PEP 695): Alias de tipos inmutables con la palabra clave 'type' (Type Alias Statements).
# 2. Tipado nativo directo list[int] y tuple[int, int] sin requerir el módulo 'typing'.
# 3. Enriquecimiento declarativo de excepciones con 'add_note()' sobre el objeto Exception.
# 4. Inmutabilidad estricta: functools.reduce(), expresiones lambda y tuplas sin bucles for/while.

from functools import reduce

# Alias de tipo declarativo inmutable (Python 3.12+)
type State = tuple[int, int]  # (current_max, max_so_far)

def max_subarray_sum(nums: list[int]) -> int:
    """
    Calcula la suma máxima de un subarreglo contiguo no vacío mediante
    una reducción inmutable pura (Algoritmo de Kadane en Paradigma Funcional).

    Complejidad Temporal: O(n)
    Complejidad Espacial: O(1)
    """
    if not nums:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Paradigma funcional Python 3.14: Se requiere len(nums) >= 1 para procesar la reducción.")
        raise err

    # Estado inmutable inicial: (current_max, max_so_far)
    initial_state: State = (nums[0], nums[0])

    # Función pura de transición de estado
    step = lambda state, x: (
        max(x, state[0] + x),
        max(state[1], max(x, state[0] + x))
    )

    # Reducción funcional pura sobre el resto de la colección inmutable
    final_state: State = reduce(step, nums[1:], initial_state)

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

    # CASO 3: Arreglo con elementos positivos y ceros
    # Subarreglo óptimo: [0, 10, 20, 30] -> suma = 60
    tc3: list[int] = [-10, 0, 10, 20, 30, -5]
    assert max_subarray_sum(tc3) == 60, f"Falló Caso 3: esperado 60, obtenido {max_subarray_sum(tc3)}"

    print("¡Todos los casos de prueba en Python 3.14 (Paradigma Funcional) pasaron correctamente!")