# CARACTERISTICAS DE PYTHON 3.9 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Tipado Genérico en Colecciones Estándar (PEP 585): Introducido en Python 3.9, permite usar `list[list[int]]` directamente en la firma de la función sin necesidad de importar `List` del módulo `typing`.
# 2. Paradigma Imperativo/Iterativo Puro: Se reemplaza la recursión natural de Depth-First Search (DFS) por una pila explícita (stack) implementada con una lista mutable y un bucle `while`. Esto emula el comportamiento de la pila de llamadas (call stack) en el Heap, evadiendo el límite de recursión de Python (`RecursionError`).
# 3. Mutación In-Place: El estado del algoritmo avanza modificando directamente los valores dentro de la matriz (listas de listas mutables) e insertando/extrayendo elementos de la pila, evitando la sobrecarga de copias o variables funcionales inmutables.

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    if not grid or not grid[0]:
        return grid

    original_color = grid[row][col]

    # Guard clause: Si el color inicial ya es el objetivo, retornamos inmediatamente para evitar bucles infinitos
    if original_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    # Uso de una pila explícita (Stack) para control iterativo del algoritmo DFS
    stack = [(row, col)]

    # Bucle iterativo de bajo nivel
    while stack:
        # Extracción destructiva del estado actual
        r, c = stack.pop()

        if grid[r][c] == original_color:
            # Mutación in-place del color
            grid[r][c] = new_color

            # Evaluación e inserción iterativa de los vecinos en las 4 direcciones
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

print("Todos los tests iterativos en Python 3.9 pasaron correctamente.")

# /*
# EXPLICACION DE CAMBIOS E IDIOMATISMOS PARA TU TESIS (FP-UNA):
# 
# 1. **Gestión de Memoria y Recursión (DFS iterativo)**:
#    En tu análisis comparativo para la página web de prompts, este es un ejemplo crucial de cómo transformar un algoritmo inherentemente recursivo (como lo es navegar un grafo o matriz) al paradigma iterativo estricto. Al usar una pila `stack = []` y un `while`, pasamos de usar la memoria de pila de llamadas (Call Stack, muy limitada por defecto en Python, ~1000 llamadas) a usar la memoria del Heap. Para matrices grandes de imágenes reales, la versión recursiva fallaría, pero esta iterativa sobrevive.
# 
# 2. **Tipado Estático Ligero (PEP 585)**:
#    Al observar la firma `list[list[int]]`, tu plataforma documentará cómo Python 3.9 dio un salto hacia la limpieza estática, abandonando los molestos imports de colecciones del módulo `typing`. Esto acerca visualmente a Python a los lenguajes compilados que analizaste anteriormente (Java/Scala).
# 
# 3. **Mutabilidad como Ventaja de Rendimiento**:
#    El paradigma iterativo en Python abraza la modificación in-place de la lista original. Aunque los puristas funcionales evitan los "side-effects" (efectos secundarios), en tareas de procesamiento de imágenes o matrices densas, mutar la matriz existente es órdenes de magnitud más rápido y barato computacionalmente que generar copias inmutables del `grid` completo en cada paso de propagación.
# */