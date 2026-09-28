// Scala 3 - Paradigma Funcional Idiomático
// Utiliza Case Classes para inmutabilidad, Pattern Matching y operaciones de colecciones (filter, map, sum)
// junto con BigDecimal para garantizar precisión decimal en operaciones financieras.

import scala.math.BigDecimal.RoundingMode

// Modelo inmutable representado mediante un case class en Scala
case class Transaction(id: Int, amount: Double, status: String, category: String)

object TransactionPipeline {

  /**
   * Procesa una lista de transacciones financieras aplicando filtrado, comisión y redondeo.
   *
   * @param transactions Lista de objetos Transaction
   * @return Suma de montos aprobados con el 10% de descuento aplicado, redondeada a 2 decimales.
   */
  def processTransactions(transactions: List[Transaction]): Double = {
    if (transactions == null || transactions.isEmpty) return 0.0

    val rawTotal = transactions
      .filter(tx => tx != null && tx.status.equalsIgnoreCase("approved") && tx.amount > 0.0)
      .map(tx => tx.amount * 0.9)
      .sum

    // Redondeo de precisión a 2 decimales usando BigDecimal con HALF_UP
    BigDecimal(rawTotal)
      .setScale(2, RoundingMode.HALF_UP)
      .toDouble
  }

  def main(args: Array[String]): Unit = {

    // Caso 1: Ejemplo base del enunciado (Aprobada vs Rechazada)
    val tc1 = List(
      Transaction(1, 100.0, "approved", "income"),
      Transaction(2, 200.0, "rejected", "food")
    )
    assert(processTransactions(tc1) == 90.0, "Falló Caso 1: Debería ser 90.0")

    // Caso 2: Lista vacía o sin transacciones válidas (montos <= 0 o no aprobados)
    val tc2Empty = List.empty[Transaction]
    assert(processTransactions(tc2Empty) == 0.0, "Falló Caso 2a: Lista vacía")

    val tc2Invalid = List(
      Transaction(3, -50.0, "approved", "tech"),
      Transaction(4, 150.0, "rejected", "books")
    )
    assert(processTransactions(tc2Invalid) == 0.0, "Falló Caso 2b: Sin transacciones válidas")

    // Caso 3: Múltiples válidas y precisión de redondeo (HALF_UP)
    val tc3 = List(
      Transaction(5, 100.0, "approved", "tech"),
      Transaction(6, 50.55, "approved", "services"),
      Transaction(7, 0.0, "approved", "other")
    )
    // Cálculo: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
    assert(processTransactions(tc3) == 135.5, "Falló Caso 3: Debería ser 135.5")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}