# P07: Flood Fill — Python 3.14
#
# CARACTERÍSTICAS Y MEJORAS:
#
# 1. Python 3.14 — PEP 649 y PEP 749:
#    Evaluación diferida de anotaciones de tipos.
#    Permite que las anotaciones se evalúen cuando
#    sean necesarias, sin afectar la lógica de BFS.
#
# 2. Anotaciones genéricas integradas — PEP 585:
#    Se utiliza list[list[int]] en lugar de
#    typing.List[List[int]].
#    Esta sintaxis existe desde Python 3.9 y
#    continúa siendo idiomática en Python 3.14.
#
# 3. Anotación de la cola:
#    deque[tuple[int, int]] describe claramente
#    que la cola almacena posiciones (fila, columna).
#
# 4. collections.deque:
#    Se conserva porque permite operaciones FIFO
#    eficientes mediante append() y popleft().
#
# 5. Compatibilidad:
#    Se mantienen el algoritmo BFS, la modificación
#    in-place, las cuatro direcciones y los tests.
#
# Biblioteca estándar únicamente.

from collections import deque


def flood_fill(
    grid: list[list[int]],
    row: int,
    col: int,
    new_color: int
) -> list[list[int]]:

    # Obtener el color original.
    original_color = grid[row][col]

    # Si el color ya coincide, devolver sin modificar.
    if original_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Cola FIFO para realizar BFS.
    queue: deque[tuple[int, int]] = deque()
    queue.append((row, col))

    # Cambiar el color inicial inmediatamente.
    grid[row][col] = new_color

    # Recorrido BFS iterativo.
    while queue:
        r, c = queue.popleft()

        # Conectividad de cuatro direcciones.
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            nr = r + dr
            nc = c + dc

            # Verificar límites y color original.
            if (
                0 <= nr < rows
                and 0 <= nc < cols
                and grid[nr][nc] == original_color
            ):
                grid[nr][nc] = new_color
                queue.append((nr, nc))

    return grid


# ==================================================
# TESTS ORIGINALES — Exactamente 3 assert
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
# CAMBIOS REALIZADOS EN LA MIGRACIÓN A PYTHON 3.14
# ==================================================
#
# 1. Anotaciones de tipos:
#    List[List[int]] -> list[list[int]].
#    Se elimina la dependencia de typing.List.
#    Es la sintaxis moderna recomendada.
#
# 2. Tipado de la cola:
#    Se añade deque[tuple[int, int]] para indicar
#    explícitamente el tipo de las coordenadas.
#
# 3. Evaluación diferida de anotaciones:
#    Python 3.14 implementa PEP 649 y PEP 749.
#    Las anotaciones no necesitan evaluarse
#    inmediatamente al definir la función.
#
# 4. Legibilidad:
#    Se reorganizan la firma y las condiciones
#    en varias líneas sin modificar su significado.
#
# 5. Algoritmo:
#    Se mantiene BFS con deque, append() y popleft().
#    No se introduce recursión ni se cambia a DFS.
#
# 6. Comportamiento:
#    Se conserva la modificación de la matriz original,
#    el retorno del mismo objeto y la conectividad
#    de cuatro direcciones, sin diagonales.
#
# 7. Pruebas:
#    Se conservan exactamente los tres assert
#    originales, con los mismos datos y resultados.
#
# 8. Rendimiento:
#    Se mantiene la complejidad O(m * n).
#    No se presupone una mejora de velocidad concreta
#    sin realizar mediciones en Python 3.14.