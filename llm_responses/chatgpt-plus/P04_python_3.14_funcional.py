# P04: Balanced Parentheses — Python 3.14
# Paradigma: Estrictamente funcional
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. map(): transformación funcional de caracteres.
# 2. filter(): selección de símbolos permitidos.
# 3. functools.reduce(): acumulación del estado.
# 4. tuple: pila inmutable de símbolos de apertura.
# 5. frozenset: conjunto inmutable de aperturas.
# 6. Funciones puras: sin modificar el estado recibido.
# 7. Anotaciones de tipos modernas de Python.
#
# COMPATIBILIDAD CON PYTHON 3.14:
# - Se aprovecha el sistema moderno de anotaciones.
# - Python 3.14 incorpora evaluación diferida de anotaciones.
# - map(), filter() y reduce() ya existían anteriormente.
# - No se requiere estado mutable ni una característica
#   exclusiva de Python 3.14 para resolver el problema.
#
# Biblioteca estándar únicamente.

from functools import reduce


def is_balanced(s: str) -> bool:

    # Estructuras inmutables.
    openings: frozenset[str] = frozenset("([{")

    # Tupla de pares: (símbolo de cierre, símbolo de apertura).
    pairs: tuple[tuple[str, str], ...] = (
        (")", "("),
        ("]", "["),
        ("}", "{"),
    )

    # Estado: (pila inmutable, validez).
    initial_state: tuple[tuple[str, ...], bool] = ((), True)

    def process(
        state: tuple[tuple[str, ...], bool],
        char: str
    ) -> tuple[tuple[str, ...], bool]:

        stack, valid = state

        # Mantener un estado inválido.
        if not valid:
            return state

        # Apertura: construir una nueva pila inmutable.
        if char in openings:
            return (stack + (char,), True)

        # Buscar la apertura correspondiente al cierre.
        expected = next(
            map(
                lambda pair: pair[1],
                filter(lambda pair: pair[0] == char, pairs)
            )
        )

        # Cierre incorrecto o sin apertura.
        if not stack or stack[-1] != expected:
            return (stack, False)

        # Cierre correcto: devolver una nueva pila.
        return (stack[:-1], True)

    # map() transforma los caracteres mediante identidad.
    mapped = map(lambda char: char, s)

    # filter() selecciona los símbolos admitidos.
    filtered = filter(
        lambda char: char in "()[]{}",
        mapped
    )

    # reduce() procesa la cadena acumulando estados inmutables.
    final_stack, valid = reduce(
        process,
        filtered,
        initial_state
    )

    return valid and not final_stack


# Test 1: Paréntesis correctamente balanceados
assert is_balanced("()") is True

# Test 2: Símbolos cerrados en orden incorrecto
assert is_balanced("([)]") is False

# Test 3: Corchetes anidados correctamente
assert is_balanced("{[]}") is True

print("All tests passed")