def is_balanced(s: str) -> bool:
    stack = []
    pairs = {
        ')': '(',
        ']': '[',
        '}': '{'
    }

    for char in s:
        if char in "([{":
            stack.append(char)
        elif char in ")]}":
            if not stack or stack.pop() != pairs[char]:
                return False

    return not stack


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True