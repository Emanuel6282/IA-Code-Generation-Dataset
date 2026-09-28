from functools import reduce
from typing import TypedDict


class Transaction(TypedDict):
    id: int
    amount: float
    status: str
    category: str


def process_transactions(transactions: list[Transaction]) -> float:
    """
    Procesa una lista de transacciones financieras: filtra las aprobadas
    con monto positivo, aplica una comision del 10%, y retorna la suma
    total resultante redondeada a 2 decimales.

    Implementacion estrictamente funcional: usa filter() para el paso de
    filtrado, map() para aplicar la comision, y functools.reduce() para la
    suma final. No se usan bucles for/while con estado mutable; todas las
    transformaciones intermedias son inmutables (iteradores perezosos
    encadenados). Compatible con Python 3.11.

    Args:
        transactions: Lista de diccionarios, cada uno con las claves "id",
                       "amount", "status" y "category".

    Returns:
        Un float con la suma total de los montos aprobados y positivos,
        despues de aplicar la comision del 10%, redondeado a 2 decimales.
        Si la lista esta vacia o no hay transacciones validas, retorna 0.0.
    """
    aprobadas_positivas = filter(
        lambda t: t.get("status") == "approved" and t.get("amount", 0) > 0,
        transactions
    )

    con_comision = map(
        lambda t: t["amount"] * 0.9,
        aprobadas_positivas
    )

    total: float = reduce(lambda acumulado, monto: acumulado + monto, con_comision, 0.0)

    return round(total, 2)


if __name__ == "__main__":
    # Caso 1: ejemplo del enunciado, solo una transaccion valida
    transacciones1: list[Transaction] = [
        {"id": 1, "amount": 100.0, "status": "approved", "category": "sales"},
        {"id": 2, "amount": 200.0, "status": "rejected", "category": "sales"},
    ]
    assert process_transactions(transacciones1) == 90.0

    # Caso 2: lista vacia -> 0.0
    assert process_transactions([]) == 0.0

    # Caso 3: varias transacciones validas, incluyendo un monto negativo
    # (que debe excluirse) y una rechazada (que tambien debe excluirse)
    transacciones3: list[Transaction] = [
        {"id": 1, "amount": 50.0, "status": "approved", "category": "food"},
        {"id": 2, "amount": -20.0, "status": "approved", "category": "food"},
        {"id": 3, "amount": 30.0, "status": "rejected", "category": "food"},
        {"id": 4, "amount": 25.5, "status": "approved", "category": "travel"},
    ]
    # (50.0 * 0.9) + (25.5 * 0.9) = 45.0 + 22.95 = 67.95
    assert process_transactions(transacciones3) == 67.95

    print("Todos los casos de prueba pasaron correctamente.")