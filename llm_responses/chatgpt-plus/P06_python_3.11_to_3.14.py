# P06: Max Subarray Sum (Kadane) — Python 3.14
# Migración desde Python 3.11
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[int] reemplaza typing.List[int].
#    Esta sintaxis está disponible desde Python 3.9
#    y es idiomática en Python 3.14.
#
# 2. Evaluación diferida de anotaciones (Python 3.14):
#    PEP 649 y PEP 749 permiten diferir la evaluación
#    de las anotaciones hasta que se soliciten.
#    No es necesario modificar el algoritmo para ello.
#
# 3. Función max():
#    Simplifica las comparaciones del algoritmo original.
#
# 4. Bucle for y variables locales:
#    Conservan el comportamiento iterativo de Kadane.
#
# NOTA:
# No se requieren características sintácticas exclusivas
# de Python 3.14 para este problema. Se utilizan las
# características estables que mejoran la legibilidad.
#
# Biblioteca estándar únicamente.


def max_subarray_sum(arr: list[int]) -> int:
    # Inicializar con el primer elemento.
    # Garantiza que el subarreglo no sea vacío.
    current_max = arr[0]
    global_max = arr[0]

    # Recorrer los elementos restantes.
    for i in range(1, len(arr)):
        # Elegir entre continuar el subarreglo
        # anterior o comenzar uno nuevo.
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


# ============================================================
# CAMBIOS REALIZADOS EN LA MIGRACIÓN PYTHON 3.11 -> 3.14
# ============================================================
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
#    disponible desde Python 3.9.
#    Evita una importación innecesaria.
#
# 2. EVALUACIÓN DIFERIDA DE ANOTACIONES
#
#    Python 3.14 incorpora el nuevo modelo de
#    evaluación de anotaciones (PEP 649 y PEP 749).
#
#    Motivo:
#    Permite diferir su evaluación y puede reducir
#    el trabajo realizado al definir funciones.
#    En este ejemplo, el impacto es mínimo.
#
# 3. SIMPLIFICACIÓN DE LAS CONDICIONES
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
#    con una sola instrucción.
#
# 4. SIMPLIFICACIÓN DEL MÁXIMO GLOBAL
#
#    Antes:
#      if current_max > global_max:
#          global_max = current_max
#
#    Ahora:
#      global_max = max(global_max, current_max)
#
#    Motivo:
#    Mejora la legibilidad y reduce el código.
#
# 5. CONSERVACIÓN DEL ALGORITMO
#
#    Se mantienen:
#      - Inicialización con arr[0].
#      - Recorrido desde el índice 1.
#      - Selección de la mejor suma local.
#      - Actualización de la mejor suma global.
#
#    Motivo:
#    Preservar los resultados del algoritmo original.
#
# 6. CONSERVACIÓN DEL COMPORTAMIENTO
#
#    Para arreglos no vacíos:
#      - Se calcula la máxima suma contigua.
#      - Si todos los números son negativos,
#        se devuelve el menos negativo.
#      - No se permite un subarreglo vacío.
#
#    Para una lista vacía:
#      - Se conserva el IndexError original,
#        producido al acceder a arr[0].
#
# 7. CONSERVACIÓN DE LOS TESTS
#
#    TC01: 6
#    TC02: 1
#    TC03: -1
#
#    Se mantienen exactamente las tres aserciones.
#
# 8. COMPATIBILIDAD CON PYTHON 3.14
#
#    Se utiliza sintaxis estable y únicamente
#    funcionalidades de la biblioteca estándar.
#
# COMPLEJIDAD:
#    Tiempo: O(n).
#    Espacio auxiliar: O(1).