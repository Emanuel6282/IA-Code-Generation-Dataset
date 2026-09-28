# Python 3.11 — paradigma estrictamente iterativo.
# No se utilizan características externas a la biblioteca estándar.

def is_balanced(s: str) -> bool:
    stack: list[str] = []
    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{",
    }

    for char in s:
        if char in "([{":
            stack.append(char)
        else:
            if not stack or stack.pop() != pairs[char]:
                return False

    return not stack


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True