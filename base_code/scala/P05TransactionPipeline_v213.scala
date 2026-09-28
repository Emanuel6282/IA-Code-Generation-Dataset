// P05: Transaction Pipeline — Scala 2.13 (codigo base para refactoring)
// Estilo: bucles for anidados con banderas (flags) y acumuladores mutables
// El desafio funcional en el refactor es encadenar filter -> map -> sum

object P05TransactionPipeline_v213 {

  def processTransactions(transactions: List[Map[String, Any]]): Double = {
    var total = 0.0
    for (tx <- transactions) {
      val status = tx("status").asInstanceOf[String]
      val amount = tx("amount").asInstanceOf[Double]
      var isValid = false
      if (status == "approved") {
        if (amount > 0) {
          isValid = true
        }
      }
      if (isValid) {
        total = total + amount * 0.9
      }
    }
    Math.round(total * 100.0) / 100.0
  }

  def tx(id: Int, amount: Double, status: String, cat: String): Map[String, Any] =
    Map("id" -> id, "amount" -> amount, "status" -> status, "category" -> cat)

  def main(args: Array[String]): Unit = {
    val txs1 = List(
      tx(1, 100.0, "approved", "income"),
      tx(2, -50.0, "approved", "income"),
      tx(3, 200.0, "rejected", "income"),
      tx(4, 150.0, "approved", "expense")
    )
    assert(Math.abs(processTransactions(txs1) - 225.0) < 0.01)
    assert(processTransactions(List.empty) == 0.0)
    assert(Math.abs(processTransactions(List(tx(1, 1000.0, "approved", "x"))) - 900.0) < 0.01)
    println("All tests passed")
  }
}
