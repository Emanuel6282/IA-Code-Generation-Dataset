# P01: Word Frequency — Python 3.11
#
# Características utilizadas:
# 1. Tipado nativo con list[str] y dict[str, int]:
#    mejora la legibilidad y elimina importaciones de typing.
# 2. Anotación explícita del diccionario:
#    facilita el análisis estático y el mantenimiento.
# 3. Sintaxis moderna compatible con Python 3.11:
#    mantiene el código simple y eficiente.


def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}

    for word in words:
        if word in result:
            result[word] = result[word] + 1
        else:
            result[word] = 1

    return result


# Casos de prueba originales
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}


# Cambios realizados:
# 1. List[str] -> list[str]:
#    utiliza tipos genéricos nativos de Python.
#
# 2. Dict[str, int] -> dict[str, int]:
#    simplifica las anotaciones de retorno.
#
# 3. Eliminación de "from typing import List, Dict":
#    ya no es necesario importar estos tipos.
#
# 4. Anotación de result como dict[str, int]:
#    documenta explícitamente la estructura del acumulador.
#
# 5. Conservación del bucle for y de la lógica condicional:
#    garantiza el mismo comportamiento que el código original.
#
# Nota: Los tipos genéricos nativos existen desde Python 3.9.
# Python 3.11 no introduce una característica específica que
# mejore significativamente este algoritmo sin modificar su lógica.