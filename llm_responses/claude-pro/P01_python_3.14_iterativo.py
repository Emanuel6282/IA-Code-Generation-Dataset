"""Word Frequency - solución iterativa para Python 3.14."""

from string.templatelib import Interpolation, Template


def word_frequency(words: list[str]) -> dict[str, int]:
    """Retorna un diccionario que asocia cada palabra única con su número de apariciones."""
    frequencies: dict[str, int] = {}
    for word in words:
        if word in frequencies:
            frequencies[word] += 1
        else:
            frequencies[word] = 1
    return frequencies


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
    test_cases: list[tuple[str, list[str], dict[str, int]]] = [
        # Caso 1: ejemplo del enunciado
        ("ejemplo del enunciado", ["the", "fox", "the"], {"the": 2, "fox": 1}),
        # Caso 2: lista vacía
        ("lista vacía", [], {}),
        # Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        ("mayúsculas y repeticiones", ["a", "A", "a", "b", "a"], {"a": 3, "A": 1, "b": 1}),
    ]

    for name, words, expected in test_cases:
        result = word_frequency(words)
        # El mensaje solo se construye si el assert falla; se arma con una t-string.
        assert result == expected, render(
            t"Caso {name!r}: entrada {words}, esperado {expected}, obtenido {result}"
        )

    print("Todas las pruebas pasaron.")