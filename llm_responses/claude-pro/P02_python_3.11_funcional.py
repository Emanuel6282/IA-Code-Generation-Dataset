"""Binary Search - solución funcional para Python 3.11."""

from functools import reduce
from typing import NamedTuple, Self


class SearchState(NamedTuple):
    """Estado inmutable de la búsqueda: rango [low, high] e índice encontrado (-1 si aún no)."""

    low: int
    high: int
    found: int = -1

    def done(self) -> bool:
        return self.found != -1 or self.low > self.high

    def narrow(self, arr: list[int], target: int) -> Self:
        """Devuelve el siguiente estado sin mutar el actual.

        typing.Self (PEP 673, nuevo en Python 3.11) indica que el método retorna una
        instancia de la misma clase, sin repetir el nombre de la clase ni usar TypeVar.
        """
        if self.done():
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
    final = reduce(
        lambda state, _: state.narrow(arr, target),
        range(len(arr).bit_length()),
        SearchState(low=0, high=len(arr) - 1),
    )
    return final.found


def _check_case(case: tuple[str, list[int], int, int]) -> AssertionError | None:
    """Evalúa un caso y devuelve el error (sin lanzarlo) o None si pasa."""
    name, arr, target, expected = case
    result = binary_search(arr, target)
    if result == expected:
        return None
    error = AssertionError(f"esperado {expected}, obtenido {result}")
    # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
    error.add_note(f"Caso fallido: {name} | arr={arr}, target={target}")
    return error


if __name__ == "__main__":
    test_cases: tuple[tuple[str, list[int], int, int], ...] = (
        # Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        ("objetivo presente", [1, 3, 5, 7, 9], 5, 2),
        # Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        ("objetivo ausente", [1, 3, 5, 7, 9], 4, -1),
        # Caso 3: arreglo vacío
        ("arreglo vacío", [], 1, -1),
    )

    # map evalúa todos los casos; filter se queda solo con los fallos.
    failures: tuple[AssertionError, ...] = tuple(
        filter(lambda outcome: outcome is not None, map(_check_case, test_cases))
    )

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): reporta todos los fallos juntos
        raise ExceptionGroup("Fallaron casos de prueba de binary_search", failures)

    assert not failures
    print("Todas las pruebas pasaron.")