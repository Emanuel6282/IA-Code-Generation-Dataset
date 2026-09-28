# Python 3.11
# Paradigma: Iterativo
#
# Características utilizadas:
# - while: recorrido iterativo de la estructura.
# - list mutable: permite utilizar una pila para controlar el recorrido.
# - list.copy(): crea una copia de la lista inicial para no modificarla.
# - No se utilizan map(), filter(), reduce() ni recursión.

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