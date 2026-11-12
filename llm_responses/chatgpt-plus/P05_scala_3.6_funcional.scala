//> using scala 3.6.4

// P05: Transaction Pipeline — Scala 3.6
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite definir bloques sin llaves.
//
// 2. Sintaxis if ... then ... else (Scala 3):
//    Permite expresar condiciones como valores.
//
// 3. case class:
//    Representa transacciones mediante objetos inmutables.
//
// 4. val:
//    Define valores que no pueden reasignarse.
//
// 5. List:
//    Colección inmutable de transacciones.
//
// 6. filter(), map(), flatMap(), foldLeft():
//    Permiten procesar datos sin mutaciones.
//
// 7. Option:
//    Representa valores presentes o ausentes.
//
// 8. @tailrec:
//    Garantiza que una función recursiva pueda
//    optimizarse mediante recursión de cola.
//
// 9. Scala 3.6:
//    Se utiliza la sintaxis estable de Scala 3,
//    compatible con la versión 3.6.
//
// NOTA:
// Las operaciones funcionales y @tailrec ya existían
// antes de Scala 3.6. No se utilizan características
// experimentales que no aporten ventajas al problema.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, for ni while.

import scala.annotation.tailrec

object P05TransactionPipeline_v36:

  // Modelo inmutable de una transacción.
  final case class Transaction(
      id: Int,
      amount: Double,
      status: String,
      category: String
  )

  // Función auxiliar recursiva para sumar importes.
  // @tailrec verifica la optimización de cola.
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

  // Función principal: procesamiento funcional.
  def processTransactions(
      transactions: List[Transaction]
  ): Double =

    // 1. Filtrar transacciones válidas.
    val validTransactions = transactions.filter: transaction =>
      transaction.status == "approved" &&
      transaction.amount > 0.0

    // 2. Aplicar comisión del 10 %.
    val netAmounts = validTransactions.map: transaction =>
      transaction.amount * 0.9

    // 3. Representar los importes mediante Option.
    val optionalAmounts = netAmounts.map: amount =>
      Option(amount)

    // 4. Extraer los valores presentes con flatMap.
    val amounts = optionalAmounts.flatMap: amount =>
      amount

    // 5. Acumular con foldLeft, sin estado mutable.
    val total = amounts.foldLeft(0.0): (accumulator, amount) =>
      accumulator + amount

    // 6. Calcular también mediante recursión de cola.
    val recursiveTotal = sumTailrec(amounts)

    // 7. Seleccionar el resultado mediante una expresión.
    val finalTotal =
      if total == recursiveTotal then total
      else recursiveTotal

    // 8. Redondear a dos decimales.
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