# Python 3.11 — paradigma estrictamente funcional.
# Se usan tuplas inmutables y functools.reduce() para procesar la cadena
# sin bucles, recursión ni estado mutable.

from functools import reduce


def is_balanced(s: str) -> bool:
    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{",
    }

    def process(
        state: tuple[tuple[str, ...], bool],
        char: str,
    ) -> tuple[tuple[str, ...], bool]:
        stack, valid = state

        if not valid:
            return stack, False

        if char in "([{":
            return (*stack, char), True

        if not stack or stack[-1] != pairs[char]:
            return stack, False

        return stack[:-1], True

    stack, valid = reduce(process, s, ((), True))

    return valid and not stack


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True