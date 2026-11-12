def binary_search(arr: list[int], target: int) -> int:
    left = 0
    right = len(arr) - 1

    while left <= right:
        mid = left + (right - left) // 2

        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1


# Test 1: Objetivo encontrado
assert binary_search([1, 3, 5, 7, 9], 5) == 2

# Test 2: Objetivo no encontrado
assert binary_search([1, 3, 5, 7, 9], 4) == -1

# Test 3: Arreglo vacío
assert binary_search([], 5) == -1

print("All tests passed")