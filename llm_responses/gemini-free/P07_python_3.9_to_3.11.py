# CARACTERÍSTICAS NUEVAS DE PYTHON 3.11 UTILIZADAS:
# 1. Anotaciones de tipos nativas simplificadas (`list[list[int]]`): Se elimina la importación y uso de `List` desde el módulo `typing`, aprovechando la compatibilidad nativa directa con colecciones estándar introducida en PEP 585 y plenamente optimizada en el intérprete de Python 3.11.
# 2. Mejoras de rendimiento del intérprete CPython 3.11: El intérprete modernizado incluye optimizaciones en la creación de tramas (frames) y ejecución adaptativa, lo que acelera significativamente los bucles y las operaciones con colas (`collections.deque`).
# 3. Tuplas estáticas inmutables para coordenadas: Se emplean tuplas anidadas `((...))` en lugar de listas literales para definir las direcciones de búsqueda, mejorando la velocidad de acceso y reduciendo la sobrecarga de asignación de memoria en cada iteración.

from collections import deque

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    original_color = grid[row][col]
    if original_color == new_color:
        return grid
    
    rows = len(grid)
    cols = len(grid[0])
    
    # Inicialización directa de la cola con el píxel inicial
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

# Casos de prueba adaptados a Python 3.11
assert flood_fill([[1, 1, 1], [1, 1, 0], [1, 0, 1]], 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]
assert flood_fill([[0]], 0, 0, 5) == [[5]]
assert flood_fill([[0, 0], [0, 0]], 0, 0, 3) == [[3, 3], [3, 3]]

print("All tests passed")

# EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.11:
# 1. Supresión del módulo `typing.List`: Python 3.9+ permite utilizar los tipos incorporados estándar (`list`, `dict`, `set`) directamente para las anotaciones. En Python 3.11, esta característica cuenta con mejoras de rendimiento en tiempo de ejecución al evaluar tipos genéricos.
# 2. Uso de tuplas literales para direcciones: Cambiar las listas `[(-1,0), ...]` por tuplas `((-1,0), ...)` evita la reasignación dinámica y la sobrecarga de mutabilidad en cada ciclo del bucle, alineándose con las mejores prácticas de optimización de CPython 3.11.
# 3. Estructura más limpia y directa: Se eliminó el código redundante de inicialización de la cola, pasando directamente la tupla de coordenadas al constructor de `deque`.