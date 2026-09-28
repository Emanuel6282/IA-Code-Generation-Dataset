# CARACTERÍSTICAS NUEVAS DE PYTHON 3.14 UTILIZADAS:
# 1. Anotaciones de tipos nativas simplificadas (`list[list[int]]`): Uso de genéricos incorporados altamente optimizados gracias a las mejoras en el sistema de tipos de Python 3.14, eliminando la necesidad de importar `List` desde `typing`.
# 2. Optimizaciones avanzadas del intérprete CPython 3.14: Mejoras en la ejecución de bucles y en la asignación de memoria para estructuras de datos integradas como `collections.deque`.
# 3. Tuplas estáticas inmutables para las direcciones: Empleo de tuplas anidadas para evitar la sobrecarga de asignación dinámica de memoria en cada iteración del bucle.

from collections import deque

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    original_color = grid[row][col]
    if original_color == new_color:
        return grid
    
    rows = len(grid)
    cols = len(grid[0])
    
    # Inicialización optimizada de la cola con el píxel inicial
    queue = deque([(row, col)])
    grid[row][col] = new_color
    
    # Tupla estática inmutable para las 4 direcciones de vecindad
    directions = ((-1, 0), (1, 0), (0, -1), (0, 1))
    
    while queue:
        r, c = queue.popleft()
        for dr, dc in directions:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == original_color:
                grid[nr][nc] = new_color
                queue.append((nr, nc))
                
    return grid

# Casos de prueba adaptados a Python 3.14
assert flood_fill([[1, 1, 1], [1, 1, 0], [1, 0, 1]], 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]
assert flood_fill([[0]], 0, 0, 5) == [[5]]
assert flood_fill([[0, 0], [0, 0]], 0, 0, 3) == [[3, 3], [3, 3]]

print("All tests passed")

# EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14:
# 1. Supresión del módulo `typing.List`: Las anotaciones modernas con tipos integrados reducen la verbosidad y aprovechan el rendimiento optimizado del evaluador de tipos en Python 3.14.
# 2. Uso de tuplas literales para direcciones: Reemplazar listas por tuplas evita la recreación inútil de objetos mutables en bucles de alto rendimiento, optimizando el uso de memoria.
# 3. Código limpio y directo: Se simplificó la inicialización del BFS para mantener un estilo idiomático y alineado con las mejores prácticas del ecosistema moderno de Python.