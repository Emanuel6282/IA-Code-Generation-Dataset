# P06: Max Subarray Sum — Python 3.9
# Paradigma: ESTRICTAMENTE ITERATIVO
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones list[int] (Python 3.9, PEP 585):
#    Permiten indicar listas de enteros sin importar
#    List desde el módulo typing.
#
# 2. Bucle for:
#    Recorre los elementos mediante iteración explícita.
#
# 3. Variables mutables:
#    current_sum y max_sum se actualizan durante el recorrido.
#
# 4. Función max():
#    Selecciona la mayor suma en cada iteración.
#
# NOTA:
# Los bucles, las variables mutables y max() existían
# antes de Python 3.9. La sintaxis list[int] sí fue
# introducida en Python 3.9.
#
# Biblioteca estándar únicamente.
# Sin map(), filter(), reduce() ni recursión.


def max_subarray_sum(nums: list[int]) -> int:
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Inicializar con el primer elemento.
    # Esto garantiza que el subarreglo no sea vacío.
    current_sum = nums[0]
    max_sum = nums[0]

    # Recorrido estrictamente iterativo.
    for i in range(1, len(nums)):
        num = nums[i]

        # Elegir entre comenzar un nuevo subarreglo
        # o continuar el subarreglo anterior.
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