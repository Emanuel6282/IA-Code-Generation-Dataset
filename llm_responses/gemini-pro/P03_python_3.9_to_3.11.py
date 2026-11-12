# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Structural Pattern Matching (match/case): Introducido en 3.10 y optimizado fuertemente en la máquina virtual de Python 3.11. Reemplaza el uso imperativo de `isinstance()` para la comprobación de tipos, haciendo que el enrutamiento lógico sea más rápido, seguro y declarativo.
# 2. Tipado Genérico Integrado (PEP 585): Se utiliza `list[int]` de forma nativa. Ya no es necesario importar `List` desde el módulo `typing`, reduciendo la carga de importaciones y modernizando las anotaciones estáticas.

from typing import Any

def flatten(nested: Any) -> list[int]:
    result = []
    stack = [nested]
    
    while stack:
        item = stack.pop()
        
        # Structural Pattern Matching para el enrutamiento del tipo de dato
        match item:
            case list():
                # En lugar del lento bucle for de Python 3.9, extendemos la pila 
                # directamente aprovechando la implementación en C de extend() y reversed()
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


# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.11:
#
# 1. match/case vs isinstance: En Python 3.9, se usaba `if isinstance(item, list):`. En Python 3.11, la forma idiomática de deconstruir y verificar tipos estructurales es mediante `match item: case list():`. El compilador de Python 3.11 optimiza estas comprobaciones a nivel de bytecode, haciéndolo superior en legibilidad y en muchos casos en rendimiento.
#
# 2. Eliminación de typing.List: Se eliminó `from typing import List`. En el código moderno de Python (3.9+), las colecciones estándar admiten genéricos directamente. Por lo tanto, `List[int]` se convierte idiomáticamente en `list[int]`.
#
# 3. Optimización del bucle interno (stack.extend): El código base de 3.9 utilizaba un bucle `for element in reversed(item): stack.append(element)`. Aunque funcionaba, en Python iterar explícitamente en el nivel de usuario es lento. Fue reemplazado por `stack.extend(reversed(item))`, delegando la iteración a la capa subyacente de C, lo cual es mucho más rápido y una mejor práctica idiomática para mutar listas sin importar la versión, pero que brilla junto a las mejoras de velocidad generales del intérprete de Python 3.11.