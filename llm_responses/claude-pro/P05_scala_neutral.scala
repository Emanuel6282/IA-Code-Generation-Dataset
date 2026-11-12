import scala.math.BigDecimal.RoundingMode

object TransactionPipeline {

  /** Transacción financiera inmutable. `category` tiene valor por defecto porque el
    * enunciado la lista como campo, pero su ejemplo la omite y la función no la usa.
    */
  final case class Transaction(id: Int, amount: Double, status: String, category: String = "")

  private val CommissionFactor = BigDecimal("0.9")   // comisión del 10 %

  /** Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
    * una comisión del 10 %, y retorna el total redondeado a 2 decimales.
    *
    * Los cálculos se hacen con BigDecimal para evitar los errores de representación de
    * Double: por ejemplo, 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con Double el
    * producto vale un poco menos y math.round(x * 100) / 100.0 devuelve 1.03. Además,
    * BigDecimal suma sin acumular errores de redondeo. BigDecimal(d) de Scala parte de
    * la representación decimal del Double (1.15 -> 1.15), no de su valor binario exacto.
    */
  def processTransactions(transactions: List[Transaction]): Double =
    transactions
      .filter(t => t.status == "approved" && t.amount > 0)   // 1. filtrar
      .map(t => BigDecimal(t.amount) * CommissionFactor)      // 2. aplicar la comisión
      .sum                                                    // 3. sumar (lista vacía -> 0)
      .setScale(2, RoundingMode.HALF_UP)                      // redondeo comercial (1.035 -> 1.04)
      .toDouble

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado, tal cual (sin category)
    assert(processTransactions(List(
      Transaction(1, 100.0, "approved"),
      Transaction(2, 200.0, "rejected"))) == 90.0)

    // Caso 2: lista vacía y lista sin transacciones válidas (rechazada, cero, negativa)
    assert(processTransactions(List.empty) == 0.0)
    assert(processTransactions(List(
      Transaction(1, 50.0, "rejected", "food"),
      Transaction(2, 0.0, "approved", "food"),
      Transaction(3, -30.0, "approved", "refund"))) == 0.0)

    // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
    assert(processTransactions(List(Transaction(1, 1.15, "approved", "fees"))) == 1.04)
    assert(processTransactions(List(
      Transaction(1, 0.3, "approved", "food"),
      Transaction(2, 0.2, "approved", "food"),
      Transaction(3, 99.99, "rejected", "food"))) == 0.45)

    println("Todas las pruebas pasaron.")
  }
}