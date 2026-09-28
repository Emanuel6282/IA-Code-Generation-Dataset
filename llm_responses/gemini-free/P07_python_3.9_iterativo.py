# Python 3.9 - Paradigma Iterativo Estricto
#
# Características de Python 3.9 utilizadas:
# 1. Anotaciones de tipos nativas sin importar colecciones de `typing` (ej. `list[list[int]]`).
# 2. Uso exclusivo de bucles `while` y `for` con colas eficientes (`collections.deque`) para un recorrido BFS iterativo.
# 3. Ausencia total de recursión y funciones de orden superior como map/filter/reduce.

from collections import deque

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    """
    Implementa el algoritmo Flood Fill de forma iterativa (BFS) sobre una matriz 2D.
    """
    if not grid or not grid[0]:
        return grid
    
    orig_color = grid[row][col]
    
    # Si el color inicial ya es el nuevo color, retorna la matriz sin cambios
    if orig_color == new_color:
        return grid
    
    rows = len(grid)
    cols = len(grid[0])
    
    # Cola para el recorrido iterativo por anchura (BFS)
    queue = deque([(row, col)])
    grid[row][col] = new_color
    
    # Direcciones de 4 vecinos: arriba, abajo, izquierda, derecha
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    
    while queue:
        r, c = queue.popleft()
        
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            
            # Verificar límites de la matriz y si el píxel adyacente tiene el color original
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == orig_color:
                grid[nr][nc] = new_color
                queue.append((nr, nc))
                
    return grid

# ==========================================
# CASOS DE PRUEBA (ASSERTS)
# ==========================================
if __name__ == "__main__":
    # CASO 1: Ejemplo clásico del enunciado
    grid1 = [[1, 1, 1], [1, 1, 0], [1, 0, 1]]
    expected1 = [[2, 2, 2], [2, 2, 0], [2, 0, 1]]
    assert flood_fill(grid1, 1, 1, 2) == expected1, "Falló Caso 1"

    # CASO 2: El color en (row, col) ya coincide con el nuevo color
    grid2 = [[0, 0, 0], [0, 1, 1]]
    expected2 = [[0, 0, 0], [0, 1, 1]]
    assert flood_fill(grid2, 1, 1, 1) == expected2, "Falló Caso 2"

    # CASO 3: Matriz de un solo elemento
    grid3 = [[7]]
    expected3 = [[9]]
    assert flood_fill(grid3, 0, 0, 9) == expected3, "Falló Caso 3"

    print("¡Todos los casos de prueba iterativos en Python 3.9 pasaron correctamente!")