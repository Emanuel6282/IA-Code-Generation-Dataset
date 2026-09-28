// P05: Transaction Pipeline — Scala 3.3
//
// Características utilizadas:
//
// 1. Sintaxis de Scala 3:
//    Se utiliza la sintaxis "if ... then", propia de Scala 3,
//    para mejorar la legibilidad de las condiciones.
//
// 2. mutable.ListBuffer:
//    Permite representar la lista de transacciones mediante una
//    colección mutable, adecuada para el paradigma iterativo.
//
// 3. mutable.Map:
//    Permite representar cada transacción como un diccionario mutable.
//
// 4. while:
//    Se utiliza como mecanismo principal de iteración.
//
// 5. var:
//    Se utiliza para mantener el índice y el acumulador mutable.
//
// No se utilizan map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object TransactionPipeline:

  def processTransactions(
      transactions: mutable.ListBuffer[mutable.Map[String, Any]]
  ): Double =

    var total = 0.0
    var index = 0

    while index < transactions.length do
      val transaction = transactions(index)

      val status = transaction("status").asInstanceOf[String]
      val amount = transaction("amount").asInstanceOf[Double]

      if status == "approved" && amount > 0 then
        total += amount * 0.9

      index += 1

    Math.round(total * 100.0) / 100.0

  private def tx(
      id: Int,
      amount: Double,
      status: String,
      category: String
  ): mutable.Map[String, Any] =

    mutable.Map[String, Any](
      "id" -> id,
      "amount" -> amount,
      "status" -> status,
      "category" -> category
    )

  def main(args: Array[String]): Unit =

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

/*
 * Cambios realizados:
 *
 * 1. Se utiliza la sintaxis de Scala 3:
 *
 *       if condicion then
 *         ...
 *
 *    Esto mejora la legibilidad y evita llaves innecesarias.
 *
 * 2. Se mantiene mutable.ListBuffer para representar la colección
 *    de transacciones, cumpliendo el requisito del paradigma iterativo.
 *
 * 3. Se mantiene mutable.Map para representar cada transacción
 *    como un diccionario mutable.
 *
 * 4. Se utiliza while para recorrer explícitamente la colección.
 *
 * 5. Se utilizan var para el índice y el acumulador total.
 *
 * 6. Se mantiene exactamente la condición de filtrado:
 *
 *       status == "approved" && amount > 0
 *
 * 7. Se mantiene exactamente la comisión del 10%:
 *
 *       amount * 0.9
 *
 * 8. Se mantiene el redondeo a dos decimales:
 *
 *       Math.round(total * 100.0) / 100.0
 *
 * 9. Una lista vacía o sin transacciones válidas mantiene total
 *    en 0.0 y, por tanto, retorna 0.0.
 *
 * 10. Se conservaron exactamente tres casos de prueba mediante assert.
 *
 * 11. No se utilizan map(), filter(), foldLeft(), for, recursion
 *     ni ningún otro mecanismo funcional para el procesamiento.
 *
 * 12. La sintaxis if ... then es una característica idiomática de
 *     Scala 3 y mejora la legibilidad del código iterativo.
 *
 * 13. No se introducen artificialmente características de Scala 3
 *     que no aporten al problema. La lógica continúa siendo
 *     explícitamente iterativa.
 */