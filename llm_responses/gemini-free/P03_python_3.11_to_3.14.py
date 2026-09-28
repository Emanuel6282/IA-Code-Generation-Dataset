# Características de Python 3.14 utilizadas en este refactor:
# 1. Evaluación diferida de anotaciones de tipo por defecto (Deferred Evaluation of Annotations - PEP 649 / Python 3.14):
#    Las anotaciones de tipo ya no se evalúan en tiempo de definición de la función, reduciendo la carga de importación
#    y permitiendo referencias hacia adelante y firmas complejas sin impacto en el tiempo de ejecución.
# 2. Tipado nativo con operador de unión (`list[int | list]`):
#    Elimina la necesidad de importar `List` o `Any` del módulo `typing` (PEP 585 / PEP 604), haciendo la firma concisa.
# 3. Structural Pattern Matching (`match / case`):
#    Sustituye la verificación con `isinstance()` por una inspección declarativa y idiomática de tipos.
# 4. Optimizaciones continuas del intérprete especializado (PEP 659 / Tail-Call & Bytecode Specialization):
#    Aprovecha las mejoras de rendimiento de la máquina virtual en Python 3.14 para la ejecución eficiente de bucles y patrones.

def flatten(nested: list[int | list]) -> list[int]:
    """
    Desanida iterativamente cualquier lista de enteros usando una pila (stack)
    y Structural Pattern Matching en Python 3.14.
    """
    result: list[int] = []
    stack: list[int | list] = [nested]

    while stack:
        item = stack.pop()
        match item:
            case list() as sublist:
                for element in reversed(sublist):
                    stack.append(element)
            case int() as val:
                result.append(val)

    return result


# --- Casos de prueba conservados (Python 3.14) ---
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, 2, 3]) == [1, 2, 3]

print("All tests passed")


# --- Explicación de los cambios ---
# 1. PEP 649 (Deferred Evaluation of Annotations in Python 3.14):
#    Se simplifica el sistema de tipos haciendo uso de la nueva infraestructura de anotaciones de Python 3.14,
#    donde los tipos no se evalúan hasta que se consultan explícitamente vía `annotationlib`, optimizando el arranque.
# 2. Eliminación de importaciones de `typing`:
#    Se reemplazó `typing.List` y `typing.Any` por tipos nativos `list[...]` y uniones explícitas con `|` (`int | list`),
#    lo que elimina dependencias obsoletas y mejora la claridad del contrato de tipos.
# 3. Adopción de Structural Pattern Matching (`match / case`):
#    Se sustituyó `if isinstance(item, list)` por la sintaxis moderna de `match / case`, permitiendo una discriminación de tipos
#    más declarativa, segura e idiómatica en Python moderno.
# 4. Tipado seguro para enteros (`case int() as val`):
#    En lugar de un `else` genérico, se garantiza explícitamente la extracción de enteros, alineándose con las mejores prácticas de código limpio.