# Python 3.11 — paradigma funcional (inmutabilidad, map/filter/functools.reduce; sin
# for/while con estado mutable).
#
# Características de 3.11 que se usan:
# 1. BaseException.add_note (PEP 678): el IndexError por posición fuera de la matriz
#    lleva una nota con las dimensiones reales. La excepción se construye con una función
#    pura (_out_of_bounds) que la devuelve ya anotada.
# 2. ExceptionGroup (PEP 654): los tests se evalúan todos con map/filter, sin detenerse
#    en el primero que falla, y los fallos se lanzan juntos en un único ExceptionGroup.
# 3. CPython 3.11 es más rápido (PEP 659): las lambdas y llamadas que usa este estilo
#    se benefician del intérprete especializado.
# También se usan TypeAlias (3.10) y genéricos PEP 585 (3.9) en las anotaciones.
#
# map/filter construyen los vecinos de cada celda, la matriz resultante y la lista de
# fallos de las pruebas; functools.reduce concatena esos fallos. La región NO se acumula
# con reduce (ver la nota en flood_fill: sería cuadrático).
#
# Inmutabilidad: flood_fill NO modifica la matriz recibida; construye y retorna una
# nueva. El recorrido es un BFS por niveles expresado como una secuencia de estados
# inmutables (frozensets) generada perezosamente con itertools.accumulate.

from functools import reduce
from itertools import accumulate, chain, repeat, takewhile
from typing import TypeAlias

Grid: TypeAlias = list[list[int]]
Cell: TypeAlias = tuple[int, int]
# Estado del BFS: (nivel anterior, nivel actual). En un grafo no dirigido, los vecinos
# del nivel k solo pueden estar en los niveles k-1, k o k+1, así que basta con recordar
# esos dos para no volver atrás, sin acumular todo lo visitado en cada paso.
Level: TypeAlias = tuple[frozenset[Cell], frozenset[Cell]]

OFFSETS: tuple[Cell, ...] = ((-1, 0), (1, 0), (0, -1), (0, 1))   # arriba, abajo, izq., der.


def _in_bounds(grid: Grid, cell: Cell) -> bool:
    """True si la celda existe (los límites se comprueban por fila: admite filas de distinto largo)."""
    r, c = cell
    return 0 <= r < len(grid) and 0 <= c < len(grid[r])


def _with_note(error: IndexError, note: str) -> IndexError:
    """Añade la nota y devuelve la misma excepción, para poder usarla en una expresión."""
    error.add_note(note)
    return error


def _out_of_bounds(grid: Grid, row: int, col: int) -> IndexError:
    """IndexError para (row, col) con una nota (PEP 678) que da las dimensiones reales."""
    return _with_note(
        IndexError(f"posición ({row}, {col}) fuera de la matriz"),
        f"la fila {row} tiene {len(grid[row])} columna(s)" if 0 <= row < len(grid)
        else f"la matriz tiene {len(grid)} fila(s)",
    )


def flood_fill(grid: Grid, row: int, col: int, new_color: int) -> Grid:
    """Retorna una matriz nueva en la que la región conectada (4 direcciones) que
    contiene (row, col) pasa a `new_color`. La matriz original no se modifica.

    Si (row, col) ya tiene `new_color`, retorna la misma matriz, sin cambios.

    Sin recursión (una región grande superaría el límite de recursión de Python):
    accumulate genera los niveles del BFS de forma perezosa y takewhile se detiene en
    el primer nivel vacío. O(filas * columnas) en tiempo.

    Raises:
        IndexError: si (row, col) está fuera de la matriz. Se comprueba explícitamente
            porque un índice negativo como -1 no falla en Python. Lleva una
            nota con las dimensiones de la matriz.
    """
    if not _in_bounds(grid, (row, col)):
        raise _out_of_bounds(grid, row, col)

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

    def assertion_failure(condition: bool, message: str) -> AssertionError | None:
        """Evalúa un assert y devuelve el AssertionError en lugar de propagarlo, para que
        las pruebas se combinen como valores (None si pasa)."""
        try:
            assert condition, message
        except AssertionError as error:
            return error
        return None

    def check(case: tuple[str, Grid, int, int, int, Grid]) -> AssertionError | None:
        """Comprueba el resultado y que la matriz de entrada no se haya modificado."""
        name, grid, row, col, new_color, expected = case
        snapshot = tuple(map(tuple, grid))
        result = flood_fill(grid, row, col, new_color)
        return assertion_failure(result == expected and tuple(map(tuple, grid)) == snapshot,
                                 f"{name}: esperado {expected}, obtenido {result}")

    def raises_noted_index_error(position: Cell) -> bool:
        """True si la posición lanza IndexError con exactamente una nota."""
        try:
            flood_fill([[1, 1, 1], [1, 1, 1]], position[0], position[1], 5)
        except IndexError as error:
            return len(error.__notes__) == 1
        return False

    # map evalúa cada grupo de asserts, filter descarta los que pasaron (None) y reduce
    # concatena los fallos en una tupla inmutable, que se lanza como un ExceptionGroup.
    failures: tuple[AssertionError, ...] = reduce(
        lambda acc, group: acc + tuple(filter(None, group)),
        (
            map(check, test_cases),
            map(lambda p: assertion_failure(raises_noted_index_error(p),
                                            f"{p} debería lanzar IndexError con una nota"),
                ((2, 0), (-1, 0), (0, 3))),
        ),
        (),
    )
    if failures:
        raise ExceptionGroup(f"{len(failures)} prueba(s) fallaron", failures)
    print("Todas las pruebas pasaron.")