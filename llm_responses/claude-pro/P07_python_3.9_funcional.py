# Python 3.9 — paradigma funcional (inmutabilidad, map/filter/functools.reduce; sin
# for/while con estado mutable).
#
# Característica de 3.9 que se usa: genéricos de la biblioteca estándar en las anotaciones
# (PEP 585): `list[list[int]]`, `tuple[int, int]`, `frozenset[Cell]` directamente, sin
# importar `List`, `Tuple` ni `FrozenSet` de `typing`, incluidos los alias de tipo de
# nivel superior (válidos en tiempo de ejecución desde 3.9). Otras novedades de 3.9
# (unión de diccionarios con `|`, str.removeprefix, zoneinfo) no tienen uso aquí.
#
# map/filter construyen los vecinos de cada celda y la matriz resultante; functools.reduce
# combina los resultados de las pruebas. La región NO se acumula con reduce (ver la nota
# en flood_fill: sería cuadrático).
#
# Inmutabilidad: flood_fill NO modifica la matriz recibida; construye y retorna una
# nueva. El recorrido es un BFS por niveles expresado como una secuencia de estados
# inmutables (frozensets) generada perezosamente con itertools.accumulate.

from functools import reduce
from itertools import accumulate, chain, repeat, takewhile

Grid = list[list[int]]
Cell = tuple[int, int]
# Estado del BFS: (nivel anterior, nivel actual). En un grafo no dirigido, los vecinos
# del nivel k solo pueden estar en los niveles k-1, k o k+1, así que basta con recordar
# esos dos para no volver atrás, sin acumular todo lo visitado en cada paso.
Level = tuple[frozenset[Cell], frozenset[Cell]]

OFFSETS: tuple[Cell, ...] = ((-1, 0), (1, 0), (0, -1), (0, 1))   # arriba, abajo, izq., der.


def _in_bounds(grid: Grid, cell: Cell) -> bool:
    """True si la celda existe (los límites se comprueban por fila: admite filas de distinto largo)."""
    r, c = cell
    return 0 <= r < len(grid) and 0 <= c < len(grid[r])


def flood_fill(grid: Grid, row: int, col: int, new_color: int) -> Grid:
    """Retorna una matriz nueva en la que la región conectada (4 direcciones) que
    contiene (row, col) pasa a `new_color`. La matriz original no se modifica.

    Si (row, col) ya tiene `new_color`, retorna la misma matriz, sin cambios.

    Sin recursión (una región grande superaría el límite de recursión de Python):
    accumulate genera los niveles del BFS de forma perezosa y takewhile se detiene en
    el primer nivel vacío. O(filas * columnas) en tiempo.

    Raises:
        IndexError: si (row, col) está fuera de la matriz. Se comprueba explícitamente
            porque un índice negativo como -1 no falla en Python.
    """
    if not _in_bounds(grid, (row, col)):
        raise IndexError(f"posición ({row}, {col}) fuera de la matriz")

    original = grid[row][col]
    if original == new_color:
        return grid

    same_color = lambda cell: _in_bounds(grid, cell) and grid[cell[0]][cell[1]] == original
    neighbors = lambda cell: filter(same_color, map(lambda d: (cell[0] + d[0], cell[1] + d[1]), OFFSETS))

    def next_level(state: Level, _: None) -> Level:
        previous, current = state
        candidates = frozenset(chain.from_iterable(map(neighbors, current)))
        return current, candidates - current - previous

    start: Level = (frozenset(), frozenset({(row, col)}))
    levels = takewhile(lambda state: state[1], accumulate(repeat(None), next_level, initial=start))
    # Región = unión de todos los niveles. Se aplana con chain en un único frozenset, en
    # O(n). Un reduce(frozenset.union, ...) sería O(n^2): cada unión copia todo lo
    # acumulado (medido: ~94 s para 1000 x 1000 frente a ~3,4 s).
    region = frozenset(chain.from_iterable(map(lambda state: state[1], levels)))

    return list(map(
        lambda r: list(map(lambda c: new_color if (r, c) in region else grid[r][c], range(len(grid[r])))),
        range(len(grid)),
    ))


if __name__ == "__main__":
    test_cases: tuple[tuple[str, Grid, int, int, int, Grid], ...] = (
        # Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
        ("ejemplo del enunciado",
         [[1, 1, 1], [1, 1, 0], [1, 0, 1]], 1, 1, 2,
         [[2, 2, 2], [2, 2, 0], [2, 0, 1]]),
        # Caso 2: el color ya es el nuevo -> la matriz no cambia
        ("color ya es el nuevo",
         [[0, 0, 0], [0, 1, 1]], 1, 1, 1,
         [[0, 0, 0], [0, 1, 1]]),
        # Caso 3: no se rellena en diagonal
        ("sin diagonales",
         [[1, 0], [0, 1]], 0, 0, 3,
         [[3, 0], [0, 1]]),
    )

    def check(case: tuple[str, Grid, int, int, int, Grid]) -> str:
        """'' si el caso pasa; si no, el mensaje de error. Comprueba también que la
        matriz de entrada no se haya modificado."""
        name, grid, row, col, new_color, expected = case
        snapshot = tuple(map(tuple, grid))
        result = flood_fill(grid, row, col, new_color)
        return ("" if result == expected and tuple(map(tuple, grid)) == snapshot
                else f"{name}: esperado {expected}, obtenido {result}")

    def raises_index_error(position: Cell) -> bool:
        try:
            flood_fill([[1, 1, 1], [1, 1, 1]], position[0], position[1], 5)
        except IndexError:
            return True
        return False

    # map + filter producen los mensajes de fallo de cada grupo de pruebas y
    # functools.reduce los concatena en una sola tupla inmutable.
    failures = reduce(
        lambda acc, group: acc + tuple(group),
        (
            filter(None, map(check, test_cases)),
            map(lambda p: f"{p} debería lanzar IndexError",
                filter(lambda p: not raises_index_error(p), ((2, 0), (-1, 0), (0, 3)))),
        ),
        (),
    )
    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")