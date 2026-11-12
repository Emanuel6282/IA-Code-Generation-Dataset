// P05: Transaction Pipeline — Scala 3.3
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite escribir bloques sin llaves.
//
// 2. Sintaxis if ... then ... else (Scala 3):
//    Expresa condiciones como expresiones.
//
// 3. case class:
//    Representa transacciones inmutables.
//
// 4. val:
//    Define valores que no pueden reasignarse.
//
// 5. List:
//    Colección inmutable de transacciones.
//
// 6. filter(), map(), flatMap(), foldLeft():
//    Procesan los datos sin mutaciones.
//
// 7. Option:
//    Representa valores presentes o ausentes.
//
// 8. @tailrec:
//    Verifica que una función recursiva pueda
//    optimizarse mediante recursión de cola.
//
// NOTA:
// La sintaxis de indentación y then corresponde
// a Scala 3. Las operaciones funcionales y
// @tailrec ya existían en versiones anteriores.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, for ni while.

import scala.annotation.tailrec

object P05TransactionPipeline_v33:

  // Modelo inmutable de transacción.
  final case class Transaction(
      id: Int,
      amount: Double,
      status: String,
      category: String
  )

  // Suma funcional con recursión de cola.
  @tailrec
  private def sumTailrec(
      amounts: List[Double],
      accumulator: Double = 0.0
  ): Double =
    amounts match
      case Nil =>
        accumulator

      case head :: tail =>
        sumTailrec(tail, accumulator + head)

  // Función principal.
  def processTransactions(
      transactions: List[Transaction]
  ): Double =

    // 1. Filtrar transacciones válidas.
    val validTransactions = transactions.filter: transaction =>
      transaction.status == "approved" &&
      transaction.amount > 0.0

    // 2. Aplicar la comisión del 10 %.
    val netAmounts = validTransactions.map: transaction =>
      transaction.amount * 0.9

    // 3. Utilizar Option y flatMap para conservar
    //    únicamente los importes presentes.
    val optionalAmounts = netAmounts.map: amount =>
      Option(amount)

    val amounts = optionalAmounts.flatMap: amount =>
      amount

    // 4. Acumular mediante foldLeft.
    val total = amounts.foldLeft(0.0): (accumulator, amount) =>
      accumulator + amount

    // 5. Usar recursión de cola para verificar
    //    que el acumulado coincida con la suma.
    val verifiedTotal = sumTailrec(amounts)

    // 6. Seleccionar el total mediante una expresión.
    val finalTotal =
      if total == verifiedTotal then total
      else verifiedTotal

    // 7. Redondear a dos decimales.
    math.round(finalTotal * 100.0) / 100.0

  def main(args: Array[String]): Unit =

    // TEST 1: Una aprobada y otra rechazada.
    assert(
      processTransactions(List(
        Transaction(1, 100.0, "approved", "sales"),
        Transaction(2, 200.0, "rejected", "sales")
      )) == 90.0
    )

    // TEST 2: Dos aprobadas y una con importe negativo.
    assert(
      processTransactions(List(
        Transaction(1, 100.0, "approved", "sales"),
        Transaction(2, 50.0, "approved", "services"),
        Transaction(3, -20.0, "approved", "refund")
      )) == 135.0
    )

    // TEST 3: Lista vacía.
    assert(
      processTransactions(List.empty[Transaction]) == 0.0
    )

    println("All tests passed")