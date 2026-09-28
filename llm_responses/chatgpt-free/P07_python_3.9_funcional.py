# 2D Flood Fill — Python 3.9

# Paradigma: estrictamente funcional

# Sin mutación de la matriz original.

#

# Se utilizan:

# - map() para transformar las filas y posiciones.

# - filter() para conservar vecinos válidos.

# - functools.reduce() para propagar el Flood Fill.

#

# Python 3.9 permite utilizar tipos genéricos incorporados

# como list[list[int]].

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

def valid_neighbors(position):
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

def update_filled_positions(state, position):
    filled, frontier = state

    if position in filled:
        return filled, frontier

    new_filled = filled + (position,)
    new_frontier = frontier + valid_neighbors(position)

    return new_filled, new_frontier

# Se utiliza reduce() para explorar las posiciones alcanzables.
#
# El estado contiene:
# - filled: posiciones ya alcanzadas.
# - frontier: posiciones pendientes de procesar.
#
# filter() elimina posiciones repetidas y ya visitadas.
def expand(state, _):
    filled, frontier = state

    next_positions = tuple(
        filter(
            lambda position: position not in filled,
            frontier
        )
    )

    if not next_positions:
        return filled, ()

    return reduce(
        update_filled_positions,
        next_positions,
        (filled, ())
    )

initial_state = ((row, col),)

filled_positions = reduce(
    expand,
    range(rows * cols),
    ((), initial_state)
)[0]

def transform_row(row_index):
    def transform_cell(col_index):
        if (row_index, col_index) in filled_positions:
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
