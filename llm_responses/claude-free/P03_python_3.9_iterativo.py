def flatten(nested_list):
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista
    plana con todos los enteros en el mismo orden de aparicion.

    Implementacion estrictamente iterativa: usa una pila (lista mutable)
    para simular manualmente el descenso por los niveles de anidamiento,
    en lugar de recursion. No se usan map(), filter(), reduce() ni llamadas
    recursivas. Compatible con Python 3.9.

    Estrategia: se recorre la estructura con una pila de "trabajos
    pendientes" que contiene iteradores de cada sublista activa. Al
    encontrar un elemento que es a su vez una lista, se apila su iterador
    y se continua descendiendo; al agotarse un iterador, se desapila y se
    continua con el iterador anterior (el nivel superior).

    Args:
        nested_list: Lista que puede contener enteros y/o otras listas
                      anidadas a cualquier profundidad.

    Returns:
        Una lista plana (list) con todos los enteros en el mismo orden de
        aparicion. Si la lista de entrada esta vacia, retorna una lista
        vacia.
    """
    resultado = []
    pila = [iter(nested_list)]

    while pila:
        iterador_actual = pila[-1]
        avanzo = False

        while True:
            elemento = next(iterador_actual, "__FIN__")
            if elemento == "__FIN__":
                break
            avanzo = True
            if isinstance(elemento, list):
                pila.append(iter(elemento))
                break
            else:
                resultado.append(elemento)

        if not avanzo or elemento == "__FIN__":
            pila.pop()

    return resultado


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, anidamiento de varios niveles
    assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]

    # Caso 2: lista vacia -> lista vacia
    assert flatten([]) == []

    # Caso 3: anidamiento profundo con listas vacias intercaladas
    assert flatten([1, [], [2, [3, [4, [5]], []], 6]]) == [1, 2, 3, 4, 5, 6]

    print("Todos los casos de prueba pasaron correctamente.")