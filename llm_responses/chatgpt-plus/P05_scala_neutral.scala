// P05: Transaction Pipeline — Scala
// Paradigma: Funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. case class:
//    Representa transacciones con campos inmutables.
//
// 2. List:
//    Colección inmutable de transacciones.
//
// 3. filter:
//    Selecciona transacciones aprobadas con amount > 0.
//
// 4. map:
//    Aplica una comisión del 10 %.
//
// 5. sum:
//    Calcula la suma de los importes netos.
//
// 6. math.round:
//    Redondea el total a dos decimales.
//
// Biblioteca estándar únicamente.

object TransactionPipeline {

  // Modelo inmutable de una transacción.
  case class Transaction(
    id: Int,
    amount: Double,
    status: String,
    category: String
  )

  // Procesa una lista de transacciones financieras.
  def processTransactions(
    transactions: List[Transaction]
  ): Double = {

    // 1. Filtrar transacciones válidas.
    val validTransactions = transactions.filter { transaction =>
      transaction.status == "approved" &&
      transaction.amount > 0
    }

    // 2. Aplicar comisión del 10 %.
    val netAmounts = validTransactions.map { transaction =>
      transaction.amount * 0.9
    }

    // 3. Sumar los importes netos.
    val total = netAmounts.sum

    // 4. Redondear a dos decimales.
    math.round(total * 100.0) / 100.0
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Una transacción aprobada y otra rechazada.
    assert(
      processTransactions(List(
        Transaction(1, 100.0, "approved", "sales"),
        Transaction(2, 200.0, "rejected", "sales")
      )) == 90.0
    )

    // Test 2: Dos aprobadas y una con importe negativo.
    assert(
      processTransactions(List(
        Transaction(1, 100.0, "approved", "sales"),
        Transaction(2, 50.0, "approved", "services"),
        Transaction(3, -20.0, "approved", "refund")
      )) == 135.0
    )

    // Test 3: Lista vacía.
    assert(
      processTransactions(List.empty[Transaction]) == 0.0
    )

    println("All tests passed")
  }
}