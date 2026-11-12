import functools

# CARACTERISTICAS DE PYTHON 3.9 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Tipado Genérico Estándar (PEP 585): Uso nativo de `list[list[int]]`, `tuple[int, int]` y `frozenset[tuple[int, int]]` en las anotaciones.
# 2. Inmutabilidad Estricta (`frozenset`): En lugar de mutar un `set` clásico o una lista de visitados, el estado avanza generando nuevos conjuntos inmutables en cada paso de la evaluación.
# 3. Plegado de Estados (`functools.reduce`): Se sustituye el bucle `while` (DFS iterativo) por una función pura que pliega (fold) el estado inmutable a través de las 4 direcciones de búsqueda.
# 4. Proyección Pura (`map`): La construcción de la matriz final se realiza sin mutar el arreglo original (`grid[r][c] = new_color`), generando listas completamente nuevas mediante mapeo.

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    if not grid or not grid[0]:
        return grid
        
    original_color = grid[row][col]
    
    # Clausura de guarda inmutable
    if original_color == new_color:
        return grid
        
    rows = len(grid)
    cols = len(grid[0])
    
    # Función pura para calcular todos los píxeles conectados sin mutar estado externo.
    # El estado (visited) fluye como acumulador en el reduce.
    def get_connected(visited: frozenset[tuple[int, int]], current: tuple[int, int]) -> frozenset[tuple[int, int]]:
        r, c = current
        
        # Condiciones de parada (límites o color diferente o ya visitado)
        if current in visited or not (0 <= r < rows and 0 <= c < cols) or grid[r][c] != original_color:
            return visited
            
        # Generamos un nuevo estado inmutable añadiendo el nodo actual
        new_visited = visited | frozenset([current])
        
        # Direcciones adyacentes a explorar
        directions = ((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
        
        # Magia funcional: reduce actúa como el motor de recorrido.
        # Aplica get_connected sobre las direcciones, arrastrando el acumulador `new_visited`.
        return functools.reduce(get_connected, directions, new_visited)

    # 1. Recolectar funcionalmente las coordenadas que deben cambiar
    pixels_to_change = get_connected(frozenset(), (row, col))
    
    # 2. Generar la nueva matriz puramente mediante proyecciones (map) y enumeraciones,
    # sin reasignación de variables ni bucles for.
    return list(
        map(
            lambda r_row: list(
                map(
                    lambda c_val: new_color if (r_row[0], c_val[0]) in pixels_to_change else c_val[1],
                    enumerate(r_row[1])
                )
            ),
            enumerate(grid)
        )
    )

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

print("Todos los tests funcionales en Python 3.9 pasaron correctamente.")

# /*
# NOTA ANALÍTICA PARA TU TRABAJO FINAL DE GRADO (FP-UNA):
# 
# Al cargar este código en tu aplicación web de prompts, obtendrás una comparativa teórica fundamental frente a Java y Scala. 
# 
# 1. El triunfo arquitectónico de `reduce`:
# Representar un recorrido de grafos (DFS/BFS) de forma funcional y puramente inmutable es notoriamente difícil en lenguajes sin estructuras de datos persistentes optimizadas. Aquí, utilizamos `functools.reduce` para "arrastrar" un `frozenset` a través de los nodos del árbol de decisión. Es matemáticamente hermoso y simula un `foldLeft` de Scala.
# 
# 2. La fricción del paradigma en Python:
# Sin embargo, para tu tesis debes destacar que, a diferencia de Scala (donde usamos `@tailrec` en algoritmos anteriores para que el compilador aplane la recursión en un bucle optimizado de la JVM), Python carece de Tail Call Optimization (TCO). Esto significa que en una matriz muy grande (por ejemplo 1000x1000), este código fallaría con un `RecursionError`.
# Además, recrear listas enteras con un `map` anidado y generar `frozensets` constantes produce una enorme sobrecarga en el Garbage Collector comparado con la mutación iterativa (in-place) que hicimos en la respuesta anterior. 
#
# Conclusión para el reporte: Forzar el paradigma funcional estricto en Python es posible y produce un código referencialmente transparente y libre de efectos secundarios, pero el ecosistema interno del lenguaje está fundamentalmente optimizado para el modelo iterativo/imperativo, a diferencia de Scala que domina este estilo por diseño nativo.
# */