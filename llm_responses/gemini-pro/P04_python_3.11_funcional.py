# CARACTERISTICAS DE PYTHON 3.11 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Structural Pattern Matching de Secuencias (*rest): Utilizamos la capacidad de deconstruir secuencias (`case [*rest, '(']`). En Python 3.11 esto recibió una optimización masiva de rendimiento gracias al "Adaptive Specializing Interpreter" (PEP 659), haciendo que el enrutamiento de patrones en reducers sea extremadamente eficiente.
# 2. Type Hinting Moderno con Unión (PEP 604): Uso del operador `|` en las firmas (`tuple[str, ...] | None`) en lugar del antiguo `Optional`, nativo de las versiones más modernas.
# 3. Inmutabilidad y Composición Pura: Se elimina completamente la mutación de estado. El stack es una tupla (`tuple`) inmutable. El estado viaja y se transforma a través del pipeline funcional puro `filter` -> `map` -> `reduce`.
# 4. Short-circuit funcional: Si la cadena falla tempranamente, el reducer propaga `None` inmutablemente hasta el final sin procesar lógicas innecesarias.

from functools import reduce

def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada y cerrada en orden.
    
    Implementación estrictamente funcional en Python 3.11: 
    sin bucles for/while, sin estructuras mutables, usando reduce y Pattern Matching.
    """
    
    # 1. filter(): Purifica el flujo de datos perezosamente.
    clean_chars = filter(lambda c: c in "()[]{}", s)
    
    # 2. map(): Transforma la cadena en un flujo de tokens (es_apertura, caracter).
    tokens = map(lambda c: (c in "([{", c), clean_chars)
    
    # 3. Función reductora pura: calcula el nuevo estado inmutable a partir del anterior
    def reducer(stack: tuple[str, ...] | None, token: tuple[bool, str]) -> tuple[str, ...] | None:
        # Propagación de estado inválido (ya se detectó un desbalance previo)
        if stack is None:
            return None
            
        match token:
            case (True, opener):
                # Generamos una NUEVA tupla añadiendo el caracter (Inmutabilidad)
                return stack + (opener,)
                
            case (False, closer):
                # Pattern Matching Avanzado de secuencias:
                # Compara la tupla actual y el caracter de cierre simultáneamente.
                # Extrae el resto de la tupla (*rest) de forma nativa si hace 'match' perfecto.
                match (stack, closer):
                    case ([*rest, '('], ')'): return tuple(rest)
                    case ([*rest, '['], ']'): return tuple(rest)
                    case ([*rest, '{'], '}'): return tuple(rest)
                    case _: 
                        # Cualquier desajuste de parejas o pila vacía
                        return None
                        
        return None

    # 4. reduce(): Pliega (folds) los tokens partiendo de una tupla vacía como estado base.
    final_state = reduce(reducer, tokens, ())
    
    # El balance perfecto implica que el estado final no es None y no quedan aperturas pendientes.
    return final_state == ()

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base del ejemplo: un solo par balanceado
assert is_balanced("()") is True, "Error: '()' debería retornar True"

# 2. Caso de anidamiento incorrecto o cruzado
assert is_balanced("([)]") is False, "Error: '([)]' debería retornar False por cierre desordenado"

# 3. Caso de anidamiento profundo balanceado
assert is_balanced("{[]}") is True, "Error: '{[]}' debería retornar True"

# Prueba extra cubierta por los requerimientos base
assert is_balanced("") is True, "Error: Una cadena vacía debería retornar True"

print("Todos los tests funcionales en Python 3.11 pasaron correctamente.")