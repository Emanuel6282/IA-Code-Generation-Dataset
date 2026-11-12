from collections import deque

# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Tipado Genérico Nativo (PEP 585): Se elimina por completo la necesidad de importar `List` del módulo `typing`. En Python 3.11, el uso de las colecciones estándar minúsculas (`list[list[int]]`) es la norma idiomática obligatoria, limpiando la cabecera del archivo.
# 2. Notas de Excepción (PEP 678): Se agrega un manejo defensivo al inicio usando `.add_note()`. Esto permite adjuntar metadatos de depuración vitales (como los límites de la matriz y las coordenadas erróneas) al `ValueError`, sin modificar el mensaje de la excepción original.
# 3. Faster CPython (PEP 659): Aunque es una mejora invisible a nivel sintáctico, el "Adaptive Specializing Interpreter" de la versión 3.11 especializa dinámicamente las llamadas a `queue.popleft()` y las operaciones matemáticas del bucle for subyacente, ejecutando este algoritmo BFS iterativo considerablemente más rápido que en 3.9.

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    if not grid or not grid[0]:
        return grid
        
    rows = len(grid)
    cols = len(grid[0])
    
    # Manejo defensivo moderno usando notas de excepción de Python 3.11
    if not (0 <= row < rows and 0 <= col < cols):
        err = ValueError("Coordenadas de inicio fuera de los límites de la matriz.")
        err.add_note(f"Se recibió row={row}, col={col}. La matriz tiene tamaño {rows}x{cols}.")
        raise err

    original_color = grid[row][col]
    
    # Cláusula de guarda para evitar ciclos infinitos si no hay cambio de color
    if original_color == new_color:
        return grid
        
    # Inicialización de la cola para la Búsqueda en Anchura (BFS)
    queue = deque([(row, col)])
    grid[row][col] = new_color
    
    # Bucle iterativo acelerado por el intérprete adaptativo de 3.11
    while queue:
        r, c = queue.popleft()
        
        # Exploración de las 4 direcciones adyacentes
        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            nr, nc = r + dr, c + dc
            
            # Condición combinada optimizada
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == original_color:
                grid[nr][nc] = new_color
                queue.append((nr, nc))
                
    return grid

# ==========================================
# CASOS DE PRUEBA
# ==========================================
assert flood_fill([[1,1,1],[1,1,0],[1,0,1]], 1, 1, 2) == [[2,2,2],[2,2,0],[2,0,1]], "Error en caso 1"
assert flood_fill([[0]], 0, 0, 5) == [[5]], "Error en caso 2 (matriz 1x1)"
assert flood_fill([[0,0],[0,0]], 0, 0, 3) == [[3,3],[3,3]], "Error en caso 3 (reemplazo total)"

print("Todos los tests iterativos (BFS) en Python 3.11 pasaron correctamente.")

# /*
# EXPLICACION DE CAMBIOS E IDIOMATISMOS PARA TU TESIS (FP-UNA):
# 
# 1. Limpieza de Imports y Tipos: 
# El código base de 3.9 todavía arrastraba prácticas de versiones anteriores (`from typing import List`). Al refactorizar a 3.11, demostramos cómo el lenguaje ha madurado hacia una sintaxis de tipado mucho más limpia. Esta es una excelente métrica para tu página web de experimentación: a medida que los lenguajes dinámicos adoptan comprobaciones estáticas, tienden a integrar los tipos genéricos directamente en sus estructuras base para reducir el "boilerplate".
# 
# 2. Manejo de Errores con Contexto (`add_note`):
# En algoritmos espaciales como Flood Fill, los errores de índices (`IndexError`) son la principal causa de fallos. El código original de 3.9 carecía de validaciones iniciales. Agregar un bloque `if` defensivo no solo lo hace robusto, sino que el uso de `add_note()` demuestra el estándar moderno de Python para enriquecer el contexto de depuración sin destruir la cadena de la excepción original.
# 
# 3. BFS (Breadth-First Search) vs DFS:
# Nota que el código base que proporcionaste utiliza `deque` y `popleft()`. A diferencia de tus ejemplos anteriores donde utilizamos una pila (`stack`) para hacer un DFS iterativo (Búsqueda en Profundidad), este enfoque realiza un BFS iterativo (Búsqueda en Anchura). Ambos pertenecen estrictamente al paradigma iterativo, pero el BFS propaga el color "en anillos" concéntricos desde el centro hacia afuera. Es valioso documentar en tu libro del trabajo final de grado que el intérprete adaptativo (Faster CPython) de 3.11 acelera de igual manera las colecciones doblemente terminadas (`deque`) del módulo de C subyacente.
# */