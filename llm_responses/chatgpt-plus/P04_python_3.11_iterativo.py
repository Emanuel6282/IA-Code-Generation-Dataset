# P04: Balanced Parentheses — Python 3.11
# Paradigma: Estrictamente iterativo
#
# Características utilizadas:
# 1. Anotaciones de tipos modernas: list[str] y dict[str, str].
# 2. Pila mutable mediante list.
# 3. Bucle for para recorrer la cadena.
# 4. Diccionario para relacionar cierres y aperturas.
# 5. Operaciones append() y pop() para gestionar la pila.
#
# Nota: las anotaciones genéricas se introdujeron en Python 3.9
# y siguen siendo compatibles con Python 3.11.
# No se necesita ninguna característica exclusiva de Python 3.11.
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
            # No existe una apertura previa.
            if not stack:
                return False

            # Extraer la última apertura.
            last_open = stack.pop()

            # Comprobar la correspondencia.
            if last_open != pairs[char]:
                return False

    # La pila debe quedar vacía.
    return len(stack) == 0


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") is True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") is False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") is True

print("All tests passed")