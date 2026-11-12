# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Alias de Tipos Nativos (PEP 695): Consolidado en las versiones recientes (introducido en 3.12+), utilizamos la nueva declaración `type` para definir alias limpios (ej. `type Grid = list[list[int]]`). Esto elimina la verbosidad en las firmas de funciones sin depender del módulo `typing`.
# 2. Motor JIT (Copy-and-Patch Optimizer): Python 3.14 madura el compilador Just-In-Time (JIT) (Tier 2 optimizer introducido en 3.13). Aunque el código iterativo se ve casi idéntico al de 3.11, el bucle `while` y las operaciones de mutación local ahora se compilan dinámicamente a código máquina, logrando un rendimiento que acorta la brecha histórica con la JVM de Java.
# 3. Paradigma Imperativo Puro: Uso estricto de mutación in-place (`grid[r][c] = new_color`) y una pila (stack) explícita en el Heap para emular la profundidad del recorrido DFS sin tocar el Call Stack ni incurrir en la sobrecarga de la recursión.

type Grid = list[list[int]]

def flood_fill(grid: Grid, row: int, col: int, new_color: int) -> Grid:
    if not grid or not grid[0]:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Manejo defensivo con notas de excepción heredado y optimizado en las versiones 3.11+
    if not (0 <= row < rows and 0 <= col < cols):
        err = ValueError("Coordenadas de inicio fuera de los límites de la matriz.")
        err.add_note(f"Se recibió row={row}, col={col}. La matriz tiene un tamaño de {rows}x{cols}.")
        raise err

    original_color = grid[row][col]

    # Guard clause: Si el color inicial ya es el objetivo, retornamos inmediatamente (Evita ciclos infinitos)
    if original_color == new_color:
        return grid

    # Inicialización de la pila explícita
    stack = [(row, col)]

    # Bucle iterativo altamente optimizable por el nuevo JIT de CPython
    while stack:
        # Desempacado eficiente de tuplas
        r, c = stack.pop()

        if grid[r][c] == original_color:
            # Mutación de estado (In-place)
            grid[r][c] = new_color

            # Evaluación e inserción iterativa (4 Direcciones)
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

print("Todos los tests iterativos en Python 3.14 pasaron correctamente.")

# /*
# NOTA ANALÍTICA PARA EL LIBRO DE TU TRABAJO FINAL DE GRADO (FP-UNA):
# 
# Al registrar este código en tu aplicación web para el seguimiento de prompts, cerrarás tu matriz de evaluación iterativa para Python (3.9 -> 3.11 -> 3.14) con una observación arquitectónica de alto impacto.
# 
# 1. El triunfo de la estabilidad iterativa frente al JIT:
# En tus experimentos con Java (17 a 21), notaste que el código imperativo puro se "congeló" sintácticamente, delegando las mejoras al motor JIT de la JVM. Ahora has documentado exactamente el mismo fenómeno en Python. Al usar Python 3.14, el código del bucle `while` apenas cambia sintácticamente respecto a 3.11 (salvo la adopción nativa de `type Grid`), pero bajo el capó, la activación del nuevo compilador JIT (Copy-and-Patch) convierte estas iteraciones puras y mutaciones de listas en instrucciones a nivel de máquina ultrarrápidas.
# 
# 2. Convergencia de Tipado (PEP 695):
# Un punto destacable para tu tesis es cómo Python 3.14 logra converger visualmente con lenguajes estáticamente tipados como Scala. Con la introducción de la palabra clave `type` para alias (`type Grid = list[list[int]]`), Python abandona definitivamente las sintaxis pesadas y verbosas de tipado de versiones anteriores. 
# 
# Conclusión para tu reporte: Cuando se fuerza el paradigma iterativo en la evolución de un lenguaje (sea fuertemente tipado como Java/Scala, o dinámico como Python), la sintaxis superficial de los bucles y mutaciones tiende a petrificarse. La verdadera innovación de las versiones recientes ocurre exclusivamente en el analizador de tipos estático y en la máquina virtual (JIT), optimizando el procesamiento de la mutación de estado a nivel de hardware.
# */