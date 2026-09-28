// P05: Transaction Pipeline — Scala 3.6
//
// Características utilizadas:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite escribir los bloques de control sin llaves innecesarias,
//    mejorando la legibilidad.
//
// 2. var:
//    Se utiliza para mantener el acumulador y el índice de iteración,
//    necesarios para el procesamiento iterativo.
//
// 3. mutable.ListBuffer:
//    Se utiliza como colección mutable para representar la lista
//    de transacciones.
//
// 4. mutable.Map:
//    Se utiliza para representar cada transacción como un diccionario
//    mutable.
//
// 5. while:
//    Se utiliza como mecanismo explícito de iteración.
//
// Nota:
// Scala 3.6 no introduce una característica específica que mejore
// materialmente este algoritmo sin cambiar el paradigma iterativo.
// Por ello, no se fuerzan características nuevas que no aporten
// valor al problema.

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
 * 1. Se utiliza la sintaxis de indentación de Scala 3, eliminando
 *    llaves innecesarias de los bloques if, while y métodos.
 *
 * 2. Se mantienen var y un acumulador mutable porque son requisitos
 *    explícitos del paradigma iterativo.
 *
 * 3. Se mantiene mutable.ListBuffer para representar la colección
 *    de transacciones.
 *
 * 4. Se mantiene mutable.Map para representar cada transacción.
 *
 * 5. Se utiliza while para recorrer la colección de forma explícitamente
 *    iterativa.
 *
 * 6. Se conserva exactamente la condición de filtrado:
 *
 *       status == "approved" && amount > 0
 *
 * 7. Se conserva exactamente la aplicación de la comisión:
 *
 *       amount * 0.9
 *
 * 8. Se conserva exactamente el redondeo a dos decimales:
 *
 *       Math.round(total * 100.0) / 100.0
 *
 * 9. Una lista vacía o sin transacciones válidas produce 0.0 porque
 *    el acumulador comienza en 0.0.
 *
 * 10. Se conservaron exactamente los tres casos de prueba originales,
 *     utilizando assert.
 *
 * 11. No se utilizan map(), filter(), foldLeft(), for, recursión,
 *     Streams ni otros mecanismos funcionales.
 *
 * 12. No se utilizan librerías de terceros; únicamente la biblioteca
 *     estándar de Scala.
 *
 * 13. No se introducen artificialmente características de Scala 3.6.
 *     Las características de Scala 3.6 que no aportan una mejora real
 *     al algoritmo no se fuerzan para evitar alterar innecesariamente
 *     su diseño.
 */