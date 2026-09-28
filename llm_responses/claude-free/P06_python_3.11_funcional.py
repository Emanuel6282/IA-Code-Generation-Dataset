from functools import reduce

Estado = tuple[int, int]


def max_subarray_sum(nums: list[int]) -> int:
    """
    Dado un arreglo de enteros (puede contener negativos), retorna la suma
    maxima de cualquier subarreglo contiguo.

    Implementacion en paradigma funcional (algoritmo de Kadane expresado
    con functools.reduce): el estado (suma_actual, suma_maxima) se modela
    como una tupla inmutable que se reconstruye por completo en cada paso
    de la reduccion, sin mutar ninguna variable externa. No se usan
    bucles for/while con estado mutable. Compatible con Python 3.11.

    Args:
        nums: Lista de enteros (no vacia), puede contener negativos.

    Returns:
        La suma maxima de cualquier subarreglo contiguo no vacio. Si todos
        los elementos son negativos, retorna el menos negativo.
    """

    def combinar(estado: Estado, numero: int) -> Estado:
        suma_actual, suma_maxima = estado
        nueva_suma_actual = max(numero, suma_actual + numero)
        nueva_suma_maxima = max(suma_maxima, nueva_suma_actual)
        return (nueva_suma_actual, nueva_suma_maxima)

    estado_inicial: Estado = (nums[0], nums[0])
    _, suma_maxima_final = reduce(combinar, nums[1:], estado_inicial)

    return suma_maxima_final


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, mezcla de positivos y negativos
    assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

    # Caso 2: todos los elementos son negativos -> retorna el menos negativo
    assert max_subarray_sum([-8, -3, -6, -2, -5, -4]) == -2

    # Caso 3: todos los elementos son positivos -> la suma total es la maxima
    assert max_subarray_sum([1, 2, 3, 4, 5]) == 15

    print("Todos los casos de prueba pasaron correctamente.")