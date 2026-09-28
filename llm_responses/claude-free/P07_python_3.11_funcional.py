from functools import reduce
from typing import Self


def flood_fill(grid: list[list[int]], row: int, col: int, new_color: int) -> list[list[int]]:
    old_color = grid[row][col]

    if old_color == new_color:
        return grid

    rows = len(grid)
    cols = len(grid[0])

    def in_bounds_and_same_color(pos: tuple[int, int]) -> bool:
        r, c = pos
        return 0 <= r < rows and 0 <= c < cols and grid[r][c] == old_color

    def neighbors(pos: tuple[int, int]) -> tuple[tuple[int, int], ...]:
        r, c = pos
        candidates = ((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
        return tuple(filter(in_bounds_and_same_color, candidates))

    def expand(visited: frozenset, frontier: frozenset) -> frozenset:
        if not frontier:
            return visited
        new_visited = visited | frontier
        next_candidates = reduce(
            lambda acc, pos: acc + list(neighbors(pos)), frontier, []
        )
        next_frontier = frozenset(next_candidates) - new_visited
        return expand(new_visited, next_frontier)

    filled_positions = expand(frozenset(), frozenset({(row, col)}))

    def build_row(r: int) -> tuple[int, ...]:
        return tuple(
            map(
                lambda c: new_color if (r, c) in filled_positions else grid[r][c],
                range(cols),
            )
        )

    new_grid = tuple(map(build_row, range(rows)))

    return list(map(list, new_grid))


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    grid1 = [[1, 1, 1], [1, 1, 0], [1, 0, 1]]
    assert flood_fill(grid1, 1, 1, 2) == [[2, 2, 2], [2, 2, 0], [2, 0, 1]], "Fallo caso 1"

    # Caso 2: el color ya es el nuevo color -> sin cambios
    grid2 = [[0, 0, 0], [0, 1, 1]]
    assert flood_fill(grid2, 1, 1, 1) == [[0, 0, 0], [0, 1, 1]], "Fallo caso 2"

    # Caso 3: matriz de un solo color, se rellena por completo
    grid3 = [[3, 3], [3, 3]]
    assert flood_fill(grid3, 0, 0, 7) == [[7, 7], [7, 7]], "Fallo caso 3"

    print("Todos los tests pasaron correctamente.")