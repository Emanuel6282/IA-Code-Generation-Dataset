def max_subarray_sum(nums):
    """Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.

    Usa el algoritmo de Kadane: O(n) en tiempo y O(1) en memoria.

    Raises:
        ValueError: si `nums` está vacío (no existe ningún subarreglo no vacío).
    """
    if not nums:
        raise ValueError("el arreglo debe tener al menos un elemento")

    best = current = nums[0]
    for x in nums[1:]:
        # La mejor suma que termina en x: o extiende el subarreglo anterior,
        # o empieza de nuevo en x si lo acumulado hasta ahora resta.
        current = max(x, current + x)
        best = max(best, current)
    return best


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
    assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

    # Caso 2: todos negativos -> el menos negativo (no se permite el subarreglo vacío)
    assert max_subarray_sum([-3, -1, -2]) == -1
    assert max_subarray_sum([-5]) == -5

    # Caso 3: todos positivos -> el arreglo completo; y arreglo vacío -> error
    assert max_subarray_sum([1, 2, 3]) == 6
    try:
        max_subarray_sum([])
        assert False, "un arreglo vacío debería lanzar ValueError"
    except ValueError:
        pass

    print("Todas las pruebas pasaron.")