# Python 3.14 - Paradigma Iterativo Estricto
# Utiliza anotaciones de tipo nativas modernas, sintaxis de unión (|),
# pattern matching (match/case) y procesamiento puramente iterativo mediante una pila (stack).

def flatten(nested_list: list[int | list]) -> list[int]:
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista plana
    con todos los enteros en el mismo orden de aparición utilizando un enfoque
    estrictamente iterativo basado en una pila (stack) sin recursión.
    """
    result: list[int] = []
    # Pila de trabajo explícita para procesar elementos en orden sin recursión
    stack: list[int | list] = [nested_list]

    while stack:
        current = stack.pop()
        match current:
            case list() as sublist:
                # Insertamos los elementos en orden inverso en la pila para
                # extraerlos de izquierda a derecha con pop()
                for i in range(len(sublist) - 1, -1, -1):
                    stack.append(sublist[i])
            case int() as val:
                result.append(val)

    return result


# --- Casos de prueba (Python 3.14) ---

# Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Falló el Caso 1"

# Caso 2: Listas vacías y estructuras con sublistas vacías intercaladas
assert flatten([]) == [], "Falló el Caso 2a (Lista vacía)"
assert flatten([[], [[]], [1, []], [[2]], []]) == [1, 2], "Falló el Caso 2b (Sublistas vacías)"

# Caso 3: Lista totalmente plana y estructura profundamente anidada
assert flatten([10, 20, 30]) == [10, 20, 30], "Falló el Caso 3a (Lista ya plana)"
assert flatten([[[[[99]]]]]) == [99], "Falló el Caso 3b (Profundamente anidada)"

print("¡Todos los casos de prueba pasaron correctamente!")