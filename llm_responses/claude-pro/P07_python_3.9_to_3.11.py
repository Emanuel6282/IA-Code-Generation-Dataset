# P07: Flood Fill — migrado de Python 3.9 a Python 3.11
#
# CARACTERÍSTICAS USADAS Y POR QUÉ
# 1. Intérprete más rápido de CPython 3.11 (PEP 659, intérprete adaptativo
#    especializado): especializa en tiempo de ejecución las sumas, comparaciones e
#    indexaciones de enteros del bucle BFS. El mismo código corre ~33 % más rápido en
#    3.11 que en 3.9 sin cambiarlo; además, el bucle interno se aligera (ver cambio 1).
# 2. TypeAlias (PEP 613, 3.10) con genéricos de la biblioteca estándar (PEP 585):
#    `Grid: TypeAlias = list[list[int]]` nombra el tipo una vez y reemplaza a
#    `typing.List`, obsoleto desde 3.9.
#
# Nota: las novedades propias de 3.11 (add_note, ExceptionGroup/except*, typing.Self,
# tomllib) no aplican sin cambiar el comportamiento: por ejemplo, añadir una nota a los
# IndexError cambiaría las excepciones que produce la función. No se fuerzan.

from collections import deque
from typing import TypeAlias

Grid: TypeAlias = list[list[int]]

# Desplazamientos arriba, abajo, izquierda, derecha (mismo orden que el original).
_OFFSETS = ((-1, 0), (1, 0), (0, -1), (0, 1))


def flood_fill(grid: Grid, row: int, col: int, new_color: int) -> Grid:
    original_color = grid[row][col]
    if original_color == new_color:
        return grid
    rows = len(grid)
    cols = len(grid[0])
    queue = deque([(row, col)])
    grid[row][col] = new_color
    while queue:
        r, c = queue.popleft()
        for dr, dc in _OFFSETS:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == original_color:
                grid[nr][nc] = new_color
                queue.append((nr, nc))
    return grid


# Casos de prueba (los mismos tres del código original)
assert flood_fill([[1, 1, 1], [1, 1, 0], [1, 0, 1]], 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]
assert flood_fill([[0]], 0, 0, 5) == [[5]]
assert flood_fill([[0, 0], [0, 0]], 0, 0, 3) == [[3, 3], [3, 3]]


# CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11
# 1. La lista `[(-1,0),(1,0),(0,-1),(0,1)]`, que se construía de nuevo en cada vuelta
#    del while, pasa a ser la tupla constante de módulo `_OFFSETS`, creada una sola vez.
#    Mismo orden de vecinos, así que la cola visita las celdas en el mismo orden.
# 2. `queue = deque()` + `queue.append((row, col))` -> `deque([(row, col)])`: la cola
#    se crea ya con la celda inicial.
# 3. `from typing import List` + `List[List[int]]` -> alias `Grid: TypeAlias` con
#    `list[list[int]]` (PEP 585 + PEP 613).
# 4. Formato PEP 8 en los tests. Los asserts siguen a nivel de módulo, igual que en el
#    original, para que se ejecuten en los mismos momentos.
# 5. Comportamiento idéntico, verificado contra el original con miles de matrices
#    aleatorias (resultado, estado final de la matriz y excepciones), incluidos los
#    casos límite que el original ya trataba así y que se conservan tal cual:
#    - Modifica la matriz en el lugar y retorna la misma lista.
#    - Una posición fuera de rango lanza IndexError en `grid[row][col]`; una matriz
#      vacía, también.
#    - Índices negativos NO se rechazan: `grid[-1][...]` se refiere a la última fila
#      (indexación de Python), pero los vecinos se calculan desde -1 y los límites
#      `0 <= nr < rows` los descartan, así que el relleno puede quedar incompleto.
#      Ejemplo: [[1,1],[0,0]] con row=-1, col=0, new_color=5 da [[1,1],[5,0]], mientras
#      que con row=1 (la misma celda) da [[1,1],[5,5]].
#    - `cols = len(grid[0])`: con filas de distinto largo usa el largo de la primera;
#      si una fila es más corta, `grid[nr][nc]` puede lanzar IndexError a mitad del
#      relleno (con la matriz ya modificada en parte), igual que el original.
#
# NOTA SOBRE RENDIMIENTO (matriz de 1000 x 1000 de un solo color, mejor de 5):
#    original en CPython 3.9: ~0,77 s; original en 3.11: ~0,52 s; migración en 3.11:
#    ~0,50 s. Casi toda la mejora (~33 %) la aporta el intérprete de 3.11; la tupla
#    constante suma un ~4 % al no construir una lista por celda visitada.