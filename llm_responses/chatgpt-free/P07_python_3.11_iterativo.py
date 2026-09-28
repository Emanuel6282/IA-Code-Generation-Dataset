# 2D Flood Fill — Python 3.11

# Paradigma: estrictamente iterativo

# Algoritmo: Flood Fill mediante DFS iterativo con una pila.

#

# Complejidad:

# - Tiempo: O(m * n) en el peor caso.

# - Espacio adicional: O(m * n) en el peor caso.

def flood_fill(
grid: list[list[int]],
row: int,
col: int,
new_color: int
) -> list[list[int]]:
original_color = grid[row][col]

# Si el color ya es el nuevo color, no hay nada que modificar.
if original_color == new_color:
    return grid

rows = len(grid)
cols = len(grid[0])

# Pila para realizar el recorrido DFS de forma iterativa.
stack = [(row, col)]

# Marcamos la posición inicial al agregarla a la pila.
grid[row][col] = new_color

# Únicamente se permiten movimientos en cuatro direcciones.
directions = [
    (-1, 0),  # arriba
    (1, 0),   # abajo
    (0, -1),  # izquierda
    (0, 1)    # derecha
]

while stack:
    current_row, current_col = stack.pop()

    for row_offset, col_offset in directions:
        next_row = current_row + row_offset
        next_col = current_col + col_offset

        if (
            0 <= next_row < rows
            and 0 <= next_col < cols
            and grid[next_row][next_col] == original_color
        ):
            grid[next_row][next_col] = new_color
            stack.append((next_row, next_col))

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
