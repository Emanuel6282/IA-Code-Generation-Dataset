def max_subarray_sum(numbers):
    current_sum = numbers[0]
    max_sum = numbers[0]

    for number in numbers[1:]:
        current_sum = max(number, current_sum + number)
        max_sum = max(max_sum, current_sum)

    return max_sum


# Tests
assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
assert max_subarray_sum([-5, -2, -8, -1]) == -1
assert max_subarray_sum([1, 2, 3, 4]) == 10

print("All tests passed")