# Python 3.9 — paradigma estrictamente iterativo

def is_balanced(s: str) -> bool:
    stack = []
    pairs = {
        ")": "(",
        "]": "[",
        "}": "{"
    }

    for char in s:
        if char in "([{":
            stack.append(char)
        else:
            if not stack or stack.pop() != pairs[char]:
                return False

    return len(stack) == 0


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True