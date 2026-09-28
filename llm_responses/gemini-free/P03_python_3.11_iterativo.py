# Python 3.11 - Paradigma Iterativo Estricto
# Uso de anotaciones de tipo nativas con sintaxis de unión (|) y procesamiento basado en una pila (stack)

def flatten(nested_list: list[int | list]) -> list[int]:
    """
    Dada una lista arbitrariamente anidada de enteros, retorna una lista plana
    con todos los enteros en el mismo orden de aparición utilizando un enfoque
    estrictamente iterativo basado en una pila (stack) sin usar recursión.
    """
    result: list[int] = []
    # Pila explícita para simular la pila de llamadas de forma iterativa
    stack: list[int | list] = [nested_list]

    while stack:
        current = stack.pop()
        # En Python 3.11 pattern matching estructural permite validar tipos e iterables de forma concisa
        match current:
            case list() as sublist:
                # Insertamos los elementos en orden inverso a la pila para procesar
                # la lista de izquierda a derecha al hacer pop()
                for i in range(len(sublist) - 1, -1, -1):
                    stack.append(sublist[i])
            case int() as val:
                result.append(val)

    return result


# --- Casos de prueba (Python 3.11) ---

# Caso 1: Ejemplo provisto con varios niveles de anidamiento
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6], "Falló el Caso 1"

# Caso 2: Listas vacías y sublistas vacías intercaladas
assert flatten([]) == [], "Falló el Caso 2a (Lista vacía)"
assert flatten([[], [[]], [1, []], [[2]], []]) == [1, 2], "Falló el Caso 2b (Sublistas vacías)"

# Caso 3: Lista totalmente plana y lista profundamente anidada
assert flatten([10, 20, 30]) == [10, 20, 30], "Falló el Caso 3a (Lista plana)"
assert flatten([[[[[99]]]]]) == [99], "Falló el Caso 3b (Profundamente anidada)"

print("¡Todos los casos de prueba pasaron correctamente!")