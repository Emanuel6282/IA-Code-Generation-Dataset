# P07: 2D Flood Fill — Python 3.9
# Paradigma: ESTRICTAMENTE ITERATIVO
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. list[tuple[int, int]]:
#    Anotación de tipos genéricos incorporada en Python 3.9
#    mediante PEP 585, sin importar List ni Tuple de typing.
#
# 2. Variables mutables:
#    Permiten actualizar la matriz y gestionar la pila.
#
# 3. Bucles while y for:
#    Recorren iterativamente los píxeles conectados.
#
# 4. Pila implementada con list:
#    Permite realizar DFS sin recursión.
#
# Biblioteca estándar únicamente.
# Sin map(), filter(), reduce() ni recursión.


def flood_fill(
    grid: list[list[int]],
    row: int,
    col: int,
    new_color: int
) -> list[list[int]]:

    # Obtener el color original.
    original_color = grid[row][col]

    # Si ya tiene el nuevo color, no modificar nada.
    if original_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Pila mutable para DFS iterativo.
    stack: list[tuple[int, int]] = [(row, col)]

    # Colorear inmediatamente para evitar
    # procesar una misma posición varias veces.
    grid[row][col] = new_color

    # Conectividad de cuatro direcciones.
    directions: list[tuple[int, int]] = [
        (-1, 0),  # Arriba
        (1, 0),   # Abajo
        (0, -1),  # Izquierda
        (0, 1)    # Derecha
    ]

    # Recorrido estrictamente iterativo.
    while stack:

        current_row, current_col = stack.pop()

        for dr, dc in directions:

            next_row = current_row + dr
            next_col = current_col + dc

            # Verificar límites de la matriz.
            if (
                0 <= next_row < rows
                and 0 <= next_col < cols
                and grid[next_row][next_col] == original_color
            ):
                # Modificar la matriz original.
                grid[next_row][next_col] = new_color

                # Agregar la posición a la pila.
                stack.append((next_row, next_col))

    return grid


# ==================================================
# TESTS — Exactamente 3 casos usando assert
# ==================================================

# TEST 1: Ejemplo del enunciado.
assert flood_fill(
    [[1, 1, 1], [1, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]


# TEST 2: El color original ya es el nuevo color.
assert flood_fill(
    [[0, 0, 0], [0, 1, 1]],
    1, 1, 1
) == [[0, 0, 0], [0, 1, 1]]


# TEST 3: No colorear conexiones diagonales.
assert flood_fill(
    [[1, 0, 1], [0, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[1, 0, 1], [0, 2, 0], [1, 0, 1]]


print("All tests passed")