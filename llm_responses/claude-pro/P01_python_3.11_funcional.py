"""Word Frequency - solución funcional para Python 3.11."""

from functools import reduce


def _merge_counts(acc: dict[str, int], single: dict[str, int]) -> dict[str, int]:
    """Combina dos conteos sin mutar ninguno: el operador "|" crea un diccionario nuevo."""
    return acc | dict(map(lambda item: (item[0], acc.get(item[0], 0) + item[1]), single.items()))


def word_frequency(words: list[str]) -> dict[str, int]:
    """Retorna un diccionario que asocia cada palabra única con su número de apariciones."""
    # Cada palabra pasa a ser un conteo unitario {palabra: 1}; reduce los pliega desde {}.
    return reduce(_merge_counts, map(lambda word: {word: 1}, words), {})


def _check_case(case: tuple[str, list[str], dict[str, int]]) -> AssertionError | None:
    """Evalúa un caso de prueba y devuelve el error (sin lanzarlo) o None si pasa."""
    name, words, expected = case
    result = word_frequency(words)
    if result == expected:
        return None
    error = AssertionError(f"esperado {expected}, obtenido {result}")
    # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
    error.add_note(f"Caso fallido: {name} | entrada: {words}")
    return error


if __name__ == "__main__":
    test_cases: tuple[tuple[str, list[str], dict[str, int]], ...] = (
        # Caso 1: ejemplo del enunciado
        ("ejemplo del enunciado", ["the", "fox", "the"], {"the": 2, "fox": 1}),
        # Caso 2: lista vacía
        ("lista vacía", [], {}),
        # Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        ("mayúsculas y repeticiones", ["a", "A", "a", "b", "a"], {"a": 3, "A": 1, "b": 1}),
    )

    # map evalúa todos los casos; filter se queda solo con los fallos.
    failures: tuple[AssertionError, ...] = tuple(
        filter(lambda outcome: outcome is not None, map(_check_case, test_cases))
    )

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): reporta todos los fallos juntos
        raise ExceptionGroup("Fallaron casos de prueba de word_frequency", failures)

    # Mecanismo estándar: assert explícito sobre el resultado global
    assert not failures
    print("Todas las pruebas pasaron.")