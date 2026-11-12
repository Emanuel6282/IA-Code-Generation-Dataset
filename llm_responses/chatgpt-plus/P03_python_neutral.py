def flatten(arr):
    result = []
    stack = [iter(arr)]

    while stack:
        try:
            item = next(stack[-1])

            if isinstance(item, list):
                stack.append(iter(item))
            else:
                result.append(item)

        except StopIteration:
            stack.pop()

    return result


# Test 1: Lista con varios niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

# Test 2: Lista vacía
assert flatten([]) == []

# Test 3: Anidamiento profundo y listas vacías
assert flatten([1, [[2]], [], [3, [4, [5]]]]) == [1, 2, 3, 4, 5]

print("All tests passed")