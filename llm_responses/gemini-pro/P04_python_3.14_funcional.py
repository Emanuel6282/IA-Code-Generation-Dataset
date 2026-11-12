# CARACTERISTICAS DE PYTHON 3.14 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Sintaxis de Alias de Tipos Nativa (PEP 695): Se utiliza la palabra clave `type` para definir `ImmutableStack`. En Python 3.14 esta es la forma canónica, limpia y segura de declarar alias sin necesidad del módulo `typing`.
# 2. Pattern Matching Estructural de Secuencias: Usamos `match/case` avanzado para deconstruir la tupla de estado inmutable (`case [*rest, '(']`). En Python 3.13/3.14, el compilador JIT Tier 2 optimiza fuertemente el bytecode de estas evaluaciones funcionales.
# 3. Inmutabilidad Absoluta y Composición: Eliminación total de estado mutable y bucles. El estado es una tupla estricta (`tuple[str, ...]`). 
# 4. Pipeline Funcional Puro (filter -> map -> reduce): El flujo de datos se transforma y pliega funcionalmente. Propagamos `None` como "Short-circuit" puro en cuanto se detecta un desbalance para ignorar operaciones subsecuentes sin romper el pipeline.

from functools import reduce

# Alias de tipo moderno (Python 3.12+)
type ImmutableStack = tuple[str, ...] | None

def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada y cerrada en orden.
    
    Implementación estrictamente funcional en Python 3.14.
    Sin bucles, utilizando únicamente funciones puras de orden superior y estados inmutables.
    """
    
    # 1. filter(): Asegura perezosamente que solo entren caracteres válidos al pipeline.
    clean_chars = filter(lambda c: c in "()[]{}", s)
    
    # 2. map(): Transforma el flujo en tokens booleanos: (es_apertura, caracter).
    tokens = map(lambda c: (c in "([{", c), clean_chars)
    
    # 3. Función reductora pura: calcula el siguiente estado inmutable
    def reducer(stack: ImmutableStack, token: tuple[bool, str]) -> ImmutableStack:
        # Propagación de estado inválido (ya se rompió el balance previamente)
        if stack is None:
            return None
            
        match token:
            case (True, opener):
                # Generamos una NUEVA tupla concatenando el caracter (Inmutabilidad)
                return stack + (opener,)
                
            case (False, closer):
                # Pattern Matching para verificar el "tope" de la tupla funcional.
                # Si coincide, extraemos el resto de la tupla (*rest) creando una nueva.
                match (stack, closer):
                    case ([*rest, '('], ')'): return tuple(rest)
                    case ([*rest, '['], ']'): return tuple(rest)
                    case ([*rest, '{'], '}'): return tuple(rest)
                    case _: 
                        # Falla funcional: desbalance o pila vacía al intentar cerrar
                        return None
                        
        return None

    # 4. reduce(): Pliega el flujo aplicando el reducer sobre un estado inicial de tupla vacía.
    final_state = reduce(reducer, tokens, ())
    
    # Una cadena perfectamente balanceada resulta exactamente en una tupla vacía.
    return final_state == ()

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

print("Todos los tests funcionales en Python 3.14 pasaron correctamente.")