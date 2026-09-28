# P03: Flatten Nested List — Python 3.9 (codigo base para refactoring)
from typing import Any, List

def flatten(nested: Any) -> List[int]:
    result = []
    stack = [nested]
    while stack:
        item = stack.pop()
        if isinstance(item, list):
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)
    return result

assert flatten([1,[2,3],[4,[5,6]]]) == [1,2,3,4,5,6]
assert flatten([]) == []
assert flatten([1,2,3]) == [1,2,3]
