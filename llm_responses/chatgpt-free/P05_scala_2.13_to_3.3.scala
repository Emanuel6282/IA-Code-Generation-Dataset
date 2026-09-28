// P05: Transaction Pipeline — Scala 3.3
//
// Características de Scala 3 utilizadas:
//
// 1. Sintaxis de indentación:
//    Elimina llaves innecesarias y mejora la legibilidad de métodos,
//    bloques condicionales y bucles.
//
// 2. if ... then:
//    Sintaxis condicional de Scala 3 que hace más clara la estructura
//    de las condiciones.
//
// 3. for con sintaxis de Scala 3:
//    Se conserva el recorrido iterativo para mantener exactamente
//    el comportamiento del código original.
//
// Nota:
// No se fuerzan características más avanzadas de Scala 3.3, ya que
// no aportarían una mejora real a este algoritmo sin cambiar
// innecesariamente su estructura o comportamiento.

object P05TransactionPipeline_v33:

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
 * 1. Se cambió la declaración del objeto a la sintaxis de indentación
 *    de Scala 3:
 *
 *        object P05TransactionPipeline_v33:
 *
 *    en lugar de utilizar llaves.
 *
 * 2. Los métodos utilizan sintaxis de indentación de Scala 3, evitando
 *    llaves innecesarias.
 *
 * 3. Las condiciones se escribieron utilizando "if ... then", que es
 *    la sintaxis idiomática de Scala 3.
 *
 * 4. El bucle for se adaptó a la sintaxis de Scala 3:
 *
 *        for tx <- transactions do
 *
 *    manteniendo el comportamiento iterativo del código original.
 *
 * 5. Se conservaron var, el acumulador total y la bandera isValid porque
 *    el objetivo de esta migración es mantener la lógica original y no
 *    cambiar el paradigma del programa.
 *
 * 6. Se conservaron List y Map inmutables, ya que ya eran apropiados
 *    para representar las transacciones.
 *
 * 7. Se mantuvo exactamente la función processTransactions.
 *
 * 8. Se mantuvo exactamente la lógica de validación:
 *    status == "approved" y amount > 0.
 *
 * 9. Se mantuvo exactamente la comisión del 10%:
 *    amount * 0.9.
 *
 * 10. Se mantuvo exactamente el redondeo a dos decimales:
 *     Math.round(total * 100.0) / 100.0.
 *
 * 11. Se conservaron los tres casos de prueba originales, únicamente
 *     adaptando su formato a la sintaxis de Scala 3.
 *
 * 12. No se introducen características de Scala 3.3 de forma artificial.
 *     Características como enum, opaque types o match más avanzado no
 *     aportarían una mejora material a este algoritmo.
 *
 * 13. No se utilizan librerías de terceros.
 */