"""Max Subarray Sum - solución funcional para Python 3.9."""

from functools import reduce
from itertools import accumulate


def _extend_or_restart(current: int, x: int) -> int:
    """Mejor suma de un subarreglo que termina en x: extender el anterior o empezar en x."""
    return max(x, current + x)


# Python 3.9 (PEP 585): genéricos nativos (list[int]) en lugar de typing.List.
def max_subarray_sum(nums: list[int]) -> int:
    """Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.

    Algoritmo de Kadane expresado sin estado mutable, en dos pasos perezosos:
    1. accumulate genera, para cada posición, la mejor suma de un subarreglo que
       termina ahí. Parte del primer elemento (no de 0), así que nunca considera el
       subarreglo vacío y, si todos son negativos, el resultado es el menos negativo.
    2. reduce(max, ...) se queda con la mayor de esas sumas.
    O(n) en tiempo y O(1) en memoria: no se crean listas intermedias.

    Raises:
        ValueError: si `nums` está vacío (no existe ningún subarreglo no vacío).
    """
    if not nums:
        raise ValueError("el arreglo debe tener al menos un elemento")
    return reduce(max, accumulate(nums, _extend_or_restart))


if __name__ == "__main__":
    test_cases: tuple[tuple[str, tuple[int, ...], int], ...] = (
        # Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
        ("ejemplo del enunciado", (-2, 1, -3, 4, -1, 2, 1, -5, 4), 6),
        # Caso 2: todos negativos -> el menos negativo
        ("todos negativos", (-3, -1, -2), -1),
        ("un solo elemento negativo", (-5,), -5),
        # Caso 3: todos positivos -> el arreglo completo
        ("todos positivos", (1, 2, 3), 6),
    )

    # map calcula el resultado de cada caso; filter se queda solo con los que fallan.
    results = map(lambda case: (case, max_subarray_sum(list(case[1]))), test_cases)
    failures = tuple(filter(lambda pair: pair[1] != pair[0][2], results))

    assert not failures, "\n".join(
        map(lambda pair: f"{pair[0][0]}: esperado {pair[0][2]}, obtenido {pair[1]}", failures)
    )

    # Arreglo vacío -> ValueError
    try:
        max_subarray_sum([])
        assert False, "un arreglo vacío debería lanzar ValueError"
    except ValueError:
        pass

    print("Todas las pruebas pasaron.")