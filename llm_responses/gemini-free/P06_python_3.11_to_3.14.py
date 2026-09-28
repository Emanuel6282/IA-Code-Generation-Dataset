# CARACTERÍSTICAS UTILIZADAS DE PYTHON 3.14 (Y EVOLUCIÓN DESDE 3.12+):
# 1. Alias de Tipos Declarativos (Type Alias Statements - PEP 695): Uso de la sintaxis nativa `type NumberList = list[int]`
#    para definir firmas de tipo limpias y reutilizables sin recurrir al módulo `typing`.
# 2. Anotación de tipos nativa (PEP 585): Uso directo de `list[int]` en lugar de `typing.List`.
# 3. Anotaciones Diferidas Incondicionales (PEP 649 / PEP 749 en Python 3.14): En Python 3.14, las anotaciones de tipos
#    no se evalúan en el momento de la definición de la función, sino bajo demanda (deferred evaluation), lo que reduce el costo de inicio (startup time) y optimiza el consumo de memoria.
# 4. Enriquecimiento de Excepciones (`add_note()`): Para adjuntar notas de diagnóstico explícitas en la validación de entrada.
# 5. Iteración directa y `max()` simplificado: Aprovecha el Specialized Adaptive Interpreter de CPython 3.14 para ejecutar
#    instrucciones bytecode optimizadas en bucles con tipos primarios homogéneos.

type NumberList = list[int]

def max_subarray_sum(arr: NumberList) -> int:
    """
    Calcula la suma máxima de un subarreglo contiguo no vacío usando el Algoritmo de Kadane.
    Migrado e idiomático para Python 3.14.
    """
    if not arr:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Python 3.14: La función requiere al menos un elemento para retornar un subarreglo no vacío.")
        raise err

    current_max: int = arr[0]
    global_max: int = arr[0]

    # Iteración sobre el slice a partir del índice 1
    for x in arr[1:]:
        current_max = max(x, current_max + x)
        global_max = max(global_max, current_max)

    return global_max


# ==========================================
# CASOS DE PRUEBA (ASSERTS ORIGINALES)
# ==========================================

if __name__ == "__main__":
    assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
    assert max_subarray_sum([1]) == 1
    assert max_subarray_sum([-2, -1]) == -1

    print("¡Todos los casos de prueba pasaron correctamente en Python 3.14!")


# EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN PYTHON 3.14:
#
# 1. Uso de la sintaxis de alias de tipos `type` (PEP 695):
#    A partir de Python 3.12 y completamente asentado en 3.14, definir alias mediante `type Name = ...` es la
#    forma idiomática y estándar, reemplazando la sintaxis obsoleta `TypeAlias` o `typing.List`.
#
# 2. Eliminación de `from typing import List`:
#    Las colecciones nativas estándar (`list[int]`, `dict[str, int]`, etc.) reemplazan completamente a los genéricos de `typing`.
#
# 3. Manejo de anotaciones en el motor de Python 3.14 (PEP 649):
#    En Python 3.14, las anotaciones de tipo se diferizan nativamente (descriptor-based deferred evaluation). Esto significa
#    que el runtime no gasta ciclos de CPU resolviendo anotaciones complejas durante la carga del módulo, mejorando la velocidad global de ejecución.
#
# 4. Sustitución de `range(1, len(arr))` e `if/else` explícitos por `arr[1:]` y `max()`:
#    Simplifica la lectura del código al eliminar indexaciones manuales (`arr[i]`). Además, las llamadas a `max()` dentro del bucle
#    son optimizadas en C por el intérprete adaptativo de CPython.
#
# 5. Incorporación de `add_note()` para diagnóstico:
#    Permite proporcionar contexto claro sin alterar el tipo de excepción lanzada (`ValueError`), alineándose con el estándar moderno de manejo de errores de CPython.