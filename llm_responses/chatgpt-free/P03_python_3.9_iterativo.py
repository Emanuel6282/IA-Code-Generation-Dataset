# P03: Flatten Nested List — Python 3.9
#
# Paradigma: Iterativo
#
# Características utilizadas:
# 1. list:
#    Se utilizan listas mutables como pila de elementos pendientes
#    y como resultado final.
#
# 2. while:
#    Permite recorrer todos los niveles de anidamiento sin utilizar
#    recursión.
#
# No se utilizan:
# - map()
# - filter()
# - reduce()
# - recursión
# - librerías de terceros


def flatten(nested_list):
    result = []
    stack = nested_list.copy()

    while stack:
        item = stack.pop()

        if isinstance(item, list):
            # Se agregan en orden inverso para que al extraerlos
            # se conserve el orden original de aparición.
            for element in reversed(item):
                stack.append(element)
        else:
            result.append(item)

    result.reverse()
    return result


# ============================================================
# CASOS DE PRUEBA
# ============================================================

assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, [2, [3, [4, 5]]]]) == [1, 2, 3, 4, 5]

print("All tests passed")