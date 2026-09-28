# Caracteristicas nuevas de Python 3.11 utilizadas y por que:
# 1. Tipos genericos nativos (list[list[int]] en vez de typing.List[List[int]]),
#    disponibles desde PEP 585 (3.9+) y ya completamente idiomaticos en 3.11,
#    lo que permite eliminar el import de typing y reducir ruido sintactico.
# 2. Se evaluo el uso de "Exception Groups" (except*) y el nuevo tipo Self,
#    novedades destacadas de 3.11, pero no aplican aqui: no hay jerarquias de
#    excepciones multiples ni metodos que retornen la propia instancia, por lo
#    que forzarlas seria artificial para este algoritmo.
# 3. Se mantiene el paradigma iterativo original (BFS con deque), ya que esta
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
#    Python 3.11 consideradas y por que la mayoria no aplican a este algoritmo.
# 2. Se elimino "from typing import List" y se reemplazaron las anotaciones
#    "List[List[int]]" por "list[list[int]]", usando los tipos genericos
#    nativos, la forma idiomatica y recomendada en versiones recientes de
#    Python (incluida 3.11) frente al modulo typing para tipos basicos.
# 3. La logica del algoritmo (BFS con deque, misma condicion de color, mismo
#    recorrido en 4 direcciones) se mantuvo exactamente igual, preservando
#    comportamiento y resultados identicos a la version 3.9.
# 4. El nombre de la funcion se mantuvo exactamente como "flood_fill".
# 5. Se conservaron los 3 casos de prueba originales con assert, sin alterar
#    los valores esperados ni el mecanismo de verificacion.
# 6. No se introdujeron Exception Groups, Self, ni pattern matching (match/case)
#    porque el problema no involucra manejo de multiples excepciones ni
#    destructuring de tipos que justifique su uso; anadirlos seria forzado.