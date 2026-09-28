# P07: Flood Fill — Python 3.9 (codigo base para refactoring)
from typing import List
from collections import deque

def flood_fill(grid: List[List[int]], row: int, col: int, new_color: int) -> List[List[int]]:
    original_color = grid[row][col]
    if original_color == new_color:
        return grid
    rows = len(grid)
    cols = len(grid[0])
    queue = deque()
    queue.append((row, col))
    grid[row][col] = new_color
    while queue:
        r, c = queue.popleft()
        for dr, dc in [(-1,0),(1,0),(0,-1),(0,1)]:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == original_color:
                grid[nr][nc] = new_color
                queue.append((nr, nc))
    return grid

assert flood_fill([[1,1,1],[1,1,0],[1,0,1]], 1, 1, 2) == [[2,2,2],[2,2,0],[2,0,1]]
assert flood_fill([[0]], 0, 0, 5) == [[5]]
assert flood_fill([[0,0],[0,0]], 0, 0, 3) == [[3,3],[3,3]]
