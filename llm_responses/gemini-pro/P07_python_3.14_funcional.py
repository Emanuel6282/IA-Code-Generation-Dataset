import functools

# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Alias de Tipos Nativos Avanzados (PEP 695): Se consolidan las declaraciones explícitas con `type`. En este caso, `type Grid` y `type Coord` limpian drásticamente la firma funcional, logrando un código más declarativo y alineado con la legibilidad de lenguajes de tipado estricto.
# 2. Plegado de Estado Puro (`functools.reduce`): El estado de la búsqueda se arrastra de forma inmutable a lo largo de las 4 direcciones espaciales.
# 3. Proyección Declarativa (Map + Enumerate): Construcción inmutable de la nueva matriz evaluando las tuplas coordenadas, eludiendo bucles imperativos o mutaciones in-place.
# 4. Inmutabilidad Absoluta (`frozenset`): Los conjuntos de píxeles visitados se regeneran en cada iteración como constantes en memoria.

type Grid = list[list[int]]
type Coord = tuple[int, int]

def flood_fill(grid: Grid, row: int, col: int, new_color: int) -> Grid:
    if not grid or not grid[0]:
        return grid
        
    rows = len(grid)
    cols = len(grid[0])
    
    # Manejo defensivo robusto
    if not (0 <= row < rows and 0 <= col < cols):
        err = ValueError("Coordenadas fuera de rango.")
        err.add_note(f"Recibido: row={row}, col={col} para una matriz de {rows}x{cols}.")
        raise err
        
    original_color = grid[row][col]
    
    # Clausura inmutable
    if original_color == new_color:
        return grid
    
    # Función pura interna: Propaga el `frozenset` a través del algoritmo DFS
    def get_connected(visited: frozenset[Coord], current: Coord) -> frozenset[Coord]:
        r, c = current
        
        # Poda referencial
        if current in visited or not (0 <= r < rows and 0 <= c < cols) or grid[r][c] != original_color:
            return visited
            
        # Transición pura: se crea una nueva constante combinada
        new_visited = visited | frozenset([current])
        
        directions = ((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
        
        # Reducción de estados sobre el grafo
        return functools.reduce(get_connected, directions, new_visited)

    # 1. Acumular el estado referencial (píxeles conectados)
    pixels_to_change = get_connected(frozenset(), (row, col))
    
    # 2. Mapeo estructural de la matriz de salida sin mutación destructiva
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

# 3. Caso de aislamiento: solo cambia el centro, los otros 1s no están conectados
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

print("Todos los tests funcionales en Python 3.14 pasaron correctamente.")

# /*
# NOTA ANALÍTICA PARA EL LIBRO DEL TRABAJO FINAL DE GRADO (FP-UNA):
# 
# Al ingresar este último bloque en tu página web de seguimiento de LLMs, habrás cerrado formalmente la experimentación del paradigma funcional cruzando lenguajes. Este fragmento consolida un hallazgo crítico para la redacción final de la tesis:
# 
# 1. El alcance de la sintaxis declarativa (PEP 695): En Scala 3.6, la sintaxis funcional se apoyaba en el ecosistema "braceless" y en tuplas desestructuradas nativamente. Con la introducción madura de los alias `type Grid` y `type Coord` en Python 3.14, el lenguaje logra imitar esa limpieza estructural, permitiendo que la firma de funciones complejas de orden superior como el `reduce` se lea casi como un teorema matemático.
# 
# 2. El techo arquitectónico frente al JIT (Copy-and-Patch): En la versión iterativa de 3.14 observamos cómo el compilador JIT optimizaba los bucles `while` llevándolos a código máquina ultrarrápido. Sin embargo, el paradigma funcional en Python presenta una fricción insalvable aquí. CPython sigue sin implementar "Tail Call Optimization" (TCO) por una decisión de diseño fundacional (mantener stack traces legibles). Por lo tanto, aunque el JIT acelera las asignaciones simples, esta implementación funcional profunda fallará estrepitosamente con un `RecursionError` en una matriz grande, a diferencia del `foldLeft` de Scala 3.6 o el Stream de Java 21 que gestionan sus acumuladores de manera optimizada bajo el capó. 
# 
# Esto sustenta una conclusión empírica sólida: mientras que lenguajes multiparadigma como Scala y Java han modelado sus máquinas virtuales para abrazar eficientemente la inmutabilidad, Python 3.14 ofrece una sintaxis moderna brillante para expresar la pureza referencial, pero penaliza su ejecución en algoritmos espaciales que demandan recursión.
# */