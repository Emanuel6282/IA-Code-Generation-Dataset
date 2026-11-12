# P07: 2D Flood Fill — Python 3.14
# Paradigma: ESTRICTAMENTE ITERATIVO
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Bucles while y for:
#    Permiten recorrer la matriz sin recursión.
#
# 2. Variables mutables:
#    La matriz se modifica directamente y la pila
#    almacena las posiciones pendientes.
#
# 3. Anotaciones modernas:
#    list[list[int]] y list[tuple[int, int]].
#    Introducidas en Python 3.9 y compatibles con 3.14.
#
# 4. Python 3.14 — PEP 649 y PEP 749:
#    Evaluación diferida de anotaciones.
#    No altera la lógica del algoritmo, pero forma
#    parte del sistema de tipos de Python 3.14.
#
# NOTA:
# No se requieren características exclusivas de
# Python 3.14 para mejorar este algoritmo.
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

    # Pila mutable para realizar DFS iterativo.
    stack: list[tuple[int, int]] = [(row, col)]

    # Cambiar inmediatamente el color inicial.
    grid[row][col] = new_color

    # Conectividad de cuatro direcciones.
    directions: tuple[tuple[int, int], ...] = (
        (-1, 0),  # Arriba
        (1, 0),   # Abajo
        (0, -1),  # Izquierda
        (0, 1)    # Derecha
    )

    # Recorrido estrictamente iterativo.
    while stack:

        current_row, current_col = stack.pop()

        # Revisar los cuatro vecinos.
        for dr, dc in directions:

            next_row = current_row + dr
            next_col = current_col + dc

            # Verificar límites y color original.
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