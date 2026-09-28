from functools import reduce
from typing import Any


def flatten(nested_list: list[Any]) -> list[Any]:
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista
    plana con todos los enteros en el mismo orden de aparicion.

    Implementacion en paradigma funcional: usa functools.reduce para
    acumular el resultado de forma inmutable (en cada paso se construye una
    lista nueva mediante concatenacion "+", sin mutar el acumulador
    previo), y recursion para descender en los niveles de anidamiento
    arbitrarios. No se usan bucles for/while con estado mutable.
    Compatible con Python 3.11.

    Args:
        nested_list: Lista que puede contener enteros y/o otras listas
                      anidadas a cualquier profundidad.

    Returns:
        Una lista plana (list) con todos los enteros en el mismo orden de
        aparicion. Si la lista de entrada esta vacia, retorna una lista
        vacia.
    """
    return reduce(
        lambda acumulado, elemento: acumulado + (
            flatten(elemento) if isinstance(elemento, list) else [elemento]
        ),
        nested_list,
        []
    )


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, anidamiento de varios niveles
    assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

    # Caso 2: lista vacia -> lista vacia
    assert flatten([]) == []

    # Caso 3: anidamiento profundo con listas vacias intercaladas
    assert flatten([1, [], [2, [3, [4, [5]], []], 6]]) == [1, 2, 3, 4, 5, 6]

    print("Todos los casos de prueba pasaron correctamente.")