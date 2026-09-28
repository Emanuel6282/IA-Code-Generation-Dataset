# P05: Transaction Pipeline — Python 3.14

#

# Características utilizadas:

# 1. list[...] y dict[...] para anotaciones de tipos:

# proporcionan una sintaxis integrada, moderna y legible.

# Esta sintaxis está disponible desde Python 3.9, por lo que no se

# presenta incorrectamente como una característica exclusiva de 3.14.

#

# 2. No se utiliza una característica exclusiva de Python 3.14:

# las novedades de 3.14 no aportan una mejora relevante de legibilidad

# o rendimiento para este algoritmo tan sencillo.

#

# El objetivo es mantener el código idiomático sin introducir complejidad

# artificial únicamente para utilizar características nuevas.

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

# se utiliza la sintaxis moderna de tipos integrada en Python.

#

# 2. Se eliminaron los imports de typing:

# ya no son necesarios para las anotaciones utilizadas.

#

# 3. Se mantuvo el paradigma iterativo:

# el bucle for y el acumulador conservan exactamente la lógica original.

#

# 4. Se conservaron los tres casos de prueba originales:

# lista vacía, transacción aprobada y transacción rechazada.

#

# 5. Se mantuvo round(total, 2):

# garantiza el mismo comportamiento de redondeo que el código original.

#

# 6. No se introdujeron características exclusivas de Python 3.14:

# ninguna novedad de esta versión proporciona una ventaja significativa

# para este algoritmo. Evitar novedades irrelevantes mantiene el código

# simple, legible y compatible con la lógica original.
