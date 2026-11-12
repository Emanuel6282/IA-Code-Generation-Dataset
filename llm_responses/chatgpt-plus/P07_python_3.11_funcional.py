# P07: 2D Flood Fill — Python 3.11
# Paradigma: ESTRICTAMENTE FUNCIONAL
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Inmutabilidad:
#    tuple y frozenset para representar la matriz,
#    la frontera y los píxeles visitados.
#
# 2. map():
#    Genera posiciones vecinas y construye el resultado.
#
# 3. filter():
#    Selecciona los vecinos válidos.
#
# 4. functools.reduce():
#    Combina conjuntos inmutables de vecinos.
#
# 5. Recursión:
#    Sustituye los bucles imperativos.
#
# 6. Python 3.11:
#    Utiliza anotaciones modernas y se beneficia
#    potencialmente de la especialización adaptativa
#    del intérprete (PEP 659).
#
# NOTA:
# Las anotaciones list[...] se introdujeron en
# Python 3.9. map, filter y reduce también existían
# antes de Python 3.11.
#
# Biblioteca estándar únicamente.
# Sin bucles for/while ni mutación de estado.

from functools import reduce


def flood_fill(
    grid: list[list[int]],
    row: int,
    col: int,
    new_color: int
) -> list[list[int]]:

    # Crear una representación inmutable.
    matrix = tuple(map(tuple, grid))

    original_color = matrix[row][col]

    # Si el color coincide, devolver sin cambios.
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

    # Comprobar si una posición es válida.
    def valid_neighbor(position: tuple[int, int]) -> bool:
        r, c = position

        return (
            0 <= r < rows
            and 0 <= c < cols
            and matrix[r][c] == original_color
        )

    # Generar los vecinos mediante map y filter.
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

        # Caso base.
        if not frontier:
            return visited

        # Unir los vecinos de toda la frontera.
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

        # Continuar la expansión funcional.
        return expand(new_frontier, new_visited)

    # Descubrir la región conectada.
    start = frozenset(((row, col),))

    connected = expand(start, start)

    # Transformar cada fila sin modificar la original.
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

    # Construir una nueva matriz.
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