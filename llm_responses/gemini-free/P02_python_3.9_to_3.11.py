# Características de Python 3.11 utilizadas en este refactor:
# 1. Structural Pattern Matching (match/case):
#    Introducido formalmente en Python 3.10 y optimizado en 3.11. Reemplaza la cadena
#    if/elif/else por una coincidencia de patrones con 'guards' (condiciones),
#    haciendo el flujo de control más expresivo y declarativo.
# 2. Generics built-in / Anotaciones de tipos nativas (PEP 585):
#    Permite usar `list[int]` directamente sin necesidad de importar `List` desde `typing`,
#    simplificando los imports y reduciendo el overhead de carga del módulo typing.


def binary_search(arr: list[int], target: int) -> int:
    left: int = 0
    right: int = len(arr) - 1

    while left <= right:
        mid: int = (left + right) // 2

        # Flujo de control idiomático con Structural Pattern Matching
        match arr[mid] == target:
            case True:
                return mid
            case False if arr[mid] < target:
                left = mid + 1
            case False:
                right = mid - 1

    return -1


# --- Casos de prueba conservados (Python 3.11) ---

assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1

print("All tests passed")


# --- Explicación de los cambios ---
# 1. Remoción del import `from typing import List`: Se adopta la sintaxis de tipos estándar
#    `list[int]` introducida en el PEP 585, la cual elimina la necesidad de depender del módulo
#     typing para colecciones estándar.
# 2. Reemplazo de bloques `if/elif/else` por `match-case`: Permite explicitar la intención del
#    algoritmo mediante la coincidencia de estados (encontrado vs. navegación por límites), lo que
#    mejora la legibilidad general del código.
# 3. Anotaciones de tipo explícitas en variables locales: Se especifican tipos explícitos
#    (`left: int`, `right: int`, `mid: int`) para aprovechar el chequeo estático de tipos
#    y mejorar la autodocumentación dentro del IDE.