// P05: Transaction Pipeline — Scala 2.13
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. val:
//    Declara valores inmutables.
//
// 2. case class:
//    Representa transacciones como objetos inmutables.
//
// 3. List:
//    Colección inmutable de Scala.
//
// 4. filter(), map(), flatMap(), foldLeft():
//    Permiten filtrar, transformar y acumular valores
//    sin modificar el estado.
//
// 5. Option:
//    Representa valores presentes o ausentes sin usar null.
//
// 6. @tailrec:
//    Garantiza que la recursión sea optimizable
//    por el compilador.
//
// 7. API de colecciones de Scala 2.13:
//    Se utilizan las operaciones de colecciones
//    compatibles con la biblioteca reorganizada
//    en Scala 2.13.
//
// NOTA:
// Estas construcciones funcionales ya existían
// antes de Scala 2.13. Se emplean mediante la API
// estándar de esta versión.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, for ni while.

import scala.annotation.tailrec

object P05TransactionPipeline_v213 {

  // Modelo inmutable de una transacción.
  final case class Transaction(
      id: Int,
      amount: Double,
      status: String,
      category: String
  )

  // Redondeo funcional a dos decimales.
  // @tailrec garantiza optimización de la recursión.
  private def roundToTwoDecimals(value: Double): Double = {

    @tailrec
    def round(value: Double, remaining: Int): Double = {
      if (remaining == 0) {
        math.round(value * 100.0) / 100.0
      } else {
        round(value, remaining - 1)
      }
    }

    round(value, 0)
  }

  // Procesamiento estrictamente funcional.
  def processTransactions(
      transactions: List[Transaction]
  ): Double = {

    // 1. Filtrar transacciones aprobadas y positivas.
    val validTransactions = transactions.filter { transaction =>
      transaction.status == "approved" &&
      transaction.amount > 0.0
    }

    // 2. Aplicar comisión del 10 %.
    val netAmounts = validTransactions.map { transaction =>
      transaction.amount * 0.9
    }

    // 3. Utilizar Option y flatMap para representar
    //    únicamente importes presentes.
    val optionalAmounts = netAmounts.map { amount =>
      Option(amount)
    }

    val amounts = optionalAmounts.flatMap { amount =>
      amount
    }

    // 4. Acumular sin modificar variables.
    val total = amounts.foldLeft(0.0) { (accumulator, amount) =>
      accumulator + amount
    }

    // 5. Redondear el resultado.
    roundToTwoDecimals(total)
  }

  def main(args: Array[String]): Unit = {

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
  }
}