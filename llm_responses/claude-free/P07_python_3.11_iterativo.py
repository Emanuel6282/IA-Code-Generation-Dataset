def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    old_color = grid[row][col]

    if old_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    stack = [(row, col)]
    grid[row][col] = new_color

    while stack:
        r, c = stack.pop()

        for dr, dc in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            nr = r + dr
            nc = c + dc

            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == old_color:
                grid[nr][nc] = new_color
                stack.append((nr, nc))

    return grid


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    grid1 = [[1, 1, 1], [1, 1, 0], [1, 0, 1]]
    assert flood_fill(grid1, 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]], "Fallo caso 1"

    # Caso 2: el color ya es el nuevo color -> sin cambios
    grid2 = [[0, 0, 0], [0, 1, 1]]
    assert flood_fill(grid2, 1, 1, 1) == [[0, 0, 0], [0, 1, 1]], "Fallo caso 2"

    # Caso 3: matriz de un solo color, se rellena por completo
    grid3 = [[3, 3], [3, 3]]
    assert flood_fill(grid3, 0, 0, 7) == [[7, 7], [7, 7]], "Fallo caso 3"

    print("Todos los tests pasaron correctamente.")