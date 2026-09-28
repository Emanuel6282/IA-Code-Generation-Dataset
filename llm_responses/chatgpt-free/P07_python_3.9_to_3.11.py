# P07: Flood Fill — Python 3.11

#

# Características utilizadas:

# 1. Tipado incorporado genérico (PEP 585):

# list[list[int]] y evita la importación de typing.List.

# Es más conciso y forma parte de la sintaxis moderna de Python.

#

# 2. Anotaciones de tipo más limpias:

# Se utiliza list[list[int]] -> list[list[int]] para expresar

# directamente el tipo de entrada y salida.

#

# Nota:

# Python 3.11 no introduce una característica específica que mejore

# materialmente el algoritmo Flood Fill. Se mantiene deque y el

# recorrido BFS porque son apropiados para este problema.

from collections import deque

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

queue = deque()
queue.append((row, col))

grid[row][col] = new_color

directions = (
    (-1, 0),
    (1, 0),
    (0, -1),
    (0, 1)
)

while queue:
    r, c = queue.popleft()

    for dr, dc in directions:
        nr = r + dr
        nc = c + dc

        if (
            0 <= nr < rows
            and 0 <= nc < cols
            and grid[nr][nc] == original_color
        ):
            grid[nr][nc] = new_color
            queue.append((nr, nc))

return grid

# Tests

assert flood_fill(
[[1,1,1],[1,1,0],[1,0,1]],
1,
1,
2
) == [[2,2,2],[2,2,0],[2,0,1]]

assert flood_fill(
[[0]],
0,
0,
5
) == [[5]]

assert flood_fill(
[[0,0],[0,0]],
0,
0,
3
) == [[3,3],[3,3]]

# Cambios realizados:

# 1. Se actualizó la versión objetivo de Python 3.9 a Python 3.11.

#

# 2. Se reemplazó:

# from typing import List

# List[List[int]]

# por:

# list[list[int]]

# aprovechando la sintaxis de genéricos incorporados de Python.

#

# 3. Se mantuvo collections.deque porque continúa siendo la estructura

# idiomática para implementar una cola FIFO eficiente en BFS.

#

# 4. Se mantuvo el algoritmo Flood Fill mediante BFS iterativo.

# Esto conserva exactamente la lógica y los resultados del código base.

#

# 5. Se extrajo la lista de direcciones a una tupla reutilizable para

# evitar reconstruirla en cada iteración.

#

# 6. Se conservaron exactamente los tres casos de prueba y sus resultados.

#

# 7. No se introdujeron características de Python 3.11 de forma artificial:

# features como Exception Groups o Self no aportan beneficios a este

# algoritmo.
