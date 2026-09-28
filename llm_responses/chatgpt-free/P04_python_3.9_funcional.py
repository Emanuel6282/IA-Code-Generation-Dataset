# Python 3.9 — paradigma estrictamente funcional.
from functools import reduce
from typing import Tuple


def is_balanced(s: str) -> bool:
    pairs = {
        ")": "(",
        "]": "[",
        "}": "{"
    }

    def process(state: Tuple[Tuple[str, ...], bool], char: str) -> Tuple[Tuple[str, ...], bool]:
        stack, valid = state

        if not valid:
            return stack, False

        if char in "([{":
            return stack + (char,), True

        return (
            stack[:-1],
            bool(stack) and stack[-1] == pairs[char]
        )

    stack, valid = reduce(
        process,
        s,
        ((), True)
    )

    return valid and not stack


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True