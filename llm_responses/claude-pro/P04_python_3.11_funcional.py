"""Balanced Parentheses - solución funcional para Python 3.11."""

from functools import reduce
from types import MappingProxyType
from typing import TypeAlias

# Pila inmutable como lista enlazada: () si está vacía, o (cima, resto). Apilar y
# desapilar crean o reutilizan nodos en O(1), sin copiar nada.
Stack: TypeAlias = "tuple[()] | tuple[str, Stack]"

# Estado del recorrido: la pila de aperturas pendientes, o None si ya se detectó un
# error (un cierre que no corresponde). None es absorbente: una vez fallado, sigue así.
State: TypeAlias = "Stack | None"

# Mapeo inmutable cierre -> apertura (MappingProxyType es una vista de solo lectura).
PAIRS = MappingProxyType({")": "(", "]": "[", "}": "{"})


def _step(state: State, char: str) -> State:
    """Procesa un carácter y devuelve el nuevo estado, sin modificar el anterior.

    Usa la sentencia match (Python 3.10+) con patrones estructurales sobre el par
    (estado, carácter): desestructura la pila en (cima, resto) y elige la rama sin
    índices ni comprobaciones manuales de longitud.
    """
    match state, char:
        case None, _:
            return None                                    # ya falló: el error se propaga
        case (top, rest), (")" | "]" | "}") if top == PAIRS[char]:
            return rest                                    # cierre correcto: se desapila
        case _, (")" | "]" | "}"):
            return None                                    # cierre sin apertura o que no corresponde
        case _, ("(" | "[" | "{"):
            return (char, state)                           # apertura: se apila
        case _:
            raise ValueError(f"Carácter no válido: {char!r}")


def is_balanced(s: str) -> bool:
    """Retorna True si los paréntesis, corchetes y llaves de `s` están balanceados
    y se cierran en el orden correcto; False en caso contrario.

    Raises:
        ValueError: si `s` contiene un carácter que no es ( ) [ ] { }.
    """
    # reduce pliega la cadena carácter a carácter partiendo de la pila vacía. La
    # cadena está balanceada solo si al final no hubo error y la pila quedó vacía.
    return reduce(_step, s, ()) == ()


def _check_case(case: tuple[str, str, bool]) -> AssertionError | None:
    """Evalúa un caso y devuelve el error (sin lanzarlo) o None si pasa."""
    name, s, expected = case
    result = is_balanced(s)
    if result is expected:
        return None
    error = AssertionError(f"esperado {expected}, obtenido {result}")
    # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
    error.add_note(f"Caso fallido: {name} | entrada: {s!r}")
    return error


if __name__ == "__main__":
    test_cases: tuple[tuple[str, str, bool], ...] = (
        # Caso 1: ejemplos balanceados del enunciado
        ("paréntesis simples", "()", True),
        ("anidamiento mixto", "{[]}", True),
        # Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
        ("orden incorrecto", "([)]", False),
        # Caso 3: aperturas sin cerrar y cierres sin abrir
        ("apertura sin cerrar", "((", False),
        ("cierre sin abrir", "())", False),
    )

    assert is_balanced("") is True, "cadena vacía -> balanceada"

    # map evalúa todos los casos; filter se queda solo con los fallos.
    failures: tuple[AssertionError, ...] = tuple(
        filter(lambda outcome: outcome is not None, map(_check_case, test_cases))
    )

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): reporta todos los fallos juntos
        raise ExceptionGroup("Fallaron casos de prueba de is_balanced", failures)

    assert not failures
    print("Todas las pruebas pasaron.")