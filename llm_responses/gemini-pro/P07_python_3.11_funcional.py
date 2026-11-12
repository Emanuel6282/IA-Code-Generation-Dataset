import functools

# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. PEP 678 (Exception Notes): Uso de `add_note()` para enriquecer el contexto de los errores de validación sin alterar la inmutabilidad del mensaje de la excepción original. Es especialmente útil en pipelines funcionales donde los stack traces de `reduce` o `map` pueden volverse difíciles de leer.
# 2. Inmutabilidad Absoluta (`frozenset`): Manejo del estado del recorrido sin variables mutables. El historial de píxeles visitados fluye generando nuevos conjuntos constantes en cada transición.
# 3. Plegado de Estado (`functools.reduce`): Reemplazo total de iteradores `while` o `for`. La exploración del grafo en 4 direcciones se resuelve matemáticamente acumulando los estados inmutables a través de una función pura.
# 4. Proyección Declarativa (`map`): La reconstrucción de la matriz resultante se delega a funciones de orden superior, eliminando la mutación destructiva (`grid[r][c] = new_color`).

def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    if not grid or not grid[0]:
        return grid
        
    rows = len(grid)
    cols = len(grid[0])
    
    # Manejo defensivo con notas de excepción (Python 3.11)
    if not (0 <= row < rows and 0 <= col < cols):
        err = ValueError("Coordenadas de inicio fuera de los límites de la matriz.")
        err.add_note(f"Se recibió row={row}, col={col}. La matriz tiene un tamaño de {rows}x{cols}.")
        raise err
        
    original_color = grid[row][col]
    
    # Clausura de guarda inmutable para prevenir evaluaciones innecesarias
    if original_color == new_color:
        return grid
    
    # Función pura para acumular coordinadas conectadas sin "side effects"
    def get_connected(visited: frozenset[tuple[int, int]], current: tuple[int, int]) -> frozenset[tuple[int, int]]:
        r, c = current
        
        # Condiciones de poda: si está fuera de límites, es de otro color, o ya se visitó
        if current in visited or not (0 <= r < rows and 0 <= c < cols) or grid[r][c] != original_color:
            return visited
            
        # Transición de estado: creación de un NUEVO frozenset
        new_visited = visited | frozenset([current])
        
        # Ramificación en 4 direcciones
        directions = ((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
        
        # El plegado propaga el `new_visited` a través de todas las ramas
        return functools.reduce(get_connected, directions, new_visited)

    # 1. Generar inmutablemente el conjunto de todas las coordenadas afectadas
    pixels_to_change = get_connected(frozenset(), (row, col))
    
    # 2. Retornar una NUEVA matriz aplicando proyecciones puras, sin modificar `grid`
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

print("Todos los tests funcionales en Python 3.11 pasaron correctamente.")

# /*
# NOTA ANALÍTICA PARA EL LIBRO DE TU TFG (FP-UNA):
# 
# Al ingresar este snippet a la aplicación web de recolección de prompts, cierras una matriz de análisis muy interesante para tu documento sobre lenguajes dinámicos vs. fuertemente tipados.
# 
# 1. Estabilidad Funcional vs. Revolución Subyacente: 
# Al comparar esta versión 3.11 funcional con la 3.9 funcional, notarás que la estructura declarativa de orden superior (`reduce` + `map`) no varió. Python, a diferencia de las drásticas adiciones de Scala 3 (como las 'trailing lambdas') o Java 17 (con 'records'), asume que las primitivas de `functools` ya son su estándar. Las ventajas en 3.11 son "invisibles": mejoras sustanciales en el manejo de memoria en el CPython Adaptive Interpreter y enriquecimiento de excepciones como `add_note()`.
# 
# 2. El "Trade-Off" Funcional en Ecosistemas Imperativos:
# Para tu reporte final, este código es la muestra ideal de fricción de paradigma. Mientras que el código iterativo muta `grid` en milisegundos con cero costo extra de memoria, esta implementación funcional estricta debe instanciar constantes nuevas (`frozenset`) y copiar la matriz completa re-evaluándola celda por celda. Además, la ausencia del *Tail Call Optimization (TCO)* nativo en CPython significa que el `reduce` recursivo colapsará la pila en matrices de alta resolución. Demuestra empíricamente que Python soporta una arquitectura declarativa pura, pero penaliza su uso en algoritmos de recorrido espacial profundo.
# */