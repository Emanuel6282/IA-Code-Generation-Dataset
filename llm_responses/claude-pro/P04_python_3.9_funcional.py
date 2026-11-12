"""Balanced Parentheses - solución funcional para Python 3.9."""

from functools import reduce
from types import MappingProxyType
from typing import Optional, Union

# Pila inmutable como lista enlazada: () si está vacía, o (cima, resto). Apilar y
# desapilar crean o reutilizan nodos en O(1), sin copiar nada.
# Python 3.9 (PEP 585): tuple[...] nativo en lugar de typing.Tuple.
Stack = Union[tuple[()], tuple[str, "Stack"]]

# Estado del recorrido: la pila de aperturas pendientes, o None si ya se detectó un
# error (un cierre que no corresponde). None es absorbente: una vez fallado, sigue así.
State = Optional[Stack]

# Mapeo inmutable cierre -> apertura (MappingProxyType es una vista de solo lectura).
PAIRS = MappingProxyType({")": "(", "]": "[", "}": "{"})


def _step(state: State, char: str) -> State:
    """Procesa un carácter y devuelve el nuevo estado, sin modificar el anterior."""
    if state is None:
        return None                                # ya falló: el error se propaga
    if char in PAIRS:
        # Un cierre debe corresponder a la cima de la pila: se desapila devolviendo
        # el resto; si no corresponde (o la pila está vacía), el estado pasa a None.
        return state[1] if state and state[0] == PAIRS[char] else None
    return (char, state)                           # apertura: se apila


def is_balanced(s: str) -> bool:
    """Retorna True si los paréntesis, corchetes y llaves de `s` están balanceados
    y se cierran en el orden correcto; False en caso contrario."""
    # reduce pliega la cadena carácter a carácter partiendo de la pila vacía. La
    # cadena está balanceada solo si al final no hubo error y la pila quedó vacía.
    return reduce(_step, s, ()) == ()


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

    # map calcula el resultado de cada caso; filter se queda solo con los que fallan.
    results = map(lambda case: (case, is_balanced(case[1])), test_cases)
    failures = tuple(filter(lambda pair: pair[1] is not pair[0][2], results))

    assert not failures, "\n".join(
        map(lambda pair: f"{pair[0][0]} ({pair[0][1]!r}): esperado {pair[0][2]}, obtenido {pair[1]}", failures)
    )
    print("Todas las pruebas pasaron.")