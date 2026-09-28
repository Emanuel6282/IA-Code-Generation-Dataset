# P05: Transaction Pipeline — Python 3.11

#

# Características utilizadas:

# 1. list[...] y dict[...] para las anotaciones de tipos:

# proporcionan una sintaxis moderna y legible, sin importar List/Dict.

# Estas anotaciones están disponibles desde Python 3.9.

#

# Nota: Python 3.11 no introduce una característica específica que mejore

# materialmente este algoritmo. Por ello, se mantiene la lógica original

# en lugar de forzar el uso de características irrelevantes para el problema.

def process_transactions(transactions: list[dict[str, object]]) -> float:
    total = 0.0

    for t in transactions:
        if t["status"] == "approved" and t["amount"] > 0:
            total = total + t["amount"] * 0.9

    return round(total, 2)

assert process_transactions([]) == 0.0
assert process_transactions([
{"id": 1, "amount": 1000.0, "status": "approved", "category": "x"}
]) == 900.0
assert process_transactions([
{"id": 1, "amount": 100.0, "status": "rejected", "category": "x"}
]) == 0.0

# Cambios realizados:

# 1. Se reemplazó List[Dict[str, Any]] por list[dict[str, object]]:

# utiliza la sintaxis moderna de tipos integrada en Python.

#

# 2. Se eliminaron los imports de typing:

# ya no son necesarios para expresar los tipos utilizados.

#

# 3. Se conservó el bucle for y el acumulador mutable:

# esto mantiene exactamente la lógica y el comportamiento del código base.

#

# 4. Se conservaron los tres tests originales:

# únicamente se ajustó el formato de los diccionarios para mejorar

# la legibilidad.

#

# 5. Se mantuvo round(total, 2):

# garantiza el mismo resultado numérico con dos decimales.

#

# 6. No se introdujeron características exclusivas de Python 3.11:

# ninguna de ellas aporta una mejora relevante a este algoritmo.
