"""Flatten Nested List - solución funcional para Python 3.11."""

from collections.abc import Iterator
from functools import reduce
from itertools import accumulate, dropwhile, repeat, takewhile
from typing import NamedTuple, Self, TypeAlias

# Tipo recursivo: una lista cuyos elementos son enteros u otras listas anidadas.
NestedList: TypeAlias = "list[int | NestedList]"

# Lista enlazada inmutable: (cabeza, resto) o None si está vacía. Permite añadir al
# principio en O(1) sin copiar nada, a diferencia de concatenar tuplas.
Cons: TypeAlias = "tuple[object, Cons] | None"


def _push_all(items: NestedList, stack: Cons) -> Cons:
    """Devuelve una pila nueva con `items` delante de `stack`, conservando su orden."""
    return reduce(lambda acc, item: (item, acc), reversed(items), stack)


class FlattenState(NamedTuple):
    """Estado inmutable del recorrido: elementos pendientes y salida en orden inverso."""

    pending: Cons
    out: Cons = None

    def done(self) -> bool:
        return self.pending is None

    def step(self) -> Self:
        """Procesa el primer pendiente y devuelve un estado nuevo, sin mutar el actual.

        typing.Self (PEP 673, nuevo en Python 3.11) indica que el método retorna una
        instancia de la misma clase, sin repetir su nombre ni usar TypeVar.
        """
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


def _check_case(case: tuple[str, NestedList, list[int]]) -> AssertionError | None:
    """Evalúa un caso y devuelve el error (sin lanzarlo) o None si pasa."""
    name, nested, expected = case
    result = flatten(nested)
    if result == expected:
        return None
    error = AssertionError(f"esperado {expected}, obtenido {result}")
    # BaseException.add_note() (PEP 678, nuevo en Python 3.11)
    error.add_note(f"Caso fallido: {name}")
    return error


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

    # map evalúa todos los casos; filter se queda solo con los fallos.
    failures: tuple[AssertionError, ...] = tuple(
        filter(lambda outcome: outcome is not None, map(_check_case, test_cases))
    )

    if failures:
        # ExceptionGroup (PEP 654, nuevo en Python 3.11): reporta todos los fallos juntos
        raise ExceptionGroup("Fallaron casos de prueba de flatten", failures)

    assert not failures
    print("Todas las pruebas pasaron.")