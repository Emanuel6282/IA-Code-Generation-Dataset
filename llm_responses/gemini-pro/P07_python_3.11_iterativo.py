# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. PEP 678 (Exception Notes): Uso explícito del método `add_note()` para enriquecer el manejo de errores en tiempo de ejecución. Permite agregar metadatos valiosos a una excepción sin alterar su mensaje original, mejorando la depuración iterativa.
# 2. Faster CPython (PEP 659 - Adaptive Specializing Interpreter): Aunque el código iterativo se lee casi idéntico al de 3.9, Python 3.11 introdujo especialización de instrucciones en tiempo de ejecución. Los bucles `while`, los saltos condicionales `if` y el desempacado de tuplas (`r, c = stack.pop()`) que forman el núcleo de este DFS iterativo, se ejecutan un 10-60% más rápido a nivel de la máquina virtual de CPython.
# 3. Paradigma Imperativo/Iterativo Puro: Se utiliza una pila (stack) explícita mediante una lista estándar para mantener la memoria en el Heap, evadiendo los límites estrictos del Call Stack y descartando la recursión. La matriz se muta destructivamente "in-place".

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    if not grid or not grid[0]:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Manejo defensivo moderno usando las notas de excepción de Python 3.11
    if not (0 <= row < rows and 0 <= col < cols):
        err = ValueError("Coordenadas de inicio fuera de los límites de la matriz.")
        err.add_note(f"Se recibió row={row}, col={col}. La matriz tiene tamaño {rows}x{cols}.")
        raise err

    original_color = grid[row][col]

    # Guard clause: Si el color inicial ya es el objetivo, retornamos inmediatamente
    if original_color == new_color:
        return grid

    # Pila explícita para control iterativo del algoritmo DFS (Depth-First Search)
    stack = [(row, col)]

    # Bucle iterativo de bajo nivel
    while stack:
        # Extracción y desempacado del estado actual
        r, c = stack.pop()

        if grid[r][c] == original_color:
            # Mutación in-place del color
            grid[r][c] = new_color

            # Evaluación e inserción iterativa de los vecinos válidos en las 4 direcciones
            if r > 0:
                stack.append((r - 1, c))  # Arriba
            if r < rows - 1:
                stack.append((r + 1, c))  # Abajo
            if c > 0:
                stack.append((r, c - 1))  # Izquierda
            if c < cols - 1:
                stack.append((r, c + 1))  # Derecha

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

print("Todos los tests iterativos en Python 3.11 pasaron correctamente.")

# /*
# NOTA ANALÍTICA PARA TU TRABAJO FINAL DE GRADO (FP-UNA):
# 
# Al cargar este snippet en la página web que diseñaste para el copy/paste de prompts, 
# se hará evidente una pauta muy similar a la que observamos con las ramas iterativas de Java (17 a 21) 
# y Scala (3.3 a 3.6): la estabilidad estática del paradigma imperativo.
# 
# Sintácticamente, el algoritmo de búsqueda de grafos iterativo (usando un while y una pila manual) 
# apenas ha mutado desde Python 3.9, a excepción de las mejoras ergonómicas como el `add_note()`. 
# Sin embargo, para tu análisis en la tesis, la magia de Python 3.11 reside en lo que NO se ve: 
# el "Faster CPython Project" (PEP 659). 
# 
# El intérprete ahora especializa adaptativamente operaciones como el `pop()`, la carga global de variables 
# y el desempacado de tuplas. Esto significa que este mismo bloque de código corre significativamente más rápido 
# en 3.11 que en 3.9 sin alterar una sola línea de lógica. Este es el argumento principal a defender en tu 
# sección sobre Python: mientras que los lenguajes compilados de la JVM imponen cambios sintácticos para modernizarse, 
# los lenguajes dinámicos suelen delegar las mejoras de rendimiento al intérprete subyacente, manteniendo 
# intacta la experiencia iterativa del desarrollador.
# */