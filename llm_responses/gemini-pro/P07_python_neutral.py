def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    if not grid or not grid[0]:
        return grid
        
    original_color = grid[row][col]
    
    # Si el color en la posición inicial ya es el nuevo color, retornamos sin cambios
    if original_color == new_color:
        return grid
        
    rows = len(grid)
    cols = len(grid[0])
    
    def dfs(r: int, c: int):
        # Condición de parada: fuera de los límites de la matriz
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return
        # Condición de parada: el píxel actual no tiene el color original
        if grid[r][c] != original_color:
            return
            
        # Actualizamos el color del píxel actual
        grid[r][c] = new_color
        
        # Llamadas recursivas para las 4 direcciones conectadas
        dfs(r - 1, c) # Arriba
        dfs(r + 1, c) # Abajo
        dfs(r, c - 1) # Izquierda
        dfs(r, c + 1) # Derecha
        
    dfs(row, col)
    return grid

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: reemplazo de unos conectados a partir de (1,1)
grid1 = [
    [1, 1, 1],
    [1, 1, 0],
    [1, 0, 1]
]
expected1 = [
    [2, 2, 2],
    [2, 2, 0],
    [2, 0, 1]
]
assert flood_fill(grid1, 1, 1, 2) == expected1

# 2. Caso sin cambios: el color inicial ya es igual al nuevo color
grid2 = [
    [0, 0, 0],
    [0, 0, 0]
]
expected2 = [
    [0, 0, 0],
    [0, 0, 0]
]
assert flood_fill(grid2, 0, 0, 0) == expected2

# 3. Caso de aislamiento: solo cambia el centro, los otros 1s no están conectados en 4 direcciones
grid3 = [
    [1, 0, 1],
    [0, 1, 0],
    [1, 0, 1]
]
expected3 = [
    [1, 0, 1],
    [0, 3, 0],
    [1, 0, 1]
]
assert flood_fill(grid3, 1, 1, 3) == expected3

print("Todos los casos de prueba pasaron correctamente.")