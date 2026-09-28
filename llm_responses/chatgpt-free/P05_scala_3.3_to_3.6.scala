// P05: Transaction Pipeline — Scala 3.6
//
// Características utilizadas:
//
// 1. Sintaxis de indentación de Scala 3:
//    Reemplaza las llaves en objetos, métodos y bloques de control,
//    mejorando la legibilidad del código.
//
// 2. if ... then:
//    Sintaxis condicional moderna de Scala 3 que hace más explícita
//    la estructura de las condiciones.
//
// 3. for ... do:
//    Sintaxis moderna para el bucle for, manteniendo el procesamiento
//    iterativo del código original.
//
// 4. Inferencia de tipos:
//    Se conserva el uso de val sin especificar tipos innecesarios
//    cuando el compilador puede inferirlos.
//
// Nota:
// Estas características pertenecen a la sintaxis de Scala 3 y son
// totalmente compatibles con Scala 3.6. No se introduce artificialmente
// una característica específica de Scala 3.6 que no aporte una mejora
// real al algoritmo.
//
// El comportamiento y los resultados del programa se mantienen idénticos.

object P05TransactionPipeline_v36:

  def processTransactions(transactions: List[Map[String, Any]]): Double =
    var total = 0.0

    for tx <- transactions do
      val status = tx("status").asInstanceOf[String]
      val amount = tx("amount").asInstanceOf[Double]

      var isValid = false

      if status == "approved" then
        if amount > 0 then
          isValid = true

      if isValid then
        total = total + amount * 0.9

    Math.round(total * 100.0) / 100.0

  def tx(
      id: Int,
      amount: Double,
      status: String,
      cat: String
  ): Map[String, Any] =
    Map(
      "id" -> id,
      "amount" -> amount,
      "status" -> status,
      "category" -> cat
    )

  def main(args: Array[String]): Unit =
    val txs1 = List(
      tx(1, 100.0, "approved", "income"),
      tx(2, -50.0, "approved", "income"),
      tx(3, 200.0, "rejected", "income"),
      tx(4, 150.0, "approved", "expense")
    )

    assert(
      Math.abs(processTransactions(txs1) - 225.0) < 0.01
    )

    assert(
      processTransactions(List.empty) == 0.0
    )

    assert(
      Math.abs(
        processTransactions(
          List(tx(1, 1000.0, "approved", "x"))
        ) - 900.0
      ) < 0.01
    )

    println("All tests passed")

/*
 * Cambios realizados:
 *
 * 1. Se migró el objeto a la sintaxis de indentación de Scala 3:
 *
 *      object P05TransactionPipeline_v36:
 *
 *    Esto elimina las llaves exteriores y es idiomático en Scala 3.6.
 *
 * 2. Los métodos ahora utilizan bloques definidos mediante indentación,
 *    evitando llaves innecesarias.
 *
 * 3. Se reemplazó:
 *
 *      if (condicion) {
 *      }
 *
 *    por:
 *
 *      if condicion then
 *
 *    Esta es la sintaxis condicional moderna de Scala 3.
 *
 * 4. Se reemplazó:
 *
 *      for (tx <- transactions) {
 *      }
 *
 *    por:
 *
 *      for tx <- transactions do
 *
 *    manteniendo exactamente el mismo comportamiento iterativo.
 *
 * 5. Se mantuvo var para total porque el código original utiliza un
 *    acumulador mutable y esta migración busca conservar su lógica
 *    y paradigma.
 *
 * 6. Se mantuvo var isValid porque forma parte de la implementación
 *    original basada en una bandera (flag).
 *
 * 7. Se conservaron List y Map, por lo que no es necesario introducir
 *    colecciones mutables adicionales.
 *
 * 8. Se mantuvo exactamente el nombre de la función:
 *
 *      processTransactions
 *
 * 9. Se mantuvo exactamente la condición de validación:
 *
 *      status == "approved" && amount > 0
 *
 * 10. Se mantuvo exactamente el cálculo de la comisión:
 *
 *      amount * 0.9
 *
 * 11. Se mantuvo exactamente el redondeo:
 *
 *      Math.round(total * 100.0) / 100.0
 *
 * 12. Los tres tests originales fueron conservados y únicamente
 *     adaptados visualmente a la sintaxis de Scala 3.
 *
 * 13. No se utilizan librerías externas.
 *
 * 14. No se introducen map(), filter(), foldLeft() ni otras
 *     operaciones funcionales, porque el código base utiliza
 *     explícitamente un enfoque iterativo.
 *
 * 15. No se afirma que if ... then, for ... do o la sintaxis de
 *     indentación sean características exclusivas de Scala 3.6.
 *     Son características de Scala 3 que siguen siendo idiomáticas
 *     y válidas en Scala 3.6.
 */