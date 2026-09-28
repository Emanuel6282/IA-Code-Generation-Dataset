# P04: Balanced Parentheses — Python 3.9 (codigo base para refactoring)
def is_balanced(s: str) -> bool:
    stack = []
    pairs = {')': '(', ']': '[', '}': '{'}
    for char in s:
        if char in '([{':
            stack.append(char)
        elif char in ')]}':
            if not stack or stack[-1] != pairs[char]:
                return False
            stack.pop()
    return len(stack) == 0

assert is_balanced("()") == True
assert is_balanced("([)]") == False
assert is_balanced("") == True
