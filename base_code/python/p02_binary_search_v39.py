# P02: Binary Search — Python 3.9 (codigo base para refactoring)
from typing import List

def binary_search(arr: List[int], target: int) -> int:
    left = 0
    right = len(arr) - 1
    while left <= right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1

assert binary_search([1,3,5,7,9], 5) == 2
assert binary_search([1,3,5,7,9], 4) == -1
assert binary_search([], 1) == -1
