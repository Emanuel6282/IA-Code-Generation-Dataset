# P03: Flatten Nested List

def flatten(nested_list):
    result = []

    def _flatten(items):
        for item in items:
            if isinstance(item, list):
                _flatten(item)
            else:
                result.append(item)

    _flatten(nested_list)
    return result


# ============================================================
# CASOS DE PRUEBA
# ============================================================

assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, [2, [3, [4, 5]]]]) == [1, 2, 3, 4, 5]

print("All tests passed")