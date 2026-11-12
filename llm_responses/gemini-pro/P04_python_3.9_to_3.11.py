# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Structural Pattern Matching (match/case): Introducido en Python 3.10, pero en Python 3.11 su rendimiento fue drásticamente optimizado gracias al "Adaptive Specializing Interpreter" (PEP 659). Permite reemplazar la larga cadena de `if/elif` y las búsquedas en diccionarios por un enrutamiento declarativo a nivel de bytecode, mejorando tanto la legibilidad como la velocidad de ejecución.
# 2. Tipado estricto de colecciones: Uso de `list[str]` nativo para inicializar la pila, proporcionando mejor autocompletado y análisis estático sin requerir importaciones adicionales.

def is_balanced(s: str) -> bool:
    # Definimos la pila con tipado explícito
    stack: list[str] = []
    
    for char in s:
        # Reemplazamos if/elif por Pattern Matching
        match char:
            # Agrupamos todas las aperturas en un solo caso
            case '(' | '[' | '{':
                stack.append(char)
                
            # Evaluamos cierres específicos sin necesidad del diccionario 'pairs'
            case ')':
                if not stack or stack.pop() != '(':
                    return False
            case ']':
                if not stack or stack.pop() != '[':
                    return False
            case '}':
                if not stack or stack.pop() != '{':
                    return False
                    
    # Idiomatismo de PEP 8 para verificar si una lista está vacía
    return not stack

# ==========================================
# CASOS DE PRUEBA
# ==========================================
assert is_balanced("()") is True
assert is_balanced("([)]") is False
assert is_balanced("") is True

print("Todos los tests iterativos refactorizados a Python 3.11 pasaron correctamente.")