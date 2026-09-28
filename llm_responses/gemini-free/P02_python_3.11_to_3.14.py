# Características de Python 3.14 utilizadas en este refactor:
# 1. Deferred Evaluation of Annotations (PEP 649 / PEP 563 evolutivo):
#    En Python 3.14, las anotaciones de tipo se evalúan de forma diferida de manera nativa
#    sin requerir `from __future__ import annotations`, reduciendo la sobrecarga de tiempo
#    de ejecución al importar el módulo.
# 2. Pattern Matching Expresivo (match/case) con Guards:
#    Estructura la lógica de bifurcación mediante la diferencia matemática (`target - arr[mid]`),
#    haciendo el flujo de control declarativo y más claro que las cadenas de if/elif/else.
# 3. Anotaciones con Generics Nativos de Colecciones (PEP 585 & PEP 604):
#    Utiliza `list[int]` directamente como tipo nativo, prescindiendo del módulo `typing`.


def binary_search(arr: list[int], target: int) -> int:
    left: int = 0
    right: int = len(arr) - 1

    while left <= right:
        mid: int = (left + right) // 2

        # Pattern matching idiomático basado en la diferencia de valores
        match target - arr[mid]:
            case 0:
                return mid
            case diff if diff > 0:
                left = mid + 1
            case _:
                right = mid - 1

    return -1


# --- Casos de prueba conservados (Python 3.14) ---

assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1

print("All tests passed")


# --- Explicación de los cambios ---
# 1. Eliminación de `from typing import List`: Se adopta el uso del tipo nativo `list[int]`.
#    Python 3.14 procesa las anotaciones de tipos de forma diferida (PEP 649), reduciendo el
#    tiempo de inicialización y eliminando la dependencia explícita de `typing.List`.
# 2. Refactorización de `if/elif/else` a `match/case`: Se evalúa la expresión `target - arr[mid]`.
#    Si es 0 (encontrado), retorna la posición; si es positivo (`diff > 0`), incrementa el puntero
#    izquierdo; en caso contrario (patrón comodín `_`), reduce el puntero derecho.
# 3. Tipado explícito de variables internas: Se añadieron anotaciones de tipo (`left: int`,
#    `right: int`, `mid: int`) para reforzar la verificación estática de código.