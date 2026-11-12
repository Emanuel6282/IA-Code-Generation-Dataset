"""Max Subarray Sum - solución iterativa para Python 3.11."""

from collections.abc import Sequence


def max_subarray_sum(nums: Sequence[int]) -> int:
    """Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.

    Usa el algoritmo de Kadane: O(n) en tiempo y O(1) en memoria. Acepta cualquier
    secuencia indexable (lista, tupla, range...).

    Raises:
        ValueError: si `nums` está vacío (no existe ningún subarreglo no vacío).
    """
    if not nums:
        raise ValueError("el arreglo debe tener al menos un elemento")

    # Se parte del primer elemento (no de 0) porque el subarreglo no puede ser vacío;
    # así, si todos son negativos, el resultado es el menos negativo.
    best = nums[0]
    current = nums[0]
    for i in range(1, len(nums)):          # por índice: sin copiar la secuencia
        x = nums[i]
        # La mejor suma que termina en x: o extiende el subarreglo anterior,
        # o empieza de nuevo en x si lo acumulado hasta ahora resta.
        if current + x > x:
            current = current + x
        else:
            current = x
        if current > best:
            best = current
    return best


if __name__ == "__main__":
    test_cases: list[tuple[str, list[int], int]] = [
        # Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
        ("ejemplo del enunciado", [-2, 1, -3, 4, -1, 2, 1, -5, 4], 6),
        # Caso 2: todos negativos -> el menos negativo
        ("todos negativos", [-3, -1, -2], -1),
        ("un solo elemento negativo", [-5], -5),
        # Caso 3: todos positivos -> el arreglo completo
        ("todos positivos", [1, 2, 3], 6),
    ]

    # Se ejecutan todos los casos aunque alguno falle y se reportan juntos.
    failures: list[Exception] = []
    for name, nums, expected in test_cases:
        try:
            result = max_subarray_sum(nums)
            assert result == expected, f"esperado {expected}, obtenido {result}"
        except AssertionError as error:
            # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
            error.add_note(f"Caso fallido: {name} | entrada: {nums}")
            failures.append(error)

    # Arreglo vacío -> ValueError (si no lo lanza, también se registra como fallo)
    try:
        max_subarray_sum([])
        failures.append(AssertionError("un arreglo vacío debería lanzar ValueError"))
    except ValueError:
        pass

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): agrupa todos los fallos
        raise ExceptionGroup("Fallaron casos de prueba de max_subarray_sum", failures)

    print("Todas las pruebas pasaron.")