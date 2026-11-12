# CARACTERISTICAS DE PYTHON 3.14 UTILIZADAS Y POR QUE:
# 1. Type Aliases Nativos (PEP 695): Se utiliza la nueva palabra clave `type` para definir una estructura de datos recursiva precisa, eliminando por completo la dependencia del módulo `typing` (adiós a `Any` y `List`).
# 2. Structural Pattern Matching (match/case): Introducido previamente pero fuertemente acelerado por el compilador JIT (Just-In-Time) en Python 3.13/3.14, reemplaza a `isinstance()` ofreciendo un enrutamiento de tipos más seguro y mucho más veloz.
# 3. Tipado Genérico Integrado: Uso de colecciones estándar nativas como genéricos (`list[int]`).

# Alias de tipo recursivo evaluado estáticamente de forma nativa
type NestedNode = int | list['NestedNode']

def flatten(nested: NestedNode) -> list[int]:
    result: list[int] = []
    stack = [nested]
    
    while stack:
        item = stack.pop()
        
        # Structural Pattern Matching para un enrutamiento altamente optimizado
        match item:
            case list():
                # Reemplazamos el bucle for por operaciones a nivel de C (extend + reversed)
                stack.extend(reversed(item))
            case _:
                result.append(item)
                
    return result

# ==========================================
# CASOS DE PRUEBA
# ==========================================
assert flatten([1, [2, 3], [4, [5, 6]]]) == [1, 2, 3, 4, 5, 6]
assert flatten([]) == []
assert flatten([1, 2, 3]) == [1, 2, 3]


# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.14:
#
# 1. Adiós al módulo 'typing': En Python 3.9 se usaba `Any` (perdiendo precisión estática) y `List`. En la era moderna de Python 3.14, el ecosistema prefiere tipados estrictos usando la nueva sintaxis `type Nombre = ...`. Definimos `NestedNode` como un entero o una lista de sí mismo, logrando tipado perfecto sin opacidad y sin importar librerías.
#
# 2. Reemplazo de isinstance() por match/case: El código base dependía de una función imperativa (`if isinstance(item, list):`). Python 3.14 brilla al utilizar el paradigma estructural (`match item: case list():`). Gracias al optimizador Tier 2 (JIT) del intérprete, este patrón ya no solo es más legible, sino que sus instrucciones se compilan directamente reduciendo la sobrecarga de evaluación de tipos.
#
# 3. Vectorización de la pila (stack.extend): El bucle `for element in reversed(item): stack.append(element)` forzaba al intérprete a ejecutar instrucciones Python en cada ciclo. Al cambiarlo por `stack.extend(reversed(item))`, toda la iteración y mutación se delega al lenguaje C subyacente de CPython. Aunque esto es válido en versiones anteriores, es el estándar idiomático moderno para complementar la velocidad que ofrece Python 3.14 en algoritmos iterativos de listas.