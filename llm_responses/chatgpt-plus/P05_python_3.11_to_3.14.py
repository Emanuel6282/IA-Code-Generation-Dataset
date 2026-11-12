# P05: Transaction Pipeline — Python 3.14
# Migración del código base proporcionado
# Paradigma: Iterativo
#
# CARACTERÍSTICAS UTILIZADAS:
#
# 1. Genéricos integrados (PEP 585):
#    Se utiliza list[dict[str, Any]] en lugar de
#    List[Dict[str, Any]], simplificando las anotaciones.
#
# 2. Evaluación diferida de anotaciones (PEP 649 y PEP 749):
#    Novedad de Python 3.14.
#    Las anotaciones se evalúan cuando se solicitan,
#    en lugar de evaluarse necesariamente al definir
#    la función. No cambia la lógica del algoritmo.
#
# 3. Asignación aumentada (+=):
#    Simplifica la acumulación de importes.
#
# 4. Estructuras iterativas:
#    Se conserva el bucle for y el condicional if.
#
# NOTA DE COMPATIBILIDAD:
# Los genéricos integrados y += existían antes de 3.14.
# La evaluación diferida de anotaciones sí es nueva
# en Python 3.14.
#
# Biblioteca estándar únicamente.

from typing import Any


def process_transactions(
    transactions: list[dict[str, Any]]
) -> float:
    # Acumulador mutable.
    total = 0.0

    # Recorrido iterativo original.
    for t in transactions:

        # Mantener exactamente las condiciones originales.
        if t["status"] == "approved" and t["amount"] > 0:

            # Aplicar la comisión del 10 % y acumular.
            total += t["amount"] * 0.9

    # Mantener el redondeo original.
    return round(total, 2)


# TESTS ORIGINALES: conservados sin modificaciones.
assert process_transactions([]) == 0.0
assert process_transactions([{"id":1,"amount":1000.0,"status":"approved","category":"x"}]) == 900.0
assert process_transactions([{"id":1,"amount":100.0,"status":"rejected","category":"x"}]) == 0.0


# CAMBIOS REALIZADOS EN LA MIGRACIÓN A PYTHON 3.14
#
# 1. Modernización de las anotaciones:
#
#    Antes:
#      from typing import List, Dict, Any
#
#      def process_transactions(
#          transactions: List[Dict[str, Any]]
#      ) -> float:
#
#    Ahora:
#      from typing import Any
#
#      def process_transactions(
#          transactions: list[dict[str, Any]]
#      ) -> float:
#
#    Motivo:
#    Los genéricos integrados proporcionan una sintaxis
#    más concisa e idiomática.
#
# 2. Evaluación diferida de anotaciones:
#
#    Python 3.14 introduce el mecanismo definido en
#    PEP 649 y PEP 749.
#
#    Motivo:
#    Permite evaluar las anotaciones cuando se necesitan.
#    Esta mejora no requiere modificar el algoritmo.
#
# 3. Simplificación de la acumulación:
#
#    Antes:
#      total = total + t["amount"] * 0.9
#
#    Ahora:
#      total += t["amount"] * 0.9
#
#    Motivo:
#    Expresa la acumulación de forma más concisa.
#    Para los importes numéricos del problema,
#    mantiene el mismo resultado.
#
# 4. Conservación del filtrado:
#
#    Se mantiene:
#      t["status"] == "approved" and t["amount"] > 0
#
#    Motivo:
#    Preservar exactamente las reglas originales.
#
# 5. Conservación de la comisión:
#
#    Se mantiene:
#      t["amount"] * 0.9
#
#    Motivo:
#    Aplicar el mismo descuento del 10 %.
#
# 6. Conservación del redondeo:
#
#    Se mantiene:
#      round(total, 2)
#
#    Motivo:
#    Obtener los mismos resultados.
#
# 7. Conservación de los tres tests:
#
#    Se mantienen exactamente las mismas entradas
#    y los mismos resultados esperados.
#
#    Motivo:
#    Comprobar que la migración no altere el comportamiento.
#
# 8. Conservación del paradigma iterativo:
#
#    Se mantiene:
#      for t in transactions:
#
#    Motivo:
#    Evitar cambios algorítmicos innecesarios.
#
# COMPLEJIDAD:
#    Tiempo: O(n).
#    Espacio auxiliar: O(1).
#
# RESULTADO:
#    Misma lógica y comportamiento que el código base,
#    con sintaxis moderna compatible con Python 3.14.