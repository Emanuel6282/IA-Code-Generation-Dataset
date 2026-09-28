from __future__ import annotations


def flatten(nested_list: list[int | list]) -> list[int]:
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista plana
    con todos los enteros en el mismo orden de aparición utilizando un enfoque
    estrictamente iterativo mediante una pila (stack).
    """
    result: list[int] = []
    # Pila de trabajo para procesar elementos de forma iterativa sin recursión
    stack: list[int | list] = [nested_list]

    while stack:
        current = stack.pop()
        if isinstance(current, list):
            # Insertamos los elementos en orden inverso para procesar
            # la estructura de izquierda a derecha con pop()
            for i in range(len(current) - 1, -1, -1):
                stack.append(current[i])
        else:
            result.append(current)

    return result


# --- Casos de prueba (Python 3.9) ---

# Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Falló el Caso 1"

# Caso 2: Estratificación de sublistas vacías e integración
assert flatten([]) == [], "Falló el Caso 2a (Lista vacía)"
assert flatten([[], [[]], [1, []], [[2]], []]) == [1, 2], "Falló el Caso 2b (Sublistas vacías)"

# Caso 3: Lista totalmente plana y estructura profundamente anidada
assert flatten([10, 20, 30]) == [10, 20, 30], "Falló el Caso 3a (Lista ya plana)"
assert flatten([[[[[99]]]]]) == [99], "Falló el Caso 3b (Profundamente anidada)"

print("¡Todos los casos de prueba pasaron correctamente!")