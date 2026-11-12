def is_balanced(s):
    """Retorna True si los paréntesis, corchetes y llaves de `s` están balanceados
    y se cierran en el orden correcto; False en caso contrario."""
    pairs = {")": "(", "]": "[", "}": "{"}
    stack = []
    for char in s:
        if char in pairs:
            # Un cierre debe corresponder a la última apertura pendiente.
            if not stack or stack.pop() != pairs[char]:
                return False
        else:
            stack.append(char)
    return not stack  # quedan aperturas sin cerrar -> no balanceada


if __name__ == "__main__":
    # Caso 1: ejemplos balanceados del enunciado y cadena vacía
    assert is_balanced("()") is True
    assert is_balanced("{[]}") is True
    assert is_balanced("") is True

    # Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
    assert is_balanced("([)]") is False

    # Caso 3: aperturas sin cerrar y cierres sin abrir
    assert is_balanced("((") is False
    assert is_balanced("())") is False

    print("Todas las pruebas pasaron.")