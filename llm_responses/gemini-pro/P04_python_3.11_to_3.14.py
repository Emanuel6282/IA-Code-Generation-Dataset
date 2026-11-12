# CARACTERISTICAS DE PYTHON 3.14 UTILIZADAS Y POR QUE:
# 1. Structural Pattern Matching (match/case): Sustituye la lógica de if/elif y el uso del diccionario de mapeo. En las versiones recientes (3.13 y 3.14), el compilador JIT Tier 2 optimiza drásticamente el bytecode generado por `match`, haciéndolo más rápido y eficiente que las búsquedas en diccionarios y condicionales encadenados.
# 2. Declaración de Alias de Tipos (PEP 695): Se utiliza la nueva palabra clave nativa `type` (ej. `type Stack = list[str]`) consolidada como el estándar moderno en 3.14 para definir tipos limpios sin importar el módulo `typing`.

# Definición idiomática y moderna de tipos
type Stack = list[str]

def is_balanced(s: str) -> bool:
    # Tipado explícito usando el alias
    stack: Stack = []
    
    for char in s:
        # Enrutamiento de control de flujo optimizado nativamente
        match char:
            # Agrupación lógica de las aperturas
            case '(' | '[' | '{':
                stack.append(char)
                
            # Validación directa de cada cierre (eliminando el diccionario temporal)
            case ')':
                if not stack or stack.pop() != '(':
                    return False
            case ']':
                if not stack or stack.pop() != '[':
                    return False
            case '}':
                if not stack or stack.pop() != '{':
                    return False
                    
    # Retorno directo y pythónico
    return not stack

# ==========================================
# CASOS DE PRUEBA
# ==========================================

assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("") is True

print("Todos los tests iterativos refactorizados a Python 3.14 pasaron correctamente.")

"""
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.14:

1. Introducción de `type` para Alias (PEP 695):
   Declarar `type Stack = list[str]` en el nivel superior en lugar de simplemente usar variables locales sin tipo o depender de importaciones lentas de `typing`, es la manera oficial y estandarizada en el Python moderno (3.12+) para mejorar la legibilidad y las herramientas de análisis estático.

2. Adopción de Pattern Matching (`match/case`):
   El código base de 3.9 inicializaba un diccionario `pairs` en cada ejecución de la función y comprobaba contenencias (`char in '([{'`). Migrar esto a `match char` no solo hace que el bloque de decisión visualmente declare la "intención" real de la evaluación de caracteres de apertura y cierre, sino que aprovecha el intérprete adaptativo de Python 3.14 para saltos rápidos sin la sobrecarga de instanciar estructuras de datos accesorias (como el dict).

3. Evaluación Booleana Idiomática (PEP 8):
   Se cambió `return len(stack) == 0` por `return not stack`. En Python, es una mala práctica verificar el tamaño de las colecciones estándar para saber si están vacías; estas evalúan inherentemente como falsas en un contexto booleano. `not stack` es idiomáticamente perfecto.

4. Aserciones de Identidad:
   Se actualizaron los tests para utilizar `is True` / `is False` en lugar de `== True` / `== False`. PEP 8 dicta explícitamente que la comparación con booleanos singulares o `None` siempre debe hacerse mediante comparadores de identidad (`is` / `is not`).
"""