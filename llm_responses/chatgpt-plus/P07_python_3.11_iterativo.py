# P07: 2D Flood Fill — Python 3.11
# Paradigma: ESTRICTAMENTE ITERATIVO
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[list[int]] y list[tuple[int, int]].
#    Introducidas en Python 3.9 y compatibles con 3.11.
#
# 2. Bucles while y for:
#    Permiten recorrer la matriz sin recursión.
#
# 3. Variables mutables:
#    La matriz y la pila se actualizan directamente.
#
# 4. Desempaquetado de tuplas:
#    Facilita el manejo de filas, columnas y direcciones.
#
# 5. Python 3.11 — PEP 659:
#    El intérprete incorpora especialización adaptativa,
#    que puede optimizar operaciones frecuentes.
#    No requiere cambios específicos en el algoritmo.
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

    # Si el color ya coincide, devolver la matriz.
    if original_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Pila mutable para DFS iterativo.
    stack: list[tuple[int, int]] = [(row, col)]

    # Colorear el píxel inicial inmediatamente.
    grid[row][col] = new_color

    # Conectividad en cuatro direcciones.
    directions: tuple[tuple[int, int], ...] = (
        (-1, 0),  # Arriba
        (1, 0),   # Abajo
        (0, -1),  # Izquierda
        (0, 1)    # Derecha
    )

    # Recorrido iterativo.
    while stack:

        current_row, current_col = stack.pop()

        # Revisar los cuatro vecinos.
        for dr, dc in directions:

            next_row = current_row + dr
            next_col = current_col + dc

            # Comprobar límites y color original.
            if (
                0 <= next_row < rows
                and 0 <= next_col < cols
                and grid[next_row][next_col] == original_color
            ):
                # Actualizar la matriz original.
                grid[next_row][next_col] = new_color

                # Agregar el vecino a la pila.
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


# TEST 3: No se colorean conexiones diagonales.
assert flood_fill(
    [[1, 0, 1], [0, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[1, 0, 1], [0, 2, 0], [1, 0, 1]]


print("All tests passed")