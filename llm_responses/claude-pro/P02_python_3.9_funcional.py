"""Binary Search - solución funcional para Python 3.9."""

from functools import reduce

# Estado inmutable de la búsqueda: (low, high, índice encontrado o -1).
# Python 3.9 (PEP 585): genéricos nativos como tuple[int, int, int], sin typing.Tuple.
State = tuple[int, int, int]


def binary_search(arr: list[int], target: int) -> int:
    """Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está."""

    def step(state: State, _: int) -> State:
        """Un paso de la búsqueda: recibe un estado y devuelve uno nuevo, sin mutar nada."""
        low, high, found = state
        if found != -1 or low > high:   # ya terminó: el estado no cambia
            return state
        mid = (low + high) // 2
        if arr[mid] == target:
            return (low, high, mid)
        if arr[mid] < target:
            return (mid + 1, high, -1)  # el objetivo solo puede estar a la derecha
        return (low, mid - 1, -1)       # el objetivo solo puede estar a la izquierda

    # Una búsqueda binaria sobre n elementos necesita como mucho n.bit_length()
    # pasos (⌊log2 n⌋ + 1), así que reduce aplica `step` exactamente ese número de veces.
    _, _, found = reduce(step, range(len(arr).bit_length()), (0, len(arr) - 1, -1))
    return found


if __name__ == "__main__":
    test_cases: tuple[tuple[str, list[int], int, int], ...] = (
        # Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        ("objetivo presente", [1, 3, 5, 7, 9], 5, 2),
        # Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        ("objetivo ausente", [1, 3, 5, 7, 9], 4, -1),
        # Caso 3: arreglo vacío
        ("arreglo vacío", [], 1, -1),
    )

    # map calcula el resultado de cada caso; filter se queda solo con los que fallan.
    results = map(lambda case: (case, binary_search(case[1], case[2])), test_cases)
    failures = tuple(filter(lambda pair: pair[1] != pair[0][3], results))

    assert not failures, "\n".join(
        map(lambda pair: f"{pair[0][0]}: esperado {pair[0][3]}, obtenido {pair[1]}", failures)
    )
    print("Todas las pruebas pasaron.")