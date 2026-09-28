# Python 3.11
# Paradigma: Funcional
#
# Características utilizadas:
# - map(): transforma cada elemento de la lista.
# - filter(): conserva únicamente los resultados válidos.
# - reduce(): combina funcionalmente las listas resultantes.
# - Tuplas: se utilizan como estructura inmutable durante el procesamiento.
# - No se utilizan for, while ni estado mutable.
#
# Python 3.11 no incorpora una característica específica que mejore
# directamente este algoritmo, por lo que se utilizan las construcciones
# funcionales idiomáticas disponibles en esta versión.

from functools import reduce


def flatten(nested_list):
    def flatten_item(item):
        if isinstance(item, list):
            return reduce(
                lambda accumulated, element:
                    accumulated + flatten_item(element),
                item,
                ()
            )
        return (item,)

    flattened = reduce(
        lambda accumulated, element:
            accumulated + flatten_item(element),
        map(lambda element: element, nested_list),
        ()
    )

    return list(
        filter(
            lambda item: isinstance(item, int),
            flattened
        )
    )


# Casos de prueba
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, [2, [3, [4, 5]]]]) == [1, 2, 3, 4, 5]