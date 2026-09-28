# Python - Algoritmo Flood Fill (Relleno por inundación)
# Paradigma: Iterativo / Recursivo con DFS controlado

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    """
    Rellena los píxeles conectados en 4 direcciones que comparten el color original
    con un nuevo color, utilizando el algoritmo Flood Fill.
    """
    if not grid or not grid[0]:
        return grid
    
    orig_color = grid[row][col]
    
    # Si el color inicial ya es el nuevo color, retorna la matriz sin cambios
    if orig_color == new_color:
        return grid
    
    rows, cols = len(grid), len(grid[0])
    
    def dfs(r: int, c: int):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] != orig_color:
            return
        grid[r][c] = new_color
        dfs(r + 1, c) # Abajo
        dfs(r - 1, c) # Arriba
        dfs(r, c + 1) # Derecha
        dfs(r, c - 1) # Izquierda
        
    dfs(row, col)
    return grid

# ==========================================
# CASOS DE PRUEBA (ASSERTS)
# ==========================================
if __name__ == "__main__":
    # CASO 1: Ejemplo del enunciado
    grid1 = [[1, 1, 1], [1, 1, 0], [1, 0, 1]]
    expected1 = [[2, 2, 2], [2, 2, 0], [2, 0, 1]]
    assert flood_fill(grid1, 1, 1, 2) == expected1, "Falló el Caso 1"

    # CASO 2: El color en (row, col) ya es el nuevo color
    grid2 = [[0, 0, 0], [0, 1, 1]]
    expected2 = [[0, 0, 0], [0, 1, 1]]
    assert flood_fill(grid2, 1, 1, 1) == expected2, "Falló el Caso 2"

    # CASO 3: Matriz de un solo elemento
    grid3 = [[5]]
    expected3 = [[3]]
    assert flood_fill(grid3, 0, 0, 3) == expected3, "Falló el Caso 3"

    print("¡Todos los casos de prueba de Flood Fill pasaron correctamente!")