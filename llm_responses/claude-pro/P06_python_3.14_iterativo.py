"""Max Subarray Sum - solución iterativa para Python 3.14."""

from collections.abc import Sequence
from string.templatelib import Interpolation, Template


def max_subarray_sum(nums: Sequence[int]) -> int:
    """Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.

    Usa el algoritmo de Kadane: O(n) en tiempo y O(1) en memoria. Acepta cualquier
    secuencia indexable (lista, tupla, range...).

    Raises:
        ValueError: si `nums` está vacío (no existe ningún subarreglo no vacío).
    """
    if not nums:
        raise ValueError("el arreglo debe tener al menos un elemento")

    # Se parte del primer elemento (no de 0) porque el subarreglo no puede ser vacío;
    # así, si todos son negativos, el resultado es el menos negativo.
    best = nums[0]
    current = nums[0]
    for i in range(1, len(nums)):          # por índice: sin copiar la secuencia
        x = nums[i]
        # La mejor suma que termina en x: o extiende el subarreglo anterior,
        # o empieza de nuevo en x si lo acumulado hasta ahora resta.
        if current + x > x:
            current = current + x
        else:
            current = x
        if current > best:
            best = current
    return best


def render(template: Template) -> str:
    """Convierte una t-string (PEP 750, nuevo en Python 3.14) en texto, de forma iterativa.

    A diferencia de una f-string, una t-string no produce un str directamente:
    produce un Template con sus partes literales e interpolaciones por separado.
    """
    parts: list[str] = []
    for item in template:
        if isinstance(item, Interpolation):
            value = item.value
            match item.conversion:
                case "r":
                    value = repr(value)
                case "s":
                    value = str(value)
                case "a":
                    value = ascii(value)
            parts.append(format(value, item.format_spec))
        else:
            parts.append(item)
    return "".join(parts)


if __name__ == "__main__":
    test_cases: list[tuple[str, list[int], int]] = [
        # Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
        ("ejemplo del enunciado", [-2, 1, -3, 4, -1, 2, 1, -5, 4], 6),
        # Caso 2: todos negativos -> el menos negativo
        ("todos negativos", [-3, -1, -2], -1),
        ("un solo elemento negativo", [-5], -5),
        # Caso 3: todos positivos -> el arreglo completo
        ("todos positivos", [1, 2, 3], 6),
    ]

    for name, nums, expected in test_cases:
        result = max_subarray_sum(nums)
        # El mensaje solo se construye si el assert falla; se arma con una t-string.
        assert result == expected, render(t"Caso {name!r} {nums}: esperado {expected}, obtenido {result}")

    # Arreglo vacío -> ValueError
    try:
        max_subarray_sum([])
        assert False, "un arreglo vacío debería lanzar ValueError"
    except ValueError:
        pass

    print("Todas las pruebas pasaron.")