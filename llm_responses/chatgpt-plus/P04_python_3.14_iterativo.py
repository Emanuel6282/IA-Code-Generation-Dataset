# P04: Balanced Parentheses — Python 3.14
# Paradigma: Estrictamente iterativo
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[str] y dict[str, str].
#
# 2. Pila mutable mediante list:
#    Permite almacenar símbolos de apertura.
#
# 3. Bucle for:
#    Procesa cada carácter de manera iterativa.
#
# 4. Diccionario:
#    Relaciona cada símbolo de cierre con su apertura.
#
# 5. append() y pop():
#    Implementan las operaciones de la pila.
#
# COMPATIBILIDAD CON PYTHON 3.14:
# - Las anotaciones genéricas están disponibles desde 3.9.
# - Python 3.14 incluye mejoras del intérprete.
# - No se requiere ninguna característica exclusiva de 3.14.
#
# Sin map(), filter(), reduce() ni recursión.
# Biblioteca estándar únicamente.


def is_balanced(s: str) -> bool:

    # Pila mutable para símbolos de apertura.
    stack: list[str] = []

    # Correspondencia entre cierres y aperturas.
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
            # No existe apertura previa.
            if not stack:
                return False

            # Extraer la última apertura.
            last_open = stack.pop()

            # Comprobar que coincida con el cierre.
            if last_open != pairs[char]:
                return False

    # Deben haberse cerrado todos los símbolos.
    return len(stack) == 0


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") is True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") is False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") is True

print("All tests passed")