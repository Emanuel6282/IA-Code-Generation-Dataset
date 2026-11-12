# CARACTERISTICAS DE PYTHON 3.9 Y PARADIGMA FUNCIONAL UTILIZADAS:
# 1. Tipado Genérico Nativo (PEP 585): Uso de `tuple[str, ...]` y `dict[str, str]` sin necesidad de importar desde el módulo `typing`.
# 2. Inmutabilidad Absoluta: En lugar de usar una lista mutable y métodos como `.append()` o `.pop()`, el estado se propaga creando nuevas tuplas inmutables (`stack + (char,)` o `stack[:-1]`).
# 3. filter(), map() y reduce(): Eliminación completa de bucles `for` o `while`. Se declara un pipeline funcional: 
#    - `filter` purifica el flujo (práctica funcional defensiva).
#    - `map` transforma los datos en tokens enriquecidos (metadatos de apertura/cierre).
#    - `reduce` pliega (folds) los tokens a través de una función pura para obtener un estado final, propagando `None` de forma segura en caso de un desbalance temprano (Short-circuit funcional).

from functools import reduce

def is_balanced(s: str) -> bool:
    """
    Verifica si una cadena de paréntesis, corchetes y llaves 
    está correctamente balanceada y cerrada en orden.
    
    Implementación estrictamente funcional en Python 3.9: sin bucles,
    sin estado mutable, utilizando map, filter y reduce.
    """
    
    # Diccionario inmutable de pares de cierre -> apertura
    pairs: dict[str, str] = {')': '(', ']': '[', '}': '{'}
    
    # 1. filter(): Asegura perezosamente que solo fluyan los caracteres correctos.
    clean_chars = filter(lambda c: c in "()[]{}", s)
    
    # 2. map(): Transforma cada caracter en una tupla de evaluación (es_apertura, caracter).
    tokens = map(lambda c: (c not in pairs, c), clean_chars)
    
    # Función pura para reduce. Transforma el estado inmutable (tuple) paso a paso.
    def reducer(stack: tuple[str, ...], token: tuple[bool, str]) -> tuple[str, ...]:
        # Si el estado es None, significa que ya falló en un paso previo. Propagamos el fallo.
        if stack is None:
            return None
            
        is_opener, char = token
        
        if is_opener:
            # En lugar de mutar, retornamos una NUEVA tupla concatenada.
            return stack + (char,)
        else:
            # Si es de cierre, verificamos el "tope" de la tupla.
            if stack and stack[-1] == pairs[char]:
                # Retornamos una NUEVA tupla excluyendo el último elemento (pop funcional)
                return stack[:-1]
            
            # Falla funcional: desbalance detectado
            return None

    # 3. reduce(): Pliega (folds) el iterador usando el reducer y una tupla vacía como estado inicial.
    final_state = reduce(reducer, tokens, ())
    
    # Si la cadena es válida, el estado final debe ser exactamente una tupla vacía.
    return final_state == ()

# ==========================================
# CASOS DE PRUEBA
# ==========================================

# 1. Caso base simple balanceado
assert is_balanced("()") is True, "Error: '()' debería retornar True"

# 2. Caso de anidamiento incorrecto
assert is_balanced("([)]") is False, "Error: '([)]' debería retornar False por cierre desordenado"

# 3. Caso de anidamiento profundo balanceado
assert is_balanced("{[]}") is True, "Error: '{[]}' debería retornar True"

# Caso adicional (Requisito implícito): Lista vacía
assert is_balanced("") is True, "Error: Una cadena vacía debería retornar True"

print("Todos los tests funcionales en Python 3.9 pasaron correctamente.")