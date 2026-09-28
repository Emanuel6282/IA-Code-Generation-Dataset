# Python 3.14
# Paradigma: Funcional
#
# Características utilizadas:
# - map(): procesa los elementos de cada lista.
# - filter(): conserva únicamente los enteros.
# - reduce(): combina los resultados de forma inmutable.
# - Tuplas: utilizadas como estructura inmutable durante el procesamiento.
# - Recursión funcional: permite manejar cualquier nivel de anidamiento.
#
# No se utilizan:
# - for
# - while
# - estado mutable
#
# Python 3.14 no introduce una característica específica que mejore
# directamente este algoritmo, por lo que se utilizan las construcciones
# funcionales disponibles en la biblioteca estándar.

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