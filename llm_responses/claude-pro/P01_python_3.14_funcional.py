"""Word Frequency - solución funcional para Python 3.14."""

from functools import reduce
from string.templatelib import Interpolation, Template

_CONVERSIONS = {"r": repr, "s": str, "a": ascii, None: lambda value: value}


def _merge_counts(acc: dict[str, int], single: dict[str, int]) -> dict[str, int]:
    """Combina dos conteos sin mutar ninguno: el operador "|" crea un diccionario nuevo."""
    return acc | dict(map(lambda item: (item[0], acc.get(item[0], 0) + item[1]), single.items()))


def word_frequency(words: list[str]) -> dict[str, int]:
    """Retorna un diccionario que asocia cada palabra única con su número de apariciones."""
    # Cada palabra pasa a ser un conteo unitario {palabra: 1}; reduce los pliega desde {}.
    return reduce(_merge_counts, map(lambda word: {word: 1}, words), {})


def _render_part(part: str | Interpolation) -> str:
    """Convierte una parte de un Template en texto, aplicando conversión y formato."""
    if isinstance(part, Interpolation):
        return format(_CONVERSIONS[part.conversion](part.value), part.format_spec)
    return part


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, sin bucles."""
    return "".join(map(_render_part, template))


def _check_case(case: tuple[str, list[str], dict[str, int]]) -> str | None:
    """Evalúa un caso y devuelve el mensaje de fallo, o None si pasa."""
    name, words, expected = case
    result = word_frequency(words)
    return None if result == expected else render(
        t"Caso {name!r}: entrada {words}, esperado {expected}, obtenido {result}"
    )


if __name__ == "__main__":
    test_cases: tuple[tuple[str, list[str], dict[str, int]], ...] = (
        # Caso 1: ejemplo del enunciado
        ("ejemplo del enunciado", ["the", "fox", "the"], {"the": 2, "fox": 1}),
        # Caso 2: lista vacía
        ("lista vacía", [], {}),
        # Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        ("mayúsculas y repeticiones", ["a", "A", "a", "b", "a"], {"a": 3, "A": 1, "b": 1}),
    )

    # map evalúa todos los casos; filter se queda solo con los mensajes de fallo.
    failures: tuple[str, ...] = tuple(
        filter(lambda message: message is not None, map(_check_case, test_cases))
    )

    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")