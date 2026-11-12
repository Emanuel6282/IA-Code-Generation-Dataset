"""Max Subarray Sum - solución funcional para Python 3.14."""

from collections.abc import Sequence
from functools import reduce
from itertools import accumulate
from string.templatelib import Interpolation, Template
from types import MappingProxyType

_CONVERSIONS = MappingProxyType({"r": repr, "s": str, "a": ascii, None: lambda value: value})


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


def _render_part(part: str | Interpolation) -> str:
    """Convierte una parte de un Template en texto, aplicando conversión y formato."""
    if isinstance(part, Interpolation):
        return format(_CONVERSIONS[part.conversion](part.value), part.format_spec)
    return part


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, sin bucles."""
    return "".join(map(_render_part, template))


def _check_case(case: tuple[str, tuple[int, ...], int]) -> str | None:
    """Evalúa un caso y devuelve el mensaje de fallo, o None si pasa."""
    name, nums, expected = case
    result = max_subarray_sum(nums)
    return None if result == expected else render(
        t"Caso {name!r} {list(nums)}: esperado {expected}, obtenido {result}"
    )


def _check_empty_raises() -> str | None:
    """Un arreglo vacío debe lanzar ValueError; si no lo hace, devuelve el mensaje."""
    try:
        max_subarray_sum(())
    except ValueError:
        return None
    return "un arreglo vacío debería lanzar ValueError"


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

    # map evalúa todos los casos; filter se queda solo con los mensajes de fallo.
    failures: tuple[str, ...] = tuple(
        filter(lambda message: message is not None,
               (*map(_check_case, test_cases), _check_empty_raises()))
    )

    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")