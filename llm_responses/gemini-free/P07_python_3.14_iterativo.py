# Python 3.14 - Paradigma Iterativo Estricto
#
# Características utilizadas:
# 1. Anotaciones de tipo nativas avanzadas de Python (ej. `list[list[int]]`).
# 2. Paradigma iterativo estricto: Empleo exclusivo de bucles `while` y `for` junto con `collections.deque` para un rendimiento óptimo de BFS en O(N).
# 3. Exclusión total de funciones de orden superior (map, filter, reduce) y recursión.

from collections import deque

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    """
    Implementa el algoritmo Flood Fill de forma iterativa (BFS) sobre una matriz 2D
    aprovechando las optimizaciones de rendimiento y tipado nativo de Python 3.14.
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
    directions = ((-1, 0), (1, 0), (0, -1), (0, 1))
    
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
    grid3 = [[42]]
    expected3 = [[99]]
    assert flood_fill(grid3, 0, 0, 99) == expected3, "Falló Caso 3"

    print("¡Todos los casos de prueba iterativos en Python 3.14 pasaron correctamente!")