// P05: Transaction Pipeline — Scala 2.13 (paradigma funcional)
// Solo biblioteca estandar de Scala.

object TransactionPipeline {

  /**
   * Representa una transaccion financiera con sus cuatro campos.
   * Case class inmutable, forma idiomatica en Scala para un portador de
   * datos simple.
   */
  case class Transaction(id: Int, amount: Double, status: String, category: String)

  /**
   * Procesa una lista de transacciones financieras: filtra las aprobadas
   * con monto positivo, aplica una comision del 10%, y retorna la suma
   * total resultante redondeada a 2 decimales.
   *
   * Implementacion estrictamente funcional: usa filter() para el paso de
   * filtrado, map() para aplicar la comision, y foldLeft() para la suma
   * final, tal como pide explicitamente el requisito. Todas las
   * colecciones intermedias son inmutables (List). No se usan var ni
   * mutable.*.
   *
   * @param transactions Lista de transacciones a procesar.
   * @return La suma total de los montos aprobados y positivos, despues
   *         de aplicar la comision del 10%, redondeada a 2 decimales.
   *         Si la lista esta vacia o no hay transacciones validas,
   *         retorna 0.0.
   */
  def processTransactions(transactions: List[Transaction]): Double = {
    val total: Double = transactions
      .filter(t => t.status == "approved" && t.amount > 0)
      .map(t => t.amount * 0.9)
      .foldLeft(0.0)((acumulado, monto) => acumulado + monto)

    BigDecimal(total).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado, solo una transaccion valida
    val transacciones1 = List(
      Transaction(1, 100.0, "approved", "sales"),
      Transaction(2, 200.0, "rejected", "sales")
    )
    assert(processTransactions(transacciones1) == 90.0)

    // Caso 2: lista vacia -> 0.0
    assert(processTransactions(List.empty[Transaction]) == 0.0)

    // Caso 3: varias transacciones validas, incluyendo un monto negativo
    // (que debe excluirse) y una rechazada (que tambien debe excluirse)
    val transacciones3 = List(
      Transaction(1, 50.0, "approved", "food"),
      Transaction(2, -20.0, "approved", "food"),
      Transaction(3, 30.0, "rejected", "food"),
      Transaction(4, 25.5, "approved", "travel")
    )
    // (50.0 * 0.9) + (25.5 * 0.9) = 45.0 + 22.95 = 67.95
    assert(processTransactions(transacciones3) == 67.95)

    println("Todos los casos de prueba pasaron correctamente.")
  }
}