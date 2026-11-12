"""Balanced Parentheses - solución iterativa para Python 3.9."""

# Python 3.9 (PEP 585): genéricos nativos (dict[...], list[...]) en las anotaciones,
# sin importar typing.Dict ni typing.List.
PAIRS: dict[str, str] = {")": "(", "]": "[", "}": "{"}


def is_balanced(s: str) -> bool:
    """Retorna True si los paréntesis, corchetes y llaves de `s` están balanceados
    y se cierran en el orden correcto; False en caso contrario."""
    stack: list[str] = []
    for char in s:
        if char in PAIRS:
            # Un cierre debe corresponder a la última apertura pendiente.
            if not stack or stack.pop() != PAIRS[char]:
                return False
        else:
            stack.append(char)
    return not stack  # quedan aperturas sin cerrar -> no balanceada


if __name__ == "__main__":
    test_cases: list[tuple[str, str, bool]] = [
        # Caso 1: ejemplos balanceados del enunciado
        ("paréntesis simples", "()", True),
        ("anidamiento mixto", "{[]}", True),
        # Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
        ("orden incorrecto", "([)]", False),
        # Caso 3: aperturas sin cerrar y cierres sin abrir
        ("apertura sin cerrar", "((", False),
        ("cierre sin abrir", "())", False),
    ]

    assert is_balanced("") is True, "cadena vacía -> balanceada"

    for name, s, expected in test_cases:
        result = is_balanced(s)
        assert result is expected, f"{name} ({s!r}): esperado {expected}, obtenido {result}"

    print("Todas las pruebas pasaron.")