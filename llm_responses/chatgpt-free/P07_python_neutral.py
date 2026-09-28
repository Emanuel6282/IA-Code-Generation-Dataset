from collections import deque

def flood_fill(grid, row, col, new_color):
original_color = grid[row][col]

if original_color == new_color:
    return grid

rows = len(grid)
cols = len(grid[0])

queue = deque([(row, col)])
grid[row][col] = new_color

directions = [
    (-1, 0),  # arriba
    (1, 0),   # abajo
    (0, -1),  # izquierda
    (0, 1),   # derecha
]

while queue:
    current_row, current_col = queue.popleft()

    for row_offset, col_offset in directions:
        next_row = current_row + row_offset
        next_col = current_col + col_offset

        if (
            0 <= next_row < rows
            and 0 <= next_col < cols
            and grid[next_row][next_col] == original_color
        ):
            grid[next_row][next_col] = new_color
            queue.append((next_row, next_col))

return grid

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
