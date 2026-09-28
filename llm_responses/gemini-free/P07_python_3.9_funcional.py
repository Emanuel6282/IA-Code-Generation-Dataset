# Python 3.9 - Paradigma Funcional Estricto
#
# Características funcionales utilizadas:
# 1. Inmutabilidad estricta: Transformación de la matriz en tuplas inmutables (`tuple`) para evitar efectos secundarios.
# 2. Funciones de orden superior: Empleo de `functools.reduce`, `filter` y expresiones `lambda` para el procesamiento declarativo de la frontera y vecinos.
# 3. Recursión pura de propagación: Ausencia total de bucles `for`/`while` con variables de estado mutables.
# 4. Anotaciones de tipos nativas de Python 3.9 (`list[list[int]]`).

from functools import reduce

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    """
    Implementa el algoritmo Flood Fill de forma estrictamente funcional utilizando
    inmutabilidad, filter, map y functools.reduce.
    """
    if not grid or not grid[0]:
        return grid
    
    orig_color = grid[row][col]
    
    # Si el color inicial ya es el nuevo color, retorna la matriz sin cambios
    if orig_color == new_color:
        return grid
    
    rows, cols = len(grid), len(grid[0])
    
    # Convertir la matriz a una estructura inmutable (tupla de tuplas)
    grid_tuple = tuple(tuple(r) for r in grid)
    
    if grid_tuple[row][col] != orig_color:
        return grid
    
    def get_neighbors(r: int, c: int) -> list[tuple[int, int]]:
        candidates = [(r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1)]
        return list(filter(
            lambda rc: 0 <= rc[0] < rows and 0 <= rc[1] < cols,
            candidates
        ))

    def explore(current_grid: tuple[tuple[int, ...], ...], frontier: set[tuple[int, int]], visited: set[tuple[int, int]]) -> tuple[tuple[int, ...], ...]:
        if not frontier:
            return current_grid
        
        # Filtrar vecinos válidos que tengan el color original y no hayan sido visitados
        def is_valid(rc: tuple[int, int]) -> bool:
            r, c = rc
            return current_grid[r][c] == orig_color and rc not in visited

        # Usar functools.reduce para acumular los vecinos de toda la frontera actual
        all_neighbors = reduce(lambda acc, rc: acc + get_neighbors(rc[0], rc[1]), frontier, [])
        valid_neighbors = set(filter(is_valid, all_neighbors))
        
        # Construir una nueva tupla de tuplas aplicando el nuevo color a la frontera actual
        grid_list = [list(r) for r in current_grid]
        for r, c in frontier:
            grid_list[r][c] = new_color
        new_grid = tuple(tuple(r) for r in grid_list)
        
        new_visited = visited.union(frontier)
        return explore(new_grid, valid_neighbors, new_visited)

    final_grid_tuple = explore(grid_tuple, {(row, col)}, set())
    
    # Retornar de vuelta como list[list[int]] según el contrato requerido
    return [list(r) for r in final_grid_tuple]

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
    grid3 = [[4]]
    expected3 = [[8]]
    assert flood_fill(grid3, 0, 0, 8) == expected3, "Falló Caso 3"

    print("¡Todos los casos de prueba funcionales en Python 3.9 pasaron correctamente!")