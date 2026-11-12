"""Balanced Parentheses - solución iterativa para Python 3.11."""

PAIRS: dict[str, str] = {")": "(", "]": "[", "}": "{"}


def is_balanced(s: str) -> bool:
    """Retorna True si los paréntesis, corchetes y llaves de `s` están balanceados
    y se cierran en el orden correcto; False en caso contrario.

    Raises:
        ValueError: si `s` contiene un carácter que no es ( ) [ ] { }.
    """
    stack: list[str] = []
    for char in s:
        # Sentencia match (Python 3.10+): cada rama describe un grupo de caracteres
        # con patrones alternativos `|`, más legible que una cadena de if/elif.
        match char:
            case "(" | "[" | "{":
                stack.append(char)
            case ")" | "]" | "}":
                # Un cierre debe corresponder a la última apertura pendiente.
                if not stack or stack.pop() != PAIRS[char]:
                    return False
            case _:
                raise ValueError(f"Carácter no válido: {char!r}")
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

    # Se ejecutan todos los casos aunque alguno falle y se reportan juntos.
    failures: list[AssertionError] = []
    for name, s, expected in test_cases:
        try:
            result = is_balanced(s)
            assert result is expected, f"esperado {expected}, obtenido {result}"
        except AssertionError as error:
            # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
            error.add_note(f"Caso fallido: {name} | entrada: {s!r}")
            failures.append(error)

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): agrupa todos los fallos
        raise ExceptionGroup("Fallaron casos de prueba de is_balanced", failures)

    print("Todas las pruebas pasaron.")