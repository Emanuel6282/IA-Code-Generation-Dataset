"""Max Subarray Sum - solución funcional para Python 3.11."""

from collections.abc import Sequence
from functools import reduce
from itertools import accumulate


def _extend_or_restart(current: int, x: int) -> int:
    """Mejor suma de un subarreglo que termina en x: extender el anterior o empezar en x."""
    return max(x, current + x)


def max_subarray_sum(nums: Sequence[int]) -> int:
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


def _check_case(case: tuple[str, tuple[int, ...], int]) -> AssertionError | None:
    """Evalúa un caso y devuelve el error (sin lanzarlo) o None si pasa."""
    name, nums, expected = case
    result = max_subarray_sum(nums)
    if result == expected:
        return None
    error = AssertionError(f"esperado {expected}, obtenido {result}")
    # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
    error.add_note(f"Caso fallido: {name} | entrada: {list(nums)}")
    return error


def _check_empty_raises() -> AssertionError | None:
    """Un arreglo vacío debe lanzar ValueError; si no lo hace, devuelve el error."""
    try:
        max_subarray_sum(())
    except ValueError:
        return None
    return AssertionError("un arreglo vacío debería lanzar ValueError")


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

    # map evalúa todos los casos; filter se queda solo con los fallos.
    failures: tuple[AssertionError, ...] = tuple(
        filter(lambda outcome: outcome is not None,
               (*map(_check_case, test_cases), _check_empty_raises()))
    )

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): reporta todos los fallos juntos
        raise ExceptionGroup("Fallaron casos de prueba de max_subarray_sum", failures)

    assert not failures
    print("Todas las pruebas pasaron.")