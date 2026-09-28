def flatten(nested_list):
    """
    Dada una lista arbitrariamente anidada de enteros,
    retorna una lista plana con todos los enteros en el mismo orden de aparición.
    """
    result = []
    for item in nested_list:
        if isinstance(item, list):
            result.extend(flatten(item))
        else:
            result.append(item)
    return result


# --- Casos de prueba ---

# Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Falló el Caso 1"

# Caso 2: Lista vacía o con sublistas vacías intercaladas
assert flatten([]) == [], "Falló el Caso 2a (Lista vacía)"
assert flatten([[], [[]], [1, []], [[2]], []]) == [1, 2], "Falló el Caso 2b (Estructura con sublistas vacías)"

# Caso 3: Lista totalmente plana y lista profundamente anidada
assert flatten([1, 2, 3, 4]) == [1, 2, 3, 4], "Falló el Caso 3a (Ya plana)"
assert flatten([[[[[10]]]]]) == [10], "Falló el Caso 3b (Profundamente anidada)"

print("¡Todos los casos de prueba pasaron correctamente!")