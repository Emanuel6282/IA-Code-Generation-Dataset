def flood_fill(grid, row, col, new_color):
    # Obtener el color original.
    original_color = grid[row][col]

    # Si el color ya es el nuevo, no modificar la matriz.
    if original_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Pila para recorrer las posiciones conectadas.
    stack = [(row, col)]

    # Cambiar el color inicial.
    grid[row][col] = new_color

    # Direcciones: arriba, abajo, izquierda, derecha.
    directions = [
        (-1, 0),
        (1, 0),
        (0, -1),
        (0, 1)
    ]

    while stack:
        current_row, current_col = stack.pop()

        for dr, dc in directions:
            next_row = current_row + dr
            next_col = current_col + dc

            # Verificar que la posición esté dentro de la matriz.
            if (
                0 <= next_row < rows
                and 0 <= next_col < cols
                and grid[next_row][next_col] == original_color
            ):
                # Cambiar el color y agregar la posición a la pila.
                grid[next_row][next_col] = new_color
                stack.append((next_row, next_col))

    return grid


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


# TEST 3: No se deben colorear elementos conectados
# únicamente en diagonal.
assert flood_fill(
    [[1, 0, 1], [0, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[1, 0, 1], [0, 2, 0], [1, 0, 1]]


print("All tests passed")