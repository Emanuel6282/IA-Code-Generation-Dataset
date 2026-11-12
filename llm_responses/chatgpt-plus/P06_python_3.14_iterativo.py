# P06: Max Subarray Sum — Python 3.14
# Paradigma: ESTRICTAMENTE ITERATIVO
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[int] e int permiten documentar los tipos
#    de entrada y salida.
#
# 2. Bucle for:
#    Recorre el arreglo de manera iterativa.
#
# 3. Variables mutables:
#    current_sum y max_sum se actualizan
#    durante cada iteración.
#
# 4. Función max():
#    Selecciona la mejor suma en cada paso.
#
# 5. Python 3.14:
#    Incluye evaluación diferida de anotaciones
#    (PEP 649 y PEP 749), sin necesidad de
#    modificar el algoritmo.
#
# NOTA:
# list[int] fue introducido en Python 3.9.
# Los bucles y max() existían antes de Python 3.14.
# No se necesitan características exclusivas de 3.14
# para resolver eficientemente este problema.
#
# Biblioteca estándar únicamente.
# Sin map(), filter(), reduce() ni recursión.


def max_subarray_sum(nums: list[int]) -> int:
    # No se permite un subarreglo vacío.
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Inicializar con el primer elemento.
    current_sum: int = nums[0]
    max_sum: int = nums[0]

    # Recorrido estrictamente iterativo.
    for i in range(1, len(nums)):
        num = nums[i]

        # Elegir entre iniciar un nuevo subarreglo
        # o extender el subarreglo anterior.
        current_sum = max(num, current_sum + num)

        # Actualizar la mejor suma encontrada.
        max_sum = max(max_sum, current_sum)

    return max_sum


# TEST 1: Ejemplo del enunciado.
assert max_subarray_sum(
    [-2, 1, -3, 4, -1, 2, 1, -5, 4]
) == 6

# TEST 2: Todos los elementos negativos.
assert max_subarray_sum(
    [-8, -3, -6, -2, -5, -4]
) == -2

# TEST 3: Todos los elementos positivos.
assert max_subarray_sum(
    [1, 2, 3, 4, 5]
) == 15

print("All tests passed")