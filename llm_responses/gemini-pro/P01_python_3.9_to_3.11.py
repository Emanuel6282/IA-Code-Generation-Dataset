# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Tipado mediante colecciones nativas (PEP 585): Se usan `list[str]` y `dict[str, int]` nativos. 
#    Aunque se introdujo en 3.9, en 3.11 es el estándar idiomático absoluto, permitiendo eliminar
#    las importaciones del módulo `typing` para hacer el código más limpio y rápido al cargar.
# 2. Faster CPython (PEP 659 - Specializing Adaptive Interpreter): En Python 3.11, la máquina 
#    virtual optimiza dinámicamente el acceso a diccionarios y la ejecución de bucles a nivel
#    de bytecode. Por esto, mantenemos el enfoque iterativo básico porque en 3.11 es 
#    extremadamente eficiente sin necesidad de trucos adicionales.

def word_frequency(words: list[str]) -> dict[str, int]:
    result: dict[str, int] = {}
    for word in words:
        # Simplificación idiomática del if/else
        result[word] = result.get(word, 0) + 1
    return result

# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}
print("Todos los tests pasaron correctamente.")

# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.11:
# 
# 1. Limpieza de Imports: Se eliminó `from typing import List, Dict`. En el ecosistema 
#    moderno de Python 3.11, usar las clases incorporadas (minúsculas `list`, `dict`) 
#    para el type-hinting no solo es idiomático, sino que evita cargar módulos extra en memoria.
#
# 2. Anotación de variables locales: Se agregó `result: dict[str, int] = {}`. Las herramientas 
#    modernas de análisis estático (como mypy de Python 3.11+) aprovechan mejor las 
#    anotaciones explícitas en las variables inicializadas.
#
# 3. Refactorización a `.get()`: El bloque `if word in result: ... else: ...` se reemplazó 
#    por `result[word] = result.get(word, 0) + 1`. Esto mejora significativamente la legibilidad 
#    (un solo paso en lugar de cuatro líneas) y aprovecha las optimizaciones internas de CPython 
#    en 3.11 para las llamadas a métodos integrados.