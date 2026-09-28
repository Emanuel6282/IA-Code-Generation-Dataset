// P05: Transaction Pipeline — Scala 2.13
//
// Estilo: funcional, utilizando colecciones inmutables,
// filter, map y foldLeft.

object TransactionPipeline {

  def processTransactions(
      transactions: List[Map[String, Any]]
  ): Double = {

    val total = transactions
      .filter { tx =>
        val status = tx("status").asInstanceOf[String]
        val amount = tx("amount").asInstanceOf[Double]

        status == "approved" && amount > 0
      }
      .map { tx =>
        val amount = tx("amount").asInstanceOf[Double]
        amount * 0.9
      }
      .foldLeft(0.0)(_ + _)

    BigDecimal(total).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def main(args: Array[String]): Unit = {

    // Test 1: approved válida + rejected + approved negativa
    val transactions1 = List(
      Map[String, Any](
        "id" -> 1,
        "amount" -> 100.0,
        "status" -> "approved",
        "category" -> "income"
      ),
      Map[String, Any](
        "id" -> 2,
        "amount" -> -50.0,
        "status" -> "approved",
        "category" -> "income"
      ),
      Map[String, Any](
        "id" -> 3,
        "amount" -> 200.0,
        "status" -> "rejected",
        "category" -> "income"
      ),
      Map[String, Any](
        "id" -> 4,
        "amount" -> 150.0,
        "status" -> "approved",
        "category" -> "expense"
      )
    )

    assert(
      math.abs(processTransactions(transactions1) - 225.0) < 0.01
    )

    // Test 2: lista vacía
    assert(
      processTransactions(List.empty) == 0.0
    )

    // Test 3: una transacción aprobada
    val transactions3 = List(
      Map[String, Any](
        "id" -> 1,
        "amount" -> 1000.0,
        "status" -> "approved",
        "category" -> "income"
      )
    )

    assert(
      math.abs(processTransactions(transactions3) - 900.0) < 0.01
    )

    println("All tests passed")
  }
}