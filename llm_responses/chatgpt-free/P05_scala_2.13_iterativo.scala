// P05: Transaction Pipeline — Scala 2.13
//
// Características utilizadas:
// 1. mutable.ListBuffer: colección mutable apropiada para representar
//    la lista de transacciones en un paradigma iterativo.
// 2. mutable.Map: permite representar cada transacción como un
//    diccionario mutable de campos.
// 3. while: se utiliza como mecanismo principal de iteración.
// 4. var: permite mantener acumuladores y variables mutables.
//
// Estas herramientas forman parte de la biblioteca estándar de Scala 2.13.
// No se utilizan map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object TransactionPipeline {

  def processTransactions(
      transactions: mutable.ListBuffer[mutable.Map[String, Any]]
  ): Double = {

    var total = 0.0
    var index = 0

    while (index < transactions.length) {
      val transaction = transactions(index)

      val status = transaction("status").asInstanceOf[String]
      val amount = transaction("amount").asInstanceOf[Double]

      if (status == "approved" && amount > 0) {
        total = total + amount * 0.9
      }

      index += 1
    }

    BigDecimal(total)
      .setScale(2, BigDecimal.RoundingMode.HALF_UP)
      .toDouble
  }

  private def tx(
      id: Int,
      amount: Double,
      status: String,
      category: String
  ): mutable.Map[String, Any] = {

    mutable.Map[String, Any](
      "id" -> id,
      "amount" -> amount,
      "status" -> status,
      "category" -> category
    )
  }

  def main(args: Array[String]): Unit = {

    // TC01: filtrar negativo y rechazado, aplicar comisión
    val transactions1 = mutable.ListBuffer[
      mutable.Map[String, Any]
    ](
      tx(1, 100.0, "approved", "income"),
      tx(2, -50.0, "approved", "income"),
      tx(3, 200.0, "rejected", "income"),
      tx(4, 150.0, "approved", "expense")
    )

    assert(
      math.abs(processTransactions(transactions1) - 225.0) < 0.01
    )


    // TC02: lista vacía
    val transactions2 = mutable.ListBuffer[
      mutable.Map[String, Any]
    ]()

    assert(
      processTransactions(transactions2) == 0.0
    )


    // TC03: una transacción approved
    val transactions3 = mutable.ListBuffer[
      mutable.Map[String, Any]
    ](
      tx(1, 1000.0, "approved", "x")
    )

    assert(
      math.abs(processTransactions(transactions3) - 900.0) < 0.01
    )

    println("All tests passed")
  }
}