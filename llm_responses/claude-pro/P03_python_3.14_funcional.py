"""Flatten Nested List - solución funcional para Python 3.14."""

from collections.abc import Iterator
from functools import reduce
from itertools import accumulate, dropwhile, repeat, takewhile
from string.templatelib import Interpolation, Template
from typing import NamedTuple

# Sentencia `type` (PEP 695, Python 3.12+): alias recursivos sin comillas, porque su
# valor se evalúa de forma perezosa y puede referirse a sí mismo.
type NestedList = list[int | NestedList]

# Lista enlazada inmutable: (cabeza, resto) o None si está vacía. Permite añadir al
# principio en O(1) sin copiar nada, a diferencia de concatenar tuplas.
type Cons = tuple[object, Cons] | None

_CONVERSIONS = {"r": repr, "s": str, "a": ascii, None: lambda value: value}


def _push_all(items: NestedList, stack: Cons) -> Cons:
    """Devuelve una pila nueva con `items` delante de `stack`, conservando su orden."""
    return reduce(lambda acc, item: (item, acc), reversed(items), stack)


class FlattenState(NamedTuple):
    """Estado inmutable del recorrido: elementos pendientes y salida en orden inverso."""

    pending: Cons
    out: Cons = None

    def done(self) -> bool:
        return self.pending is None

    # Evaluación diferida de anotaciones (PEP 649 / PEP 749, nuevo en Python 3.14):
    # el método puede anotar su retorno con la propia clase, FlattenState, aunque la
    # clase aún no exista al definirse el método, sin comillas ni
    # `from __future__ import annotations`. En Python 3.13 esto daría NameError.
    def step(self) -> FlattenState:
        """Procesa el primer pendiente y devuelve un estado nuevo, sin mutar el actual."""
        head, rest = self.pending
        if isinstance(head, list):
            return self._replace(pending=_push_all(head, rest))   # sublista -> su contenido
        return self._replace(pending=rest, out=(head, self.out))  # entero -> a la salida


def _values(cons: Cons) -> Iterator[object]:
    """Recorre una lista enlazada de forma perezosa, del primer nodo al último."""
    nodes = accumulate(repeat(None), lambda node, _: node[1], initial=cons)
    return map(lambda node: node[0], takewhile(lambda node: node is not None, nodes))


def flatten(nested: NestedList) -> list[int]:
    """Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.

    No usa recursión: `accumulate` genera la secuencia perezosa de estados y
    `dropwhile` avanza hasta el primero sin pendientes. Así funciona con cualquier
    nivel de anidamiento, sin alcanzar el límite de recursión de Python.
    """
    states = accumulate(repeat(None), lambda state, _: state.step(),
                        initial=FlattenState(pending=_push_all(nested, None)))
    final = next(dropwhile(lambda state: not state.done(), states))
    return list(reversed(tuple(_values(final.out))))  # la salida se acumuló al revés


def _render_part(part: str | Interpolation) -> str:
    """Convierte una parte de un Template en texto, aplicando conversión y formato."""
    if isinstance(part, Interpolation):
        return format(_CONVERSIONS[part.conversion](part.value), part.format_spec)
    return part


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, sin bucles."""
    return "".join(map(_render_part, template))


def _check_case(case: tuple[str, NestedList, list[int]]) -> str | None:
    """Evalúa un caso y devuelve el mensaje de fallo, o None si pasa."""
    name, nested, expected = case
    result = flatten(nested)
    return None if result == expected else render(
        t"Caso {name!r}: esperado {expected}, obtenido {result}"
    )


if __name__ == "__main__":
    # Anidamiento profundo (más allá del límite de recursión por defecto, ~1000),
    # construido con reduce en lugar de un bucle.
    deep: NestedList = reduce(lambda acc, _: [acc], range(5000), [7])

    test_cases: tuple[tuple[str, NestedList, list[int]], ...] = (
        # Caso 1: ejemplo del enunciado
        ("ejemplo del enunciado", [1, [2, 3], [4, [5, 6]]], [1, 2, 3, 4, 5, 6]),
        # Caso 2: lista vacía y sublistas vacías anidadas
        ("listas vacías", [[], [[]], [[], [[]]]], []),
        # Caso 3: anidamiento muy profundo
        ("anidamiento profundo", [0, deep, 8], [0, 7, 8]),
    )

    assert flatten([]) == [], "lista vacía -> lista vacía"

    # map evalúa todos los casos; filter se queda solo con los mensajes de fallo.
    failures: tuple[str, ...] = tuple(
        filter(lambda message: message is not None, map(_check_case, test_cases))
    )

    assert not failures, "\n".join(failures)
    print("Todas las pruebas pasaron.")