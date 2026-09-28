# Python 3.14
# Paradigma: Iterativo
#
# Características utilizadas:
# - while y for: recorrido iterativo de la estructura.
# - Listas mutables: utilizadas como pila y resultado.
# - list.copy(): evita modificar la lista original.
# - reversed(): permite preservar el orden de aparición.
#
# No se utilizan:
# - map()
# - filter()
# - reduce()
# - recursión
#
# Python 3.14 no incorpora una característica específica que mejore
# directamente este algoritmo, por lo que se utiliza un enfoque iterativo
# idiomático y compatible exclusivamente con Python 3.14.

def flatten(nested_list):
    result = []
    stack = nested_list.copy()

    while stack:
        item = stack.pop()

        if isinstance(item, list):
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)

    result.reverse()
    return result


# Casos de prueba
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, [2, [3, [4, 5]]]]) == [1, 2, 3, 4, 5]