# Python 3.9 — paradigma iterativo (while/for con estado mutable, sin map/filter/reduce
# ni recursión).
#
# Característica de 3.9 que se usa: genéricos de la biblioteca estándar en las anotaciones
# (PEP 585): `list[list[int]]` y `tuple[int, int]` directamente, sin importar `List` ni
# `Tuple` de `typing`. Otras novedades de 3.9 (unión de diccionarios con `|`,
# str.removeprefix, zoneinfo, graphlib) no tienen uso en este problema.

Grid = list[list[int]]   # alias con genéricos PEP 585: válido en tiempo de ejecución desde 3.9


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
        raise IndexError(f"posición ({row}, {col}) fuera de la matriz")

    original = grid[row][col]
    # Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación el
    # bucle no terminaría, porque las celdas pintadas seguirían teniendo el color original.
    if original == new_color:
        return grid

    offsets: list[tuple[int, int]] = [(-1, 0), (1, 0), (0, -1), (0, 1)]   # arriba, abajo, izq., der.
    stack: list[tuple[int, int]] = [(row, col)]
    grid[row][col] = new_color

    while stack:
        r, c = stack.pop()
        for dr, dc in offsets:
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

    for name, grid, row, col, new_color, expected in test_cases:
        working = _copy_grid(grid)
        result = flood_fill(working, row, col, new_color)
        assert result == expected, f"{name}: esperado {expected}, obtenido {result}"
        assert result is working, f"{name}: debe modificar y retornar la misma matriz"

    # Posición fuera de la matriz (incluido un índice negativo) -> IndexError
    for bad_row, bad_col in [(2, 0), (-1, 0), (0, 3)]:
        raised = False
        try:
            flood_fill([[1, 1, 1], [1, 1, 1]], bad_row, bad_col, 5)
        except IndexError:
            raised = True
        assert raised, f"({bad_row}, {bad_col}) debería lanzar IndexError"

    print("Todas las pruebas pasaron.")