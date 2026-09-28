# P07: Flood Fill — Python 3.14

#

# Características utilizadas:

# 1. Genéricos incorporados: list[list[int]]

# Permiten expresar los tipos directamente sin importar List desde typing.

#

# 2. Anotaciones de tipo modernas:

# La función utiliza list[list[int]] como tipo de entrada y salida,

# manteniendo una sintaxis concisa e idiomática.

#

# Nota:

# Python 3.14 no introduce una característica que mejore materialmente

# este algoritmo BFS. Características nuevas como template strings (t-strings)

# o functools.Placeholder no aportan una mejora al problema, por lo que

# no se utilizan artificialmente.

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

# 1. Se actualizó la versión objetivo de Python 3.11 a Python 3.14.

#

# 2. Se eliminó:

# from typing import List

# y se utilizaron genéricos incorporados:

# list[list[int]]

# para mantener las anotaciones modernas y concisas.

#

# 3. Se mantuvo collections.deque porque proporciona operaciones

# popleft() eficientes y es apropiado para implementar la cola FIFO

# utilizada por BFS.

#

# 4. Se convirtió la lista de direcciones en una tupla reutilizable.

# Esto evita reconstruir la colección de direcciones durante cada

# iteración del recorrido.

#

# 5. Se mantuvo exactamente el algoritmo Flood Fill basado en BFS.

# La lógica de recorrido, conectividad y modificación de la matriz

# permanece igual.

#

# 6. Se mantiene la modificación in-place de grid, por lo que el

# comportamiento observable es idéntico al código base.

#

# 7. Se conservaron los mismos tres casos de prueba y los mismos resultados.

#

# 8. No se utilizan características exclusivas de Python 3.14 de forma

# artificial, porque no ofrecen una mejora relevante para este algoritmo.
