# P06: Max Subarray Sum — Python 3.11
# Paradigma: ESTRICTAMENTE ITERATIVO
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Anotaciones de tipos modernas:
#    list[int] e int permiten documentar los tipos
#    sin importar módulos adicionales.
#
# 2. Bucle for:
#    Recorre el arreglo de manera iterativa.
#
# 3. Variables mutables:
#    current_sum y max_sum se actualizan
#    durante cada iteración.
#
# 4. Función max():
#    Permite seleccionar la mejor suma.
#
# 5. Python 3.11:
#    Se beneficia de las optimizaciones de ejecución
#    del intérprete, como la especialización adaptativa.
#
# NOTA:
# list[int] fue introducido en Python 3.9.
# Los bucles, las variables mutables y max()
# también existían antes de Python 3.11.
# No se requieren características exclusivas de 3.11.
#
# Biblioteca estándar únicamente.
# Sin map(), filter(), reduce() ni recursión.


def max_subarray_sum(nums: list[int]) -> int:
    # Un subarreglo debe contener al menos un elemento.
    if not nums:
        raise ValueError("El arreglo no puede estar vacío")

    # Inicializar con el primer elemento.
    current_sum: int = nums[0]
    max_sum: int = nums[0]

    # Recorrer el arreglo de forma iterativa.
    for i in range(1, len(nums)):
        num = nums[i]

        # Elegir entre iniciar un nuevo subarreglo
        # o extender el subarreglo anterior.
        current_sum = max(num, current_sum + num)

        # Actualizar la suma máxima global.
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