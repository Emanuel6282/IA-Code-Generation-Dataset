def max_subarray_sum(nums: list[int]) -> int:
    """
    Dado un arreglo de enteros (puede contener negativos), retorna la suma
    maxima de cualquier subarreglo contiguo.

    Implementacion estrictamente iterativa (algoritmo de Kadane): usa un
    bucle for explicito con variables mutables (suma_actual, suma_maxima)
    actualizadas manualmente en cada iteracion mediante if/else. No se
    usan map(), filter(), reduce() ni recursion. Compatible con Python
    3.14.

    Args:
        nums: Lista de enteros (no vacia), puede contener negativos.

    Returns:
        La suma maxima de cualquier subarreglo contiguo no vacio. Si todos
        los elementos son negativos, retorna el menos negativo.
    """
    suma_actual: int = nums[0]
    suma_maxima: int = nums[0]

    for indice in range(1, len(nums)):
        numero = nums[indice]
        if numero > suma_actual + numero:
            suma_actual = numero
        else:
            suma_actual = suma_actual + numero

        if suma_actual > suma_maxima:
            suma_maxima = suma_actual

    return suma_maxima


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, mezcla de positivos y negativos
    assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

    # Caso 2: todos los elementos son negativos -> retorna el menos negativo
    assert max_subarray_sum([-8, -3, -6, -2, -5, -4]) == -2

    # Caso 3: todos los elementos son positivos -> la suma total es la maxima
    assert max_subarray_sum([1, 2, 3, 4, 5]) == 15

    print("Todos los casos de prueba pasaron correctamente.")