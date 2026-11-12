# P06: Max Subarray Sum (Kadane) — Python 3.11
# Migración desde Python 3.9
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[int] reemplaza typing.List[int].
#    Esta sintaxis fue introducida en Python 3.9
#    y es idiomática en Python 3.11.
#
# 2. Función max():
#    Simplifica las comparaciones condicionales
#    del algoritmo original.
#
# 3. Especialización adaptativa (Python 3.11):
#    El intérprete CPython 3.11 puede optimizar
#    automáticamente operaciones frecuentes
#    durante la ejecución.
#
# 4. Bucle for y variables locales:
#    Conservan el algoritmo iterativo original.
#
# NOTA:
# Python 3.11 no requiere una nueva construcción
# sintáctica para mejorar este algoritmo.
# Las optimizaciones del intérprete se aplican
# automáticamente, sin modificar su lógica.
#
# Biblioteca estándar únicamente.


def max_subarray_sum(arr: list[int]) -> int:
    # Mantener la inicialización original.
    current_max: int = arr[0]
    global_max: int = arr[0]

    # Recorrer los elementos restantes.
    for i in range(1, len(arr)):
        # Elegir entre extender el subarreglo actual
        # o comenzar uno nuevo.
        current_max = max(arr[i], current_max + arr[i])

        # Actualizar la máxima suma global.
        global_max = max(global_max, current_max)

    return global_max


# TEST 1: Ejemplo original.
assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6

# TEST 2: Arreglo con un único elemento.
assert max_subarray_sum([1]) == 1

# TEST 3: Todos los elementos negativos.
assert max_subarray_sum([-2, -1]) == -1


# CAMBIOS REALIZADOS EN LA MIGRACIÓN PYTHON 3.9 -> 3.11
#
# 1. MODERNIZACIÓN DE LAS ANOTACIONES
#
#    Antes:
#      from typing import List
#      def max_subarray_sum(arr: List[int]) -> int:
#
#    Ahora:
#      def max_subarray_sum(arr: list[int]) -> int:
#
#    Motivo:
#    Se utiliza la sintaxis genérica integrada,
#    disponible desde Python 3.9 y recomendada
#    para código moderno de Python.
#
# 2. SIMPLIFICACIÓN DE LAS CONDICIONES
#
#    Antes:
#      if current_max + arr[i] > arr[i]:
#          current_max = current_max + arr[i]
#      else:
#          current_max = arr[i]
#
#    Ahora:
#      current_max = max(arr[i], current_max + arr[i])
#
#    Motivo:
#    Expresa directamente la decisión de Kadane
#    mediante una sola instrucción.
#
# 3. SIMPLIFICACIÓN DEL MÁXIMO GLOBAL
#
#    Antes:
#      if current_max > global_max:
#          global_max = current_max
#
#    Ahora:
#      global_max = max(global_max, current_max)
#
#    Motivo:
#    Reduce código repetitivo y mejora la legibilidad.
#
# 4. CONSERVACIÓN DEL ALGORITMO
#
#    Se mantienen:
#      - Inicialización con el primer elemento.
#      - Recorrido desde el índice 1.
#      - Elección entre extender o reiniciar.
#      - Actualización del máximo global.
#
#    Motivo:
#    Garantizar el mismo comportamiento para
#    arreglos no vacíos.
#
# 5. CONSERVACIÓN DE LOS TESTS
#
#    TC01: 6
#    TC02: 1
#    TC03: -1
#
#    Se conservan exactamente las tres aserciones.
#
# 6. COMPATIBILIDAD CON PYTHON 3.11
#
#    Se aprovechan las optimizaciones automáticas
#    del intérprete CPython 3.11.
#    No se necesitan dependencias externas.
#
# COMPLEJIDAD:
#    Tiempo: O(n).
#    Espacio auxiliar: O(1).