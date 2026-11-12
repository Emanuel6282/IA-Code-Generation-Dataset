"""Binary Search - solución iterativa para Python 3.14."""

from string.templatelib import Interpolation, Template


def binary_search(arr: list[int], target: int) -> int:
    """Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está."""
    low = 0
    high = len(arr) - 1
    while low <= high:
        mid = (low + high) // 2
        if arr[mid] == target:
            return mid
        if arr[mid] < target:
            low = mid + 1   # el objetivo solo puede estar en la mitad derecha
        else:
            high = mid - 1  # el objetivo solo puede estar en la mitad izquierda
    return -1


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
    test_cases: list[tuple[str, list[int], int, int]] = [
        # Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        ("objetivo presente", [1, 3, 5, 7, 9], 5, 2),
        # Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        ("objetivo ausente", [1, 3, 5, 7, 9], 4, -1),
        # Caso 3: arreglo vacío
        ("arreglo vacío", [], 1, -1),
    ]

    for name, arr, target, expected in test_cases:
        result = binary_search(arr, target)
        # El mensaje solo se construye si el assert falla; se arma con una t-string.
        assert result == expected, render(
            t"Caso {name!r}: arr={arr}, target={target}, esperado {expected}, obtenido {result}"
        )

    print("Todas las pruebas pasaron.")