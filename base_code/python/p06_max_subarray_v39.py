# P06: Max Subarray Sum (Kadane) — Python 3.9 (codigo base para refactoring)
from typing import List

def max_subarray_sum(arr: List[int]) -> int:
    current_max = arr[0]
    global_max = arr[0]
    for i in range(1, len(arr)):
        if current_max + arr[i] > arr[i]:
            current_max = current_max + arr[i]
        else:
            current_max = arr[i]
        if current_max > global_max:
            global_max = current_max
    return global_max

assert max_subarray_sum([-2,1,-3,4,-1,2,1,-5,4]) == 6
assert max_subarray_sum([1]) == 1
assert max_subarray_sum([-2,-1]) == -1
