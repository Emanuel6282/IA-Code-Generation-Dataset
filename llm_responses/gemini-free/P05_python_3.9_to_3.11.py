# Características de Python 3.11 utilizadas en este refactoring:
# 1. `typing.TypedDict` con `NotRequired` (PEP 655): Permite definir estructuras de datos
#    fuertemente tipadas donde campos opcionales como 'category' no son obligatorios.
# 2. Anotaciones de tipos nativas (PEP 585): Uso directo de `list` en lugar de `typing.List`
#    para una firma de función más limpia.
# 3. Operador de asignación compuesta `+=`: Aumenta la claridad del acumulador mutable.
# 4. Asignación defensiva con `.get()` en TypedDict: Previene `KeyError` runtime en entornos de tipado dinámico.

from typing import NotRequired, TypedDict


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: NotRequired[str]


def process_transactions(transactions: list[Transaction]) -> float:
    """Procesa una lista de transacciones calculando el monto total con descuento."""
    total: float = 0.0
    for t in transactions:
        if t.get("status") == "approved" and t.get("amount", 0.0) > 0:
            total += t["amount"] * 0.9
    return round(total, 2)


# --- Tests conservados y adaptados a Python 3.11 ---
assert process_transactions([]) == 0.0
assert process_transactions([{"id": 1, "amount": 1000.0, "status": "approved", "category": "x"}]) == 900.0
assert process_transactions([{"id": 1, "amount": 100.0, "status": "rejected", "category": "x"}]) == 0.0

print("All tests passed")


# --- Explicación de los cambios ---
# 1. Definición de modelo estricto con `TypedDict` y `NotRequired` (PEP 655):
#    Python 3.11 introdujo `NotRequired`, permitiendo especificar exactamente qué campos
#    pueden faltar en el diccionario sin romper el tipado estático, eliminando el uso de `Dict[str, Any]`.
# 2. Eliminación de importaciones obsoletas (`List` de `typing`):
#    Aprovechando los tipos genéricos integrados (PEP 585), la firma se simplificó a `list[Transaction]`,
#    haciendo el código más idiomático y liviano.
# 3. Asignación acumulativa idiomática (`+=`):
#    Se reemplazó `total = total + t["amount"] * 0.9` por `total += t["amount"] * 0.9`,
#    estándar en código Python moderno para legibilidad.
# 4. Acceso seguro mediante `.get()`:
#    Garantiza robustez en la evaluación iterativa ante esquemas de diccionarios incompletos.