# Características de Python 3.11 utilizadas en este refactoring:
# 1. Structural Pattern Matching (PEP 634 - Python 3.10/3.11): Reemplaza la cadena
#    de verificaciones `if char in '([{': ... elif ...` por un bloque `match-case`,
#    lo que mejora la legibilidad y optimiza la ramificación del intérprete (bytecode).
# 2. Tipado estándar simplificado (PEP 585): Uso explícito del tipo nativo `list[str]`
#    para documentar la pila sin recurrir al módulo `typing`.
# 3. Optimización de ejecución y tracebacks precisos (PEP 657 - Python 3.11):
#    Python 3.11 introduce trazas de error enriquecidas y una aceleración general
#    del intérprete (Specializing Adaptive Interpreter) que hace más eficientes
#    las operaciones sobre listas y la evaluación de cadenas.

def is_balanced(s: str) -> bool:
    stack: list[str] = []
    
    for char in s:
        match char:
            case "(" | "[" | "{":
                stack.append(char)
            case ")":
                if not stack or stack.pop() != "(":
                    return False
            case "]":
                if not stack or stack.pop() != "[":
                    return False
            case "}":
                if not stack or stack.pop() != "{":
                    return False
            case _:
                # Ignorar o descartar caracteres no esperados si los hubiera
                pass

    return len(stack) == 0


if __name__ == "__main__":
    assert is_balanced("()") is True
    assert is_balanced("([)]") is False
    assert is_balanced("") is True
    print("All tests passed")

# --- Explicación de los cambios ---
# 1. Uso de `match-case` (Pattern Matching):
#    Sustituye la búsqueda en cadenas (`if char in '([{':`) y las consultas consecutivas
#    al diccionario `pairs[char]`. Al desestructurar cada carácter directamente en su caso,
#    se eliminan las búsquedas en diccionario en tiempo de ejecución (`pairs.get` / `pairs[...]`)
#    y se simplifica el flujo de control.
# 2. Eliminación de la estructura mutable adicional (`pairs`):
#    Se quitó el diccionario `pairs = {')': '(', ...}` ya que la correspondencia entre apertura
#    y cierre queda declarada de forma estática en las cláusulas `case`, ahorrando memoria
#    y tiempo de inicialización por llamada.
# 3. Anotación de tipos nativos:
#    Se añadió la anotación `stack: list[str] = []` utilizando el tipo genérico nativo `list`,
#    proporcionando documentación clara para analizadores estáticos.