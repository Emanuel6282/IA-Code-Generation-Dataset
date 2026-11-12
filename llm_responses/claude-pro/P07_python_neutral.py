def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    """Rellena con `new_color` la región conectada (4 direcciones) que contiene (row, col).

    Todos los píxeles alcanzables desde (row, col) moviéndose arriba, abajo, izquierda o
    derecha por celdas del mismo color original pasan a `new_color`. La matriz se
    modifica en el lugar y se retorna la misma lista.

    Se usa una pila explícita en lugar de recursión: una región grande (por ejemplo,
    una matriz de 1000 x 1000 de un solo color) superaría el límite de recursión de
    Python (1000 llamadas por defecto). Cada celda se pinta al apilarla, así que no se
    visita dos veces. O(filas * columnas) en tiempo.

    Raises:
        IndexError: si (row, col) está fuera de la matriz. Se comprueba de forma
            explícita porque en Python un índice negativo como -1 no falla: apuntaría
            a la última fila o columna.
    """
    if not (0 <= row < len(grid) and 0 <= col < len(grid[row])):
        raise IndexError(f"posición ({row}, {col}) fuera de la matriz")

    original = grid[row][col]
    # Si ya tiene el nuevo color no hay nada que hacer. Además evita un bucle infinito:
    # las celdas pintadas seguirían teniendo el color "original".
    if original == new_color:
        return grid

    grid[row][col] = new_color
    stack = [(row, col)]
    while stack:
        r, c = stack.pop()
        for nr, nc in ((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1)):
            # len(grid[nr]) por fila: también funciona si las filas tienen distinto largo.
            if 0 <= nr < len(grid) and 0 <= nc < len(grid[nr]) and grid[nr][nc] == original:
                grid[nr][nc] = new_color
                stack.append((nr, nc))
    return grid


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
    assert flood_fill([[1, 1, 1], [1, 1, 0], [1, 0, 1]], 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]

    # Caso 2: el color ya es el nuevo -> la matriz no cambia
    grid = [[0, 0, 0], [0, 1, 1]]
    assert flood_fill(grid, 1, 1, 1) == [[0, 0, 0], [0, 1, 1]]

    # Caso 3: no se rellena en diagonal; se modifica en el lugar y se retorna la misma lista
    grid = [[1, 0], [0, 1]]
    result = flood_fill(grid, 0, 0, 3)
    assert result == [[3, 0], [0, 1]]
    assert result is grid

    print("Todas las pruebas pasaron.")