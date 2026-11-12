# P04: Balanced Parentheses — Python 3.11
# Paradigma: Estrictamente funcional
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. map() para transformar los caracteres.
# 2. filter() para seleccionar los símbolos permitidos.
# 3. functools.reduce() para acumular el estado.
# 4. Tuplas inmutables como pila funcional.
# 5. Funciones puras sin efectos secundarios.
# 6. Anotaciones modernas: tuple[str, ...] y dict[str, str].
#
# COMPATIBILIDAD CON PYTHON 3.11:
# - Se utiliza la sintaxis moderna de anotaciones de tipos.
# - Las anotaciones genéricas existen desde Python 3.9.
# - Python 3.11 incorpora optimizaciones del intérprete.
# - No se requieren características exclusivas de 3.11.
#
# Sin for, while, append(), pop() ni estado mutable.
# Biblioteca estándar únicamente.

from functools import reduce


def is_balanced(s: str) -> bool:

    # Diccionario de correspondencias.
    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{"
    }

    # Estado inicial inmutable:
    # (pila de aperturas, validez)
    initial_state: tuple[tuple[str, ...], bool] = ((), True)

    def process(
        state: tuple[tuple[str, ...], bool],
        char: str
    ) -> tuple[tuple[str, ...], bool]:

        stack, valid = state

        # Conservar un estado inválido.
        if not valid:
            return state

        # Apertura: crear una nueva tupla.
        if char in "([{":
            return (stack + (char,), True)

        # Cierre incorrecto o sin apertura previa.
        if not stack or stack[-1] != pairs[char]:
            return (stack, False)

        # Cierre correcto: devolver una nueva pila.
        return (stack[:-1], True)

    # map() transforma los caracteres.
    mapped = map(lambda char: char, s)

    # filter() selecciona los símbolos admitidos.
    filtered = filter(
        lambda char: char in "()[]{}",
        mapped
    )

    # reduce() procesa los caracteres y acumula
    # un nuevo estado inmutable en cada paso.
    final_state = reduce(
        process,
        filtered,
        initial_state
    )

    final_stack, valid = final_state

    return valid and len(final_stack) == 0


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") is True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") is False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") is True

print("All tests passed")