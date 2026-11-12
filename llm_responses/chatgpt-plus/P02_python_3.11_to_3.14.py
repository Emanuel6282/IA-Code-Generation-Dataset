# P02: Binary Search — Python 3.14
#
# CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
#
# 1. Anotaciones modernas con list[int]:
#    Se reemplaza typing.List[int] por list[int].
#    Esta sintaxis existe desde Python 3.9 y sigue siendo
#    la opción idiomática en Python 3.14.
#
# 2. Evaluación diferida de anotaciones (PEP 649 y PEP 749):
#    Python 3.14 incorpora este comportamiento de forma
#    predeterminada, sin necesitar importaciones adicionales.
#    Permite gestionar las anotaciones de forma más flexible.
#
# 3. Mejoras internas del intérprete de Python 3.14:
#    Se aprovechan automáticamente las optimizaciones
#    disponibles en la versión utilizada.
#    No es necesario modificar el algoritmo.


def binary_search(arr: list[int], target: int) -> int:
    left = 0
    right = len(arr) - 1

    while left <= right:
        mid = (left + right) // 2

        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1


# TESTS ORIGINALES — CONSERVADOS SIN CAMBIOS

assert binary_search([1, 3, 5, 7, 9], 5) == 2
assert binary_search([1, 3, 5, 7, 9], 4) == -1
assert binary_search([], 1) == -1


# RESUMEN DE CAMBIOS:
#
# 1. Se eliminó "from typing import List".
#    Motivo: la anotación list[int] evita una importación
#    innecesaria y es la sintaxis moderna de Python.
#
# 2. Se reemplazó List[int] por list[int].
#    Motivo: mejora la legibilidad y utiliza tipos genéricos
#    integrados en el lenguaje.
#
# 3. Se aprovecha la evaluación diferida de anotaciones.
#    Motivo: Python 3.14 incorpora esta característica
#    automáticamente. No modifica la búsqueda binaria.
#
# 4. Se conservó el cálculo mid = (left + right) // 2.
#    Motivo: mantener exactamente la lógica original.
#    Python utiliza enteros de precisión arbitraria,
#    por lo que esta suma no presenta desbordamiento.
#
# 5. Se conservaron el bucle while y las variables mutables.
#    Motivo: constituyen una implementación iterativa
#    clara, eficiente e idiomática.
#
# 6. Se conservaron los tres asserts originales.
#    Motivo: verificar los mismos resultados que en
#    la versión de origen.
#
# 7. No se añadieron dependencias externas.
#    Motivo: el algoritmo funciona con las herramientas
#    integradas de Python 3.14.