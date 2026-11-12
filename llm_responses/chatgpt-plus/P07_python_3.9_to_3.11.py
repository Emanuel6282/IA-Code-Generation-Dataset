# P07: Flood Fill — Python 3.11
#
# CARACTERÍSTICAS Y MEJORAS:
#
# 1. Python 3.11 — PEP 659:
#    El intérprete incorpora especialización adaptativa
#    para optimizar operaciones frecuentes.
#    Puede beneficiar los bucles y accesos a listas
#    sin modificar el código del algoritmo.
#
# 2. Anotaciones genéricas integradas — PEP 585:
#    Se utiliza list[list[int]] en lugar de
#    typing.List[List[int]].
#    Esta sintaxis existe desde Python 3.9 y es
#    la forma idiomática en Python 3.11.
#
# 3. collections.deque:
#    Se conserva para implementar BFS eficientemente,
#    con extracción O(1) desde el inicio.
#
# 4. Compatibilidad:
#    Se mantienen la misma lógica, los mismos
#    resultados y los tres tests originales.

from collections import deque


def flood_fill(
    grid: list[list[int]],
    row: int,
    col: int,
    new_color: int
) -> list[list[int]]:

    # Obtener el color original.
    original_color = grid[row][col]

    # No modificar si el color ya coincide.
    if original_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Cola FIFO para BFS.
    queue: deque[tuple[int, int]] = deque()
    queue.append((row, col))

    # Marcar el píxel inicial como visitado.
    grid[row][col] = new_color

    # Recorrido BFS iterativo.
    while queue:
        r, c = queue.popleft()

        # Mantener exactamente las cuatro direcciones.
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
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


# ==================================================
# TESTS ORIGINALES — Sin modificaciones
# ==================================================

assert flood_fill(
    [[1, 1, 1], [1, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]

assert flood_fill(
    [[0]],
    0, 0, 5
) == [[5]]

assert flood_fill(
    [[0, 0], [0, 0]],
    0, 0, 3
) == [[3, 3], [3, 3]]


print("All tests passed")


# ==================================================
# CAMBIOS REALIZADOS EN LA MIGRACIÓN A PYTHON 3.11
# ==================================================
#
# 1. typing.List -> list:
#    Se reemplazó List[List[int]] por list[list[int]].
#    Esto elimina una importación innecesaria y utiliza
#    las anotaciones genéricas integradas (PEP 585).
#
# 2. Anotación de la cola:
#    Se añadió deque[tuple[int, int]] para expresar
#    explícitamente el tipo de las posiciones.
#
# 3. Formato del código:
#    Se mejoró la legibilidad de las condiciones
#    y de la firma de la función.
#
# 4. Algoritmo BFS:
#    Se conservó deque, append() y popleft().
#    No se sustituyó por DFS ni por recursión.
#
# 5. Python 3.11:
#    El intérprete puede aplicar especialización
#    adaptativa (PEP 659) automáticamente.
#    No se requieren cambios en el algoritmo.
#
# 6. Comportamiento:
#    Se mantiene la modificación in-place, la
#    conectividad de cuatro direcciones y el retorno
#    de la misma matriz.
#
# 7. Pruebas:
#    Se conservaron exactamente los tres assert
#    originales, con el mismo contenido y resultados.