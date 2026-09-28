from functools import reduce


def binary_search(arr, target):
    def search_step(state, _):
        left, right, result = state

        if result != -1 or left > right:
            return state

        middle = (left + right) // 2

        if arr[middle] == target:
            return (left, right, middle)

        if arr[middle] < target:
            return (middle + 1, right, -1)

        return (left, middle - 1, -1)

    # Genera suficientes pasos para completar la búsqueda binaria.
    steps = map(lambda _: None, range(len(arr) + 1))

    final_state = reduce(
        search_step,
        steps,
        (0, len(arr) - 1, -1)
    )

    return final_state[2]


# Casos de prueba
assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([2, 4, 6, 8, 10], 10) == 4

print("Todos los tests pasaron correctamente.")