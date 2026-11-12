# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA ITERATIVO UTILIZADAS:
# 1. Structural Pattern Matching (match/case): Introducido en 3.10 y altamente optimizado a nivel de bytecode en Python 3.11 (gracias al "Adaptive Specializing Interpreter"). Reemplaza las largas cadenas imperativas de if/elif y los diccionarios de mapeo, permitiendo validar patrones complejos (como tuplas de apertura y cierre) de forma nativa, muy limpia y rápida.
# 2. Paradigma Iterativo Puro: Uso explícito de estado mutable (la lista `stack` que actúa como pila) mutada en cada ciclo mediante `.append()` y `.pop()`, controlando el flujo paso a paso a través de un bucle `for`.
# 3. Tipado Genérico Estándar: Uso de `list[str]` sin dependencias externas, estándar en las versiones modernas de Python.

def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada y cerrada en orden.
    
    Implementación estrictamente iterativa aprovechando 
    Pattern Matching de Python moderno.
    """
    # Pila mutable para rastrear los caracteres de apertura
    stack: list[str] = []
    
    for char in s:
        # Evaluamos el caracter actual utilizando Pattern Matching
        match char:
            # Agrupación lógica de casos: caracteres de apertura
            case '(' | '[' | '{':
                stack.append(char)
                
            # Agrupación lógica: caracteres de cierre
            case ')' | ']' | '}':
                # Si llega un cierre y la pila está vacía, no hay balance
                if not stack:
                    return False
                
                # Extraemos el último elemento apilado (mutación de estado)
                top = stack.pop()
                
                # Pattern Matching anidado sobre tuplas para validar el par exacto
                match (top, char):
                    case ('(', ')') | ('[', ']') | ('{', '}'):
                        # El par coincide correctamente, el bucle continúa
                        continue
                    case _:
                        # Cualquier otra combinación es inválida (ej. '[' con '}')
                        return False
                        
    # Al terminar la iteración, la pila debe estar completamente vacía.
    # Una cadena vacía ("") no entra al bucle y retorna True automáticamente.
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

print("Todos los tests iterativos en Python 3.11 pasaron correctamente.")