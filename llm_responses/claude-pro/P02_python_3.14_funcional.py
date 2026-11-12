"""Binary Search - solución funcional para Python 3.14."""

from functools import reduce
from string.templatelib import Interpolation, Template
from typing import NamedTuple

_CONVERSIONS = {"r": repr, "s": str, "a": ascii, None: lambda value: value}


class SearchState(NamedTuple):
    """Estado inmutable de la búsqueda: rango [low, high] e índice encontrado (-1 si aún no)."""

    low: int
    high: int
    found: int = -1

    # Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en Python 3.14):
    # el método puede anotar su retorno con la propia clase, SearchState, aunque la
    # clase aún no exista al definirse el método, sin comillas ni
    # `from __future__ import annotations`. En Python 3.13 esto daría NameError.
    def narrow(self, arr: list[int], target: int) -> SearchState:
        """Devuelve el siguiente estado sin mutar el actual."""
        if self.found != -1 or self.low > self.high:   # ya terminó: el estado no cambia
            return self
        mid = (self.low + self.high) // 2
        if arr[mid] == target:
            return self._replace(found=mid)
        if arr[mid] < target:
            return self._replace(low=mid + 1)   # el objetivo solo puede estar a la derecha
        return self._replace(high=mid - 1)      # el objetivo solo puede estar a la izquierda


def binary_search(arr: list[int], target: int) -> int:
    """Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está."""
    # Una búsqueda binaria sobre n elementos necesita como mucho n.bit_length()
    # pasos (⌊log2 n⌋ + 1); reduce aplica `narrow` exactamente ese número de veces.
    return reduce(
        lambda state, _: state.narrow(arr, target),
        range(len(arr).bit_length()),
        SearchState(low=0, high=len(arr) - 1),
    ).found


def _render_part(part: str | Interpolation) -> str:
    """Convierte una parte de un Template en texto, aplicando conversión y formato."""
    if isinstance(part, Interpolation):
        return format(_CONVERSIONS[part.conversion](part.value), part.format_spec)
    return part


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, sin bucles."""
    return "".join(map(_render_part, template))


def _check_case(case: tuple[str, list[int], int, int]) -> str | None:
    """Evalúa un caso y devuelve el mensaje de fallo, o None si pasa."""
    name, arr, target, expected = case
    result = binary_search(arr, target)
    return None if result == expected else render(
        t"Caso {name!r}: arr={arr}, target={target}, esperado {expected}, obtenido {result}"
    )


if __name__ == "__main__":
    test_cases: tuple[tuple[str, list[int], int, int], ...] = (
        # Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        ("objetivo presente", [1, 3, 5, 7, 9], 5, 2),
        # Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        ("objetivo ausente", [1, 3, 5, 7, 9], 4, -1),
        # Caso 3: arreglo vacío
        ("arreglo vacío", [], 1, -1),
    )

    # map evalúa todos los casos; filter se queda solo con los mensajes de fallo.
    failures: tuple[str, ...] = tuple(
        filter(lambda message: message is not None, map(_check_case, test_cases))
    )

    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")