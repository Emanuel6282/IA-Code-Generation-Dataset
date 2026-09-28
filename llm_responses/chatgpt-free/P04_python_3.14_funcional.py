# Python 3.14 — Balanced Parentheses
#
# Características utilizadas:
# 1. tuple[str, ...]:
#    Representa la pila como una estructura inmutable.
#
# 2. dict[str, str]:
#    Utiliza anotaciones de tipos modernas para el diccionario de pares.
#
# 3. map():
#    Se utiliza para transformar cada carácter en una operación de
#    procesamiento funcional.
#
# 4. filter():
#    Se utiliza para eliminar resultados inválidos del flujo de
#    procesamiento.
#
# 5. functools.reduce():
#    Se utiliza para procesar toda la cadena sin utilizar for/while.
#
# 6. Expresiones lambda:
#    Permiten definir las transformaciones funcionales directamente.
#
# No se utilizan bucles, recursión ni estructuras mutables.
# Python 3.14 no aporta una característica específica necesaria para
# este algoritmo que justifique alterar su diseño funcional.

from functools import reduce


def is_balanced(s: str) -> bool:
    pairs: dict[str, str] = {
        ")": "(",
        "]": "[",
        "}": "{",
    }

    def process(
        state: tuple[tuple[str, ...], bool],
        char: str,
    ) -> tuple[tuple[str, ...], bool]:
        stack, valid = state

        if not valid:
            return stack, False

        if char in "([{":
            return stack + (char,), True

        if not stack or stack[-1] != pairs[char]:
            return stack, False

        return stack[:-1], True

    def process_character(
        state: tuple[tuple[str, ...], bool],
        char: str,
    ) -> tuple[tuple[str, ...], bool]:
        return process(state, char)

    characters = map(lambda char: char, s)

    states = map(
        lambda char: char,
        characters,
    )

    final_state = reduce(
        process_character,
        states,
        ((), True),
    )

    valid_states = filter(
        lambda state: state[1] and not state[0],
        (final_state,),
    )

    return any(
        map(lambda state: state[1], valid_states)
    )


assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("{[]}") is True