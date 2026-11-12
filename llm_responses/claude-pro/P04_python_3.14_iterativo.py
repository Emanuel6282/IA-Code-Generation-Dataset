"""Balanced Parentheses - solución iterativa para Python 3.14."""

from string.templatelib import Interpolation, Template

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


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, de forma iterativa.

    A diferencia de una f-string, una t-string no produce un str directamente:
    produce un Template con sus partes literales e interpolaciones por separado.
    """
    parts: list[str] = []
    for item in template:
        if isinstance(item, Interpolation):
            value = item.value
            match item.conversion:
                case "r":
                    value = repr(value)
                case "s":
                    value = str(value)
                case "a":
                    value = ascii(value)
            parts.append(format(value, item.format_spec))
        else:
            parts.append(item)
    return "".join(parts)


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
        # El mensaje solo se construye si el assert falla; se arma con una t-string.
        assert result is expected, render(t"Caso {name!r} ({s!r}): esperado {expected}, obtenido {result}")

    print("Todas las pruebas pasaron.")