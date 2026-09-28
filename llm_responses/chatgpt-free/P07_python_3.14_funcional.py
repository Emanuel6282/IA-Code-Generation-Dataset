# 2D Flood Fill — Python 3.14

# Paradigma: estrictamente funcional

#

# Se utilizan:

# - map() para transformar posiciones y construir la matriz.

# - filter() para seleccionar vecinos válidos.

# - functools.reduce() para propagar el Flood Fill.

#

# No se utilizan for, while ni recursión.

# La matriz original permanece sin modificaciones.

from functools import reduce

def flood_fill(
grid: list[list[int]],
row: int,
col: int,
new_color: int
) -> list[list[int]]:
original_color = grid[row][col]

if original_color == new_color:
    return grid

rows = len(grid)
cols = len(grid[0])

directions = (
    (-1, 0),
    (1, 0),
    (0, -1),
    (0, 1)
)

def get_neighbors(position):
    current_row, current_col = position

    candidates = map(
        lambda direction: (
            current_row + direction[0],
            current_col + direction[1]
        ),
        directions
    )

    return tuple(
        filter(
            lambda position: (
                0 <= position[0] < rows
                and 0 <= position[1] < cols
                and grid[position[0]][position[1]] == original_color
            ),
            candidates
        )
    )

def visit_position(state, position):
    visited, pending = state

    if position in visited:
        return visited, pending

    new_visited = visited + (position,)

    new_neighbors = tuple(
        filter(
            lambda candidate: (
                candidate not in new_visited
                and candidate not in pending
            ),
            get_neighbors(position)
        )
    )

    return (
        new_visited,
        pending + new_neighbors
    )

def expand(state, _):
    visited, pending = state

    if not pending:
        return state

    current = pending[0]
    remaining = pending[1:]

    return visit_position(
        (visited, remaining),
        current
    )

initial_state = ((), ((row, col),))

visited, _ = reduce(
    expand,
    range(rows * cols),
    initial_state
)

def transform_row(row_index):
    def transform_cell(col_index):
        if (row_index, col_index) in visited:
            return new_color

        return grid[row_index][col_index]

    return list(
        map(
            transform_cell,
            range(cols)
        )
    )

return list(
    map(
        transform_row,
        range(rows)
    )
)

# Casos de prueba

assert flood_fill(
[[1, 1, 1], [1, 1, 0], [1, 0, 1]],
1,
1,
2
) == [
[2, 2, 2],
[2, 2, 0],
[2, 0, 1]
]

assert flood_fill(
[[0, 0], [0, 1]],
0,
0,
2
) == [
[2, 2],
[2, 1]
]

assert flood_fill(
[[1, 1], [1, 1]],
0,
0,
1
) == [
[1, 1],
[1, 1]
]

print("All tests passed")
