# P07: 2D Flood Fill — Python 3.14
# Paradigma: ESTRICTAMENTE FUNCIONAL
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Inmutabilidad:
#    tuple y frozenset representan la matriz,
#    las posiciones visitadas y la frontera.
#
# 2. map():
#    Genera vecinos y construye la matriz resultante.
#
# 3. filter():
#    Selecciona los vecinos válidos.
#
# 4. functools.reduce():
#    Acumula vecinos sin modificar colecciones.
#
# 5. Recursión:
#    Sustituye los bucles imperativos.
#
# 6. Python 3.14 — PEP 649 y PEP 749:
#    Evaluación diferida de anotaciones.
#    Permite utilizar anotaciones modernas sin
#    afectar la lógica del algoritmo.
#
# NOTA:
# map, filter, reduce, tuple y frozenset existían
# antes de Python 3.14. Las anotaciones genéricas
# list[...] se introdujeron en Python 3.9.
#
# Biblioteca estándar únicamente.
# Sin for, while ni estado mutable.

from functools import reduce


def flood_fill(
    grid: list[list[int]],
    row: int,
    col: int,
    new_color: int
) -> list[list[int]]:

    # Convertir la matriz a una representación inmutable.
    matrix = tuple(map(tuple, grid))

    # Obtener el color original.
    original_color = matrix[row][col]

    # Si ya coincide, devolver la matriz sin cambios.
    if original_color == new_color:
        return grid

    rows = len(matrix)
    cols = len(matrix[0])

    # Conectividad de cuatro direcciones.
    directions = (
        (-1, 0),  # Arriba
        (1, 0),   # Abajo
        (0, -1),  # Izquierda
        (0, 1)    # Derecha
    )

    # Función pura para validar posiciones.
    def valid_neighbor(position: tuple[int, int]) -> bool:
        r, c = position

        return (
            0 <= r < rows
            and 0 <= c < cols
            and matrix[r][c] == original_color
        )

    # Generar vecinos mediante map() y filter().
    def neighbors(
        position: tuple[int, int]
    ) -> frozenset[tuple[int, int]]:

        r, c = position

        candidates = map(
            lambda direction: (
                r + direction[0],
                c + direction[1]
            ),
            directions
        )

        return frozenset(
            filter(valid_neighbor, candidates)
        )

    # Expandir recursivamente la región conectada.
    def expand(
        frontier: frozenset[tuple[int, int]],
        visited: frozenset[tuple[int, int]]
    ) -> frozenset[tuple[int, int]]:

        # Caso base: no quedan posiciones pendientes.
        if not frontier:
            return visited

        # Acumular los vecinos de toda la frontera.
        adjacent = reduce(
            lambda accumulated, position:
                accumulated.union(neighbors(position)),
            frontier,
            frozenset()
        )

        # Excluir posiciones ya visitadas.
        new_frontier = adjacent.difference(visited)

        # Crear un nuevo conjunto inmutable.
        new_visited = visited.union(new_frontier)

        # Continuar mediante recursión funcional.
        return expand(new_frontier, new_visited)

    # Posición inicial representada como frozenset.
    start = frozenset(((row, col),))

    # Obtener todas las posiciones conectadas.
    connected = expand(start, start)

    # Construir cada fila sin modificar la original.
    def transform_row(indexed_row):
        r, values = indexed_row

        return list(
            map(
                lambda indexed_value: (
                    new_color
                    if (r, indexed_value[0]) in connected
                    else indexed_value[1]
                ),
                enumerate(values)
            )
        )

    # Crear una nueva matriz con los colores actualizados.
    return list(
        map(transform_row, enumerate(matrix))
    )


# ==================================================
# TESTS — Exactamente 3 casos usando assert
# ==================================================

# TEST 1: Ejemplo del enunciado.
assert flood_fill(
    [[1, 1, 1], [1, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]


# TEST 2: El color original ya es el nuevo color.
assert flood_fill(
    [[0, 0, 0], [0, 1, 1]],
    1, 1, 1
) == [[0, 0, 0], [0, 1, 1]]


# TEST 3: No colorear conexiones diagonales.
assert flood_fill(
    [[1, 0, 1], [0, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[1, 0, 1], [0, 2, 0], [1, 0, 1]]


print("All tests passed")