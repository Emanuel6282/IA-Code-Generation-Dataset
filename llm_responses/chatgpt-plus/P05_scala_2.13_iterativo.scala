// P05: Transaction Pipeline — Scala 2.13
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. scala.collection.mutable.ListBuffer:
//    Permite almacenar transacciones en una colección mutable.
//
// 2. scala.collection.mutable.Map:
//    Representa cada transacción mediante pares clave-valor.
//
// 3. var:
//    Declara variables mutables para el índice y el total.
//
// 4. while:
//    Recorre las transacciones de manera iterativa.
//
// 5. if:
//    Selecciona transacciones aprobadas con importe positivo.
//
// 6. math.round:
//    Redondea el total a dos decimales.
//
// 7. Biblioteca de colecciones de Scala 2.13:
//    Scala 2.13 reorganizó y mejoró sus colecciones.
//    ListBuffer y mutable.Map forman parte de esta API.
//
// NOTA:
// var, while, ListBuffer y mutable.Map ya existían
// antes de Scala 2.13. Se utiliza su API compatible
// con esta versión, sin atribuirles una introducción
// específica en Scala 2.13.
//
// Biblioteca estándar únicamente.
// Sin map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object P05TransactionPipeline_v213 {

  // Procesa una lista mutable de transacciones.
  def processTransactions(
      transactions: mutable.ListBuffer[mutable.Map[String, Any]]
  ): Double = {

    // Acumulador mutable.
    var total: Double = 0.0

    // Índice mutable.
    var i: Int = 0

    // Recorrido estrictamente iterativo.
    while (i < transactions.length) {

      val transaction = transactions(i)

      // Obtener el estado y el importe.
      val status = transaction("status").asInstanceOf[String]

      val amount = transaction("amount") match {
        case number: java.lang.Number => number.doubleValue()
        case _ => 0.0
      }

      // Filtrar las transacciones válidas.
      if (status == "approved" && amount > 0) {

        // Aplicar una comisión del 10 %.
        val netAmount = amount * 0.9

        // Acumular el importe neto.
        total += netAmount
      }

      // Avanzar al siguiente elemento.
      i += 1
    }

    // Redondear el resultado final a dos decimales.
    math.round(total * 100.0) / 100.0
  }

  // Construye una transacción mutable.
  def tx(
      id: Int,
      amount: Double,
      status: String,
      category: String
  ): mutable.Map[String, Any] = {

    val transaction = mutable.Map[String, Any]()

    transaction("id") = id
    transaction("amount") = amount
    transaction("status") = status
    transaction("category") = category

    transaction
  }

  def main(args: Array[String]): Unit = {

    // TEST 1: Una transacción aprobada y otra rechazada.
    val transactions1 =
      mutable.ListBuffer[mutable.Map[String, Any]]()

    transactions1 += tx(1, 100.0, "approved", "sales")
    transactions1 += tx(2, 200.0, "rejected", "sales")

    assert(processTransactions(transactions1) == 90.0)

    // TEST 2: Dos aprobadas y una con importe negativo.
    val transactions2 =
      mutable.ListBuffer[mutable.Map[String, Any]]()

    transactions2 += tx(1, 100.0, "approved", "sales")
    transactions2 += tx(2, 50.0, "approved", "services")
    transactions2 += tx(3, -20.0, "approved", "refund")

    assert(processTransactions(transactions2) == 135.0)

    // TEST 3: Lista vacía.
    val transactions3 =
      mutable.ListBuffer[mutable.Map[String, Any]]()

    assert(processTransactions(transactions3) == 0.0)

    println("All tests passed")
  }
}