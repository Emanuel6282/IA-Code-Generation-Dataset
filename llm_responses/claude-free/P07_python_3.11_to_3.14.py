# Caracteristicas nuevas de Python 3.14 utilizadas y por que:
# 1. Tipos genericos nativos (list[list[int]]), que en 3.14 se benefician de
#    la evaluacion diferida de anotaciones (PEP 649, activa por defecto en
#    3.14), permitiendo anotaciones de tipo mas expresivas sin costo en
#    tiempo de importacion; se mantiene el uso de "list[list[int]]" en vez de
#    "typing.List[List[int]]" por ser la forma idiomatica y ya vigente desde
#    3.9, ahora reforzada por este cambio en el interprete.
# 2. Se evaluo el uso de template strings (t-strings, PEP 750), la novedad mas
#    destacada de 3.14, pero no aplica aqui: no hay construccion de cadenas de
#    texto en el algoritmo, por lo que introducirlas seria artificial.
# 3. Se evaluo el nuevo build libre de GIL (free-threading) y el REPL mejorado,
#    pero son caracteristicas de entorno/ejecucion, no cambios de codigo
#    fuente aplicables a este algoritmo secuencial y de un solo hilo.
# 4. Se mantiene el paradigma iterativo original (BFS con deque), ya que esta
#    es una migracion de version, no un cambio de paradigma.

from collections import deque


def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    original_color = grid[row][col]
    if original_color == new_color:
        return grid
    rows = len(grid)
    cols = len(grid[0])
    queue = deque()
    queue.append((row, col))
    grid[row][col] = new_color
    while queue:
        r, c = queue.popleft()
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == original_color:
                grid[nr][nc] = new_color
                queue.append((nr, nc))
    return grid


assert flood_fill([[1, 1, 1], [1, 1, 0], [1, 0, 1]], 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]
assert flood_fill([[0]], 0, 0, 5) == [[5]]
assert flood_fill([[0, 0], [0, 0]], 0, 0, 3) == [[3, 3], [3, 3]]

# Cambios realizados y justificacion:
# 1. Se agrego el comentario inicial documentando las caracteristicas de
#    Python 3.14 evaluadas, explicando por que la mayoria no aplican a este
#    algoritmo concreto.
# 2. Se elimino "from typing import List" y se reemplazaron las anotaciones
#    "List[List[int]]" por "list[list[int]]", consistente con las practicas
#    idiomaticas ya establecidas y compatibles con la evaluacion diferida de
#    anotaciones de 3.14.
# 3. La logica del algoritmo (BFS con deque, misma condicion de color, mismo
#    recorrido en 4 direcciones) se mantuvo exactamente igual, preservando
#    comportamiento y resultados identicos a la version 3.11.
# 4. El nombre de la funcion se mantuvo exactamente como "flood_fill".
# 5. Se conservaron los 3 casos de prueba originales con assert, sin alterar
#    los valores esperados ni el mecanismo de verificacion.
# 6. No se introdujeron t-strings, free-threading ni otras novedades de 3.14
#    porque el problema no involucra construccion de texto ni concurrencia
#    que justifique su uso; anadirlas seria forzado y no aportaria claridad.