# Características de Python 3.14 utilizadas en este refactoring:
# 1. Structural Pattern Matching (`match-case`) con guardas de restricción (`if`):
#    Permite filtrar y capturar el campo `amount` de diccionarios en un solo paso
#    declarativo, reemplazando las verificaciones mediante índices y operadores lógicos.
# 2. Tipado estricto con `TypedDict` y `NotRequired` (PEP 655):
#    Sustituye la firma genérica `List[Dict[str, Any]]` por un esquema explícito para
#    las estructuras de transacciones.
# 3. Tipos contenedores nativos (PEP 585):
#    Aprovecha la sintaxis nativa `list[Transaction]` omitiendo las importaciones obsoletas de `typing.List`.
# 4. Asignación acumulativa idiomática (`+=`):
#    Sustituye la reasignación explícita `total = total + ...` por la sintaxis estándar y legible.

from typing import NotRequired, TypedDict


class Transaction(TypedDict):
    id: int
    amount: float | int
    status: str
    category: NotRequired[str]


def process_transactions(transactions: list[Transaction]) -> float:
    """Procesa una lista de transacciones calculando el monto acumulado con comisión del 10%."""
    total: float = 0.0

    for t in transactions:
        match t:
            case {"status": "approved", "amount": amount} if amount > 0:
                total += amount * 0.9
            case _:
                pass

    return round(total, 2)


# --- Tests conservados y adaptados a Python 3.14 ---
assert process_transactions([]) == 0.0
assert process_transactions([{"id": 1, "amount": 1000.0, "status": "approved", "category": "x"}]) == 900.0
assert process_transactions([{"id": 1, "amount": 100.0, "status": "rejected", "category": "x"}]) == 0.0

print("All tests passed")


# --- Explicación de los cambios ---
# 1. Adopción de Structural Pattern Matching (`match-case`):
#    En Python moderno (3.10+ optimizado en 3.14), la sentencia `match-case` ofrece una forma
#    declarativa de desestructurar diccionarios, validando simultáneamente la existencia de la
#    clave `status == "approved"` y la extracción de `amount` en una única instrucción legible.
# 2. Migración a Tipos Genéricos Nativos (`list` en lugar de `List`):
#    Se simplifica la anotación de tipos utilizando los coleccionables nativos del intérprete,
#    evitando dependencias innecesarias del módulo `typing`.
# 3. Tipado estructural declarativo mediante `TypedDict` y `NotRequired`:
#    Modelado preciso de la estructura de las entradas para facilitar el análisis estático de tipos,
#    soportando claves opcionales como `category`.
# 4. Operador de asignación compuesta (`+=`):
#    Sustituye la sintaxis extendida `total = total + ...` por la expresión estándar más compacta y clara.