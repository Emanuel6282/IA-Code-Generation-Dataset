"""Balanced Parentheses - solución funcional para Python 3.14."""

from functools import reduce
from string.templatelib import Interpolation, Template
from types import MappingProxyType

# Sentencia `type` (PEP 695, Python 3.12+): alias recursivos sin comillas, porque su
# valor se evalúa de forma perezosa y puede referirse a sí mismo.
# Pila inmutable como lista enlazada: () si está vacía, o (cima, resto). Apilar y
# desapilar crean o reutilizan nodos en O(1), sin copiar nada.
type Stack = tuple[()] | tuple[str, Stack]

# Estado del recorrido: la pila de aperturas pendientes, o None si ya se detectó un
# error (un cierre que no corresponde). None es absorbente: una vez fallado, sigue así.
type State = Stack | None

# Mapeo inmutable cierre -> apertura (MappingProxyType es una vista de solo lectura).
PAIRS = MappingProxyType({")": "(", "]": "[", "}": "{"})

_CONVERSIONS = MappingProxyType({"r": repr, "s": str, "a": ascii, None: lambda value: value})


def _step(state: State, char: str) -> State:
    """Procesa un carácter y devuelve el nuevo estado, sin modificar el anterior.

    Usa la sentencia match con patrones estructurales sobre el par (estado, carácter):
    desestructura la pila en (cima, resto) y elige la rama sin índices.
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


def _render_part(part: str | Interpolation) -> str:
    """Convierte una parte de un Template en texto, aplicando conversión y formato."""
    if isinstance(part, Interpolation):
        return format(_CONVERSIONS[part.conversion](part.value), part.format_spec)
    return part


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, sin bucles."""
    return "".join(map(_render_part, template))


def _check_case(case: tuple[str, str, bool]) -> str | None:
    """Evalúa un caso y devuelve el mensaje de fallo, o None si pasa."""
    name, s, expected = case
    result = is_balanced(s)
    return None if result is expected else render(
        t"Caso {name!r} ({s!r}): esperado {expected}, obtenido {result}"
    )


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

    # map evalúa todos los casos; filter se queda solo con los mensajes de fallo.
    failures: tuple[str, ...] = tuple(
        filter(lambda message: message is not None, map(_check_case, test_cases))
    )

    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")