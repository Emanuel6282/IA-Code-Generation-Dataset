# Python 3.11 — paradigma iterativo (while/for con estado mutable, sin map/filter/reduce
# ni recursión).
#
# Características de 3.11 que se usan:
# 1. BaseException.add_note (PEP 678): el IndexError por posición fuera de la matriz
#    lleva una nota con las dimensiones reales, que se muestra en el traceback sin
#    alterar el mensaje principal.
# 2. ExceptionGroup (PEP 654): los tests no se detienen en el primer assert que falla;
#    se ejecutan todos y, si alguno falla, se lanzan juntos en un único ExceptionGroup.
# 3. CPython 3.11 es más rápido en bucles de Python puro (PEP 659, intérprete
#    especializado): favorece directamente a este estilo iterativo.
# También se usan TypeAlias (3.10) y genéricos PEP 585 (3.9) en las anotaciones.

from typing import TypeAlias

Grid: TypeAlias = list[list[int]]
Cell: TypeAlias = tuple[int, int]

OFFSETS: tuple[Cell, ...] = ((-1, 0), (1, 0), (0, -1), (0, 1))   # arriba, abajo, izq., der.


def flood_fill(grid: Grid, row: int, col: int, new_color: int) -> Grid:
    """Rellena con `new_color` la región conectada (4 direcciones) que contiene (row, col).

    La matriz se modifica en el lugar y se retorna la misma lista.

    Se recorre con una pila explícita y un bucle while, sin recursión: una región grande
    (por ejemplo, 1000 x 1000 de un solo color) superaría el límite de recursión de
    Python. Cada celda se pinta al apilarla, así que no se visita dos veces.
    O(filas * columnas) en tiempo.

    Raises:
        IndexError: si (row, col) está fuera de la matriz. Se comprueba explícitamente
            porque un índice negativo como -1 no falla en Python: apuntaría a la última
            fila o columna.
    """
    if row < 0 or row >= len(grid) or col < 0 or col >= len(grid[row]):
        error = IndexError(f"posición ({row}, {col}) fuera de la matriz")
        if 0 <= row < len(grid):
            error.add_note(f"la fila {row} tiene {len(grid[row])} columna(s)")
        else:
            error.add_note(f"la matriz tiene {len(grid)} fila(s)")
        raise error

    original = grid[row][col]
    # Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación el
    # bucle no terminaría, porque las celdas pintadas seguirían teniendo el color original.
    if original == new_color:
        return grid

    stack: list[Cell] = [(row, col)]
    grid[row][col] = new_color

    while stack:
        r, c = stack.pop()
        for dr, dc in OFFSETS:
            nr = r + dr
            nc = c + dc
            # Límites comprobados por fila: funciona aunque las filas tengan distinto largo.
            if 0 <= nr < len(grid) and 0 <= nc < len(grid[nr]) and grid[nr][nc] == original:
                grid[nr][nc] = new_color
                stack.append((nr, nc))
    return grid


def _copy_grid(grid: Grid) -> Grid:
    """Copia la matriz fila por fila con un bucle (para no compartir listas entre tests)."""
    copy: Grid = []
    for line in grid:
        copy.append(line.copy())
    return copy


if __name__ == "__main__":
    test_cases: list[tuple[str, Grid, int, int, int, Grid]] = [
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
    ]

    failures: list[AssertionError] = []

    for name, grid, row, col, new_color, expected in test_cases:
        working = _copy_grid(grid)
        try:
            result = flood_fill(working, row, col, new_color)
            assert result == expected, f"{name}: esperado {expected}, obtenido {result}"
            assert result is working, f"{name}: debe modificar y retornar la misma matriz"
        except AssertionError as error:
            failures.append(error)

    # Posición fuera de la matriz (incluido un índice negativo) -> IndexError con nota
    for bad_row, bad_col in [(2, 0), (-1, 0), (0, 3)]:
        try:
            raised = False
            try:
                flood_fill([[1, 1, 1], [1, 1, 1]], bad_row, bad_col, 5)
            except IndexError as error:
                raised = len(error.__notes__) == 1
            assert raised, f"({bad_row}, {bad_col}) debería lanzar IndexError con una nota"
        except AssertionError as error:
            failures.append(error)

    if failures:
        raise ExceptionGroup(f"{len(failures)} prueba(s) fallaron", failures)
    print("Todas las pruebas pasaron.")