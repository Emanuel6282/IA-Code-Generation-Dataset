def flatten(nested):
    """Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.

    Usa una pila de iteradores en lugar de recursión, así que funciona con cualquier
    nivel de anidamiento sin alcanzar el límite de recursión de Python.
    """
    result = []
    stack = [iter(nested)]
    while stack:
        for item in stack[-1]:
            if isinstance(item, list):
                stack.append(iter(item))  # baja un nivel; se retoma este al terminar
                break
            result.append(item)
        else:
            stack.pop()  # el nivel actual se agotó: vuelve al anterior
    return result


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado
    assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

    # Caso 2: lista vacía y sublistas vacías
    assert flatten([]) == []
    assert flatten([[], [[]], [[], [[]]]]) == []

    # Caso 3: anidamiento profundo (más allá del límite de recursión por defecto)
    deep = [7]
    for _ in range(5000):
        deep = [deep]
    assert flatten([0, deep, 8]) == [0, 7, 8]

    print("Todas las pruebas pasaron.")