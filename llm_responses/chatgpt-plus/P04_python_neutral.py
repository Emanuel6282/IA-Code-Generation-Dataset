# Balanced Parentheses
# Solución utilizando una pila (stack)

def is_balanced(s: str) -> bool:
    stack = []

    # Correspondencia entre símbolos de cierre y apertura
    pairs = {
        ')': '(',
        ']': '[',
        '}': '{'
    }

    for char in s:
        if char in "([{":
            # Guardar símbolo de apertura
            stack.append(char)
        else:
            # Comprobar que exista una apertura correspondiente
            if not stack or stack.pop() != pairs[char]:
                return False

    # La pila debe quedar vacía
    return len(stack) == 0


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") == True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") == False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") == True

print("All tests passed")