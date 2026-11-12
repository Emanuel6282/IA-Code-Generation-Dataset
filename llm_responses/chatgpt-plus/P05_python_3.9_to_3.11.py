# P05: Transaction Pipeline — Python 3.11
# Migración desde Python 3.9
# Paradigma: Iterativo
#
# CARACTERÍSTICAS MODERNAS UTILIZADAS:
#
# 1. Genéricos integrados (PEP 585, Python 3.9):
#    Se utiliza list[dict[str, Any]] en lugar de
#    List[Dict[str, Any]].
#    Esto simplifica las anotaciones de tipos.
#
# 2. Operador de asignación aumentada (+=):
#    Simplifica la acumulación sin cambiar su resultado.
#
# 3. Especialización adaptativa (PEP 659, Python 3.11):
#    El intérprete de Python 3.11 puede optimizar
#    automáticamente operaciones frecuentes.
#    No requiere cambios adicionales en el algoritmo.
#
# NOTA:
# Los genéricos integrados y += ya existían antes
# de Python 3.11. PEP 659 sí es una novedad de 3.11.
#
# Se conserva el recorrido iterativo original.
# Biblioteca estándar únicamente.

from typing import Any


def process_transactions(
    transactions: list[dict[str, Any]]
) -> float:
    # Acumulador mutable original.
    total = 0.0

    # Recorrer las transacciones.
    for t in transactions:

        # Mantener las condiciones originales.
        if t["status"] == "approved" and t["amount"] > 0:

            # Aplicar la comisión del 10 % y acumular.
            total += t["amount"] * 0.9

    # Conservar el redondeo original.
    return round(total, 2)


# TESTS ORIGINALES: conservados sin modificaciones.
assert process_transactions([]) == 0.0
assert process_transactions([{"id":1,"amount":1000.0,"status":"approved","category":"x"}]) == 900.0
assert process_transactions([{"id":1,"amount":100.0,"status":"rejected","category":"x"}]) == 0.0


# CAMBIOS REALIZADOS EN LA MIGRACIÓN PYTHON 3.9 -> PYTHON 3.11
#
# 1. Modernización de las anotaciones de tipos:
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
#    más concisa. Están disponibles desde Python 3.9
#    y son idiomáticos en Python 3.11.
#
# 2. Simplificación de la acumulación:
#
#    Antes:
#      total = total + t["amount"] * 0.9
#
#    Ahora:
#      total += t["amount"] * 0.9
#
#    Motivo:
#    Es una forma más concisa de expresar la acumulación.
#    Para los importes numéricos del problema,
#    mantiene el mismo resultado.
#
# 3. Conservación del filtrado:
#
#    Se mantiene:
#      t["status"] == "approved" and t["amount"] > 0
#
#    Motivo:
#    Preservar exactamente las reglas originales.
#
# 4. Conservación de la comisión:
#
#    Se mantiene:
#      t["amount"] * 0.9
#
#    Motivo:
#    Aplicar el mismo descuento del 10 %.
#
# 5. Conservación del redondeo:
#
#    Se mantiene:
#      return round(total, 2)
#
#    Motivo:
#    Mantener los resultados originales.
#
# 6. Conservación de los tres casos de prueba:
#
#    Se mantienen las mismas entradas y resultados
#    esperados del código original.
#
#    Motivo:
#    Verificar que la migración no altere el comportamiento.
#
# 7. Compatibilidad con Python 3.11:
#
#    El código puede beneficiarse de la especialización
#    adaptativa del intérprete introducida en Python 3.11.
#
#    Motivo:
#    Aprovechar mejoras internas de rendimiento sin
#    introducir dependencias ni modificar el algoritmo.
#
# COMPLEJIDAD:
#    Tiempo: O(n).
#    Espacio auxiliar: O(1).
#
# RESULTADO:
#    Misma lógica y resultados que Python 3.9,
#    con sintaxis moderna compatible con Python 3.11.