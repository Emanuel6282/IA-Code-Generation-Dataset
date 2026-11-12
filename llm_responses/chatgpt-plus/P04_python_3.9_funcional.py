# P04: Balanced Parentheses — Python 3.9
# Paradigma: Estrictamente funcional
#
# Características utilizadas:
# 1. tuple[str, ...] (PEP 585, Python 3.9).
# 2. map() para transformar los caracteres.
# 3. filter() para seleccionar los símbolos válidos.
# 4. functools.reduce() para procesar la cadena.
# 5. Tuplas inmutables como pila funcional.
# 6. Funciones puras y expresiones condicionales.
#
# Sin for, while, append(), pop() ni estado mutable.
# Biblioteca estándar únicamente.

from functools import reduce


def is_balanced(s: str) -> bool:

    # Cada cierre se relaciona con su apertura.
    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{"
    }

    # Estado inmutable:
    # (pila_de_aperturas, es_valido)
    initial_state: tuple[tuple[str, ...], bool] = ((), True)

    def process(
        state: tuple[tuple[str, ...], bool],
        char: str
    ) -> tuple[tuple[str, ...], bool]:

        stack, valid = state

        # Si ya existe un error, conservar el estado inválido.
        if not valid:
            return state

        # Símbolo de apertura: crear una nueva tupla.
        if char in "([{":
            return (stack + (char,), True)

        # Símbolo de cierre: comprobar correspondencia.
        if not stack or stack[-1] != pairs[char]:
            return (stack, False)

        # Cierre correcto: crear una nueva pila sin el último elemento.
        return (stack[:-1], True)

    # map() transforma los caracteres mediante una función pura.
    mapped = map(lambda char: char, s)

    # filter() selecciona los símbolos admitidos.
    filtered = filter(lambda char: char in "()[]{}", mapped)

    # reduce() procesa secuencialmente los caracteres,
    # propagando un estado completamente inmutable.
    final_state = reduce(process, filtered, initial_state)

    final_stack, valid = final_state

    # Balanceado si no hubo errores y no quedan aperturas.
    return valid and len(final_stack) == 0


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") is True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") is False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") is True

print("All tests passed")