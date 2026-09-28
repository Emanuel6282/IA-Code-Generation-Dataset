def binary_search(arr, target):
    left = 0
    right = len(arr) - 1

    while left <= right:
        middle = (left + right) // 2

        if arr[middle] == target:
            return middle
        elif arr[middle] < target:
            left = middle + 1
        else:
            right = middle - 1

    return -1


# Casos de prueba
assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([2, 4, 6, 8, 10], 10) == 4

print("Todos los tests pasaron correctamente.")