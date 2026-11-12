# P07: 2D Flood Fill — Python 3.9
# Paradigma: ESTRICTAMENTE FUNCIONAL
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Inmutabilidad:
#    Se utilizan tuplas y frozenset para representar
#    la matriz, los píxeles visitados y la frontera.
#
# 2. map():
#    Transforma la matriz y genera sus nuevas filas.
#
# 3. filter():
#    Selecciona únicamente vecinos válidos.
#
# 4. functools.reduce():
#    Acumula los píxeles conectados sin mutación.
#
# 5. Recursión:
#    Sustituye los bucles imperativos.
#
# 6. Anotaciones list[list[int]]:
#    Sintaxis de tipos genéricos introducida
#    en Python 3.9 mediante PEP 585.
#
# Biblioteca estándar únicamente.
# Sin bucles for/while ni estado mutable.

from functools import reduce


def flood_fill(
    grid: list[list[int]],
    row: int,
    col: int,
    new_color: int
) -> list[list[int]]:

    # Representación inmutable de la matriz.
    matrix = tuple(map(tuple, grid))

    original_color = matrix[row][col]

    # Si el color ya coincide, devolver sin cambios.
    if original_color == new_color:
        return grid

    rows = len(matrix)
    cols = len(matrix[0])

    # Desplazamientos en cuatro direcciones.
    directions = (
        (-1, 0),
        (1, 0),
        (0, -1),
        (0, 1)
    )

    def valid_neighbor(position: tuple[int, int]) -> bool:
        r, c = position

        return (
            0 <= r < rows
            and 0 <= c < cols
            and matrix[r][c] == original_color
        )

    def neighbors(position: tuple[int, int]) -> tuple[tuple[int, int], ...]:
        r, c = position

        # map(): calcular las posiciones vecinas.
        candidates = map(
            lambda direction: (
                r + direction[0],
                c + direction[1]
            ),
            directions
        )

        # filter(): conservar solo posiciones válidas.
        return tuple(filter(valid_neighbor, candidates))

    def expand(
        frontier: frozenset[tuple[int, int]],
        visited: frozenset[tuple[int, int]]
    ) -> frozenset[tuple[int, int]]:

        # Condición de terminación.
        if not frontier:
            return visited

        # reduce(): acumular vecinos sin modificar
        # ningún conjunto existente.
        adjacent = reduce(
            lambda accumulated, position: (
                accumulated.union(neighbors(position))
            ),
            frontier,
            frozenset()
        )

        # Eliminar posiciones ya visitadas.
        new_frontier = adjacent.difference(visited)

        # Crear un nuevo conjunto inmutable.
        new_visited = visited.union(new_frontier)

        # Recursión funcional.
        return expand(new_frontier, new_visited)

    # Encontrar toda la región conectada.
    connected = expand(
        frozenset(((row, col),)),
        frozenset(((row, col),))
    )

    # Crear una nueva matriz mediante map().
    # No se modifica la matriz original.
    def transform_row(index_and_row):
        r, values = index_and_row

        return list(
            map(
                lambda item: (
                    new_color
                    if (r, item[0]) in connected
                    else item[1]
                ),
                enumerate(values)
            )
        )

    return list(map(transform_row, enumerate(matrix)))


# ==================================================
# TESTS — Exactamente 3 casos usando assert
# ==================================================

# TEST 1: Ejemplo del enunciado.
assert flood_fill(
    [[1, 1, 1], [1, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]]


# TEST 2: El color ya es el nuevo color.
assert flood_fill(
    [[0, 0, 0], [0, 1, 1]],
    1, 1, 1
) == [[0, 0, 0], [0, 1, 1]]


# TEST 3: No se colorean conexiones diagonales.
assert flood_fill(
    [[1, 0, 1], [0, 1, 0], [1, 0, 1]],
    1, 1, 2
) == [[1, 0, 1], [0, 2, 0], [1, 0, 1]]


print("All tests passed")