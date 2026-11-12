# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Sintaxis de Alias de Tipos (PEP 695): Consolidada en las versiones recientes (introducida en 3.12+), usamos la palabra clave `type` para definir `type Stack = list[str]`. Es la forma idiomática y moderna de declarar tipos nativos legibles.
# 2. Structural Pattern Matching (match/case): Utilizado para reemplazar cadenas de `if/elif`. En Python 3.13/3.14, el compilador JIT (Tier 2 Optimizer) optimiza radicalmente el bytecode de estas estructuras, haciendo que las validaciones de tuplas en el `match` iterativo sean extremadamente rápidas.
# 3. Paradigma Iterativo Puro: Gestión manual de la memoria y el control de flujo utilizando una lista mutable como pila (`stack.append` y `stack.pop`) dentro de un bucle `for` clásico.

type Stack = list[str]

def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada y cerrada en orden.
    
    Implementación estrictamente iterativa usando estado mutable 
    y Pattern Matching optimizado.
    """
    # Pila mutable para almacenar los caracteres de apertura
    stack: Stack = []
    
    for char in s:
        # Evaluamos el caracter actual
        match char:
            # Agrupación de caracteres de apertura
            case '(' | '[' | '{':
                stack.append(char)
                
            # Agrupación de caracteres de cierre
            case ')' | ']' | '}':
                # Si la pila está vacía al recibir un cierre, está desbalanceado
                if not stack:
                    return False
                
                # Extraemos el tope de la pila (mutación)
                top = stack.pop()
                
                # Evaluamos si el par coincide exactamente
                match (top, char):
                    case ('(', ')') | ('[', ']') | ('{', '}'):
                        continue
                    case _:
                        # Desajuste de parejas (ej. '[' cerrado con '}')
                        return False
                        
    # Si la iteración termina y la pila está vacía, la cadena está perfectamente balanceada.
    # Las cadenas vacías ("") retornan True directamente.
    return not stack

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: un solo par balanceado
assert is_balanced("()") is True, "Error: '()' debería retornar True"

# 2. Caso del ejemplo: anidamiento incorrecto o cruzado
assert is_balanced("([)]") is False, "Error: '([)]' debería retornar False por cierre desordenado"

# 3. Caso del ejemplo: anidamiento profundo balanceado
assert is_balanced("{[]}") is True, "Error: '{[]}' debería retornar True"

# Prueba extra cubierta por los requerimientos base
assert is_balanced("") is True, "Error: Una cadena vacía debería retornar True"

print("Todos los tests iterativos en Python 3.14 pasaron correctamente.")