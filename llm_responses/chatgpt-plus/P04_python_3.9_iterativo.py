# P04: Balanced Parentheses — Python 3.9
# Paradigma: Estrictamente iterativo
#
# Características utilizadas:
# 1. Anotaciones de tipos compatibles con Python 3.9.
# 2. list[str] y dict[str, str] (PEP 585, Python 3.9).
# 3. Pila mutable mediante list.
# 4. Bucle for para recorrer la cadena.
# 5. Diccionario para relacionar símbolos de cierre y apertura.
#
# Sin map(), filter(), reduce() ni recursión.
# Biblioteca estándar únicamente.


def is_balanced(s: str) -> bool:

    # Pila mutable para almacenar símbolos de apertura.
    stack: list[str] = []

    # Correspondencia entre símbolos de cierre y apertura.
    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{"
    }

    # Recorrido estrictamente iterativo.
    for char in s:

        if char in "([{":
            # Guardar símbolo de apertura.
            stack.append(char)

        else:
            # Si no existe apertura previa, no está balanceado.
            if not stack:
                return False

            # Extraer el último símbolo de apertura.
            last_open = stack.pop()

            # Comprobar que los símbolos coincidan.
            if last_open != pairs[char]:
                return False

    # La cadena está balanceada si no quedan aperturas.
    return len(stack) == 0


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") is True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") is False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") is True

print("All tests passed")