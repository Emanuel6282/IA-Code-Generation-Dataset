"""Flatten Nested List - solución funcional para Python 3.9."""

from functools import reduce
from itertools import accumulate, dropwhile, repeat, takewhile
from typing import Iterator, Optional, Union

# Python 3.9 (PEP 585): list[...] y tuple[...] nativos en lugar de typing.List/Tuple.
NestedList = list[Union[int, "NestedList"]]

# Lista enlazada inmutable: (cabeza, resto) o None si está vacía. Permite añadir al
# principio en O(1) sin copiar nada, a diferencia de concatenar tuplas.
Cons = Optional[tuple[object, "Cons"]]

# Estado inmutable del recorrido: (elementos pendientes, salida en orden inverso).
State = tuple[Cons, Cons]


def _push_all(items: NestedList, stack: Cons) -> Cons:
    """Devuelve una pila nueva con `items` delante de `stack`, conservando su orden."""
    return reduce(lambda acc, item: (item, acc), reversed(items), stack)


def _step(state: State, _: None) -> State:
    """Un paso del recorrido: procesa el primer pendiente y devuelve un estado nuevo."""
    (head, rest), out = state
    if isinstance(head, list):
        return (_push_all(head, rest), out)  # sustituye la sublista por su contenido
    return (rest, (head, out))               # el entero pasa a la salida


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
    states = accumulate(repeat(None), _step, initial=(_push_all(nested, None), None))
    _, out = next(dropwhile(lambda state: state[0] is not None, states))
    return list(reversed(tuple(_values(out))))  # la salida se acumuló al revés


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

    # map calcula el resultado de cada caso; filter se queda solo con los que fallan.
    results = map(lambda case: (case, flatten(case[1])), test_cases)
    failures = tuple(filter(lambda pair: pair[1] != pair[0][2], results))

    assert not failures, "\n".join(
        map(lambda pair: f"{pair[0][0]}: esperado {pair[0][2]}, obtenido {pair[1]}", failures)
    )
    print("Todas las pruebas pasaron.")