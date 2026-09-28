def max_subarray_sum(nums):
    """
    Dado un arreglo de enteros (puede contener negativos), retorna la suma
    maxima de cualquier subarreglo contiguo.

    Implementacion mediante el algoritmo de Kadane: se recorre el arreglo
    manteniendo la suma maxima que termina en la posicion actual
    (suma_actual) y la suma maxima global encontrada hasta el momento
    (suma_maxima). En cada paso, suma_actual se actualiza como el maximo
    entre extender el subarreglo anterior (suma_actual + numero) o
    empezar un nuevo subarreglo desde el elemento actual (numero).

    Args:
        nums: Lista de enteros (no vacia), puede contener negativos.

    Returns:
        La suma maxima de cualquier subarreglo contiguo no vacio. Si todos
        los elementos son negativos, retorna el menos negativo (el mayor
        de ellos).
    """
    suma_actual = nums[0]
    suma_maxima = nums[0]

    for numero in nums[1:]:
        suma_actual = max(numero, suma_actual + numero)
        suma_maxima = max(suma_maxima, suma_actual)

    return suma_maxima


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, mezcla de positivos y negativos
    assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

    # Caso 2: todos los elementos son negativos -> retorna el menos negativo
    assert max_subarray_sum([-8, -3, -6, -2, -5, -4]) == -2

    # Caso 3: todos los elementos son positivos -> la suma total es la maxima
    assert max_subarray_sum([1, 2, 3, 4, 5]) == 15

    print("Todos los casos de prueba pasaron correctamente.")