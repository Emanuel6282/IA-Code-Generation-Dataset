//> using scala 3.6.4

// P05: Transaction Pipeline — Scala 3.6
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite definir bloques sin llaves, mejorando
//    la legibilidad del código.
//
// 2. Sintaxis while ... do (Scala 3):
//    Expresa explícitamente los bucles iterativos.
//
// 3. Sintaxis if ... then (Scala 3):
//    Permite escribir condiciones sin paréntesis.
//
// 4. Sintaxis object ...: (Scala 3):
//    Define objetos mediante indentación significativa.
//
// 5. var:
//    Declara acumuladores e índices mutables.
//
// 6. mutable.ListBuffer:
//    Almacena transacciones en una colección mutable.
//
// 7. mutable.Map:
//    Representa cada transacción como un diccionario.
//
// NOTA SOBRE SCALA 3.6:
// La sintaxis de Scala 3 utilizada aquí es estable
// y compatible con Scala 3.6. No se introducen
// características experimentales de Scala 3.6,
// porque no aportan ventajas al algoritmo iterativo.
//
// var, ListBuffer y mutable.Map existían antes
// de Scala 3.
//
// Biblioteca estándar únicamente.
// Sin map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object P05TransactionPipeline_v36:

  // Procesa una colección mutable de transacciones.
  def processTransactions(
      transactions: mutable.ListBuffer[mutable.Map[String, Any]]
  ): Double =

    // Acumulador mutable.
    var total: Double = 0.0

    // Índice mutable.
    var i: Int = 0

    // Recorrido estrictamente iterativo.
    while i < transactions.length do

      val transaction = transactions(i)

      // Obtener el estado de la transacción.
      val status =
        transaction("status").asInstanceOf[String]

      // Obtener el importe numérico.
      val amount: Double =
        transaction("amount") match
          case number: java.lang.Number =>
            number.doubleValue()
          case _ =>
            0.0

      // Filtrar transacciones válidas.
      if status == "approved" && amount > 0.0 then

        // Aplicar la comisión del 10 %.
        val netAmount = amount * 0.9

        // Acumular el importe neto.
        total += netAmount

      // Avanzar al siguiente elemento.
      i += 1

    // Redondear a dos decimales.
    math.round(total * 100.0) / 100.0

  // Construye una transacción mutable.
  def tx(
      id: Int,
      amount: Double,
      status: String,
      category: String
  ): mutable.Map[String, Any] =

    val transaction = mutable.Map[String, Any]()

    transaction("id") = id
    transaction("amount") = amount
    transaction("status") = status
    transaction("category") = category

    transaction

  def main(args: Array[String]): Unit =

    // TEST 1: Una aprobada y otra rechazada.
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