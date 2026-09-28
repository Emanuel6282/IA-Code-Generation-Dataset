from functools import reduce


def flatten(nested_list):
    def flatten_item(item):
        if isinstance(item, list):
            return reduce(
                lambda acc, element: acc + flatten_item(element),
                item,
                []
            )
        return [item]

    return list(
        filter(
            lambda item: item is not None,
            reduce(
                lambda acc, item: acc + flatten_item(item),
                map(lambda item: item, nested_list),
                []
            )
        )
    )


# Casos de prueba
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, [2, [3, [4, 5]]]]) == [1, 2, 3, 4, 5]