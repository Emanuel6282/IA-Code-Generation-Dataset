/*
 * CARACTERÍSTICAS NUEVAS DE SCALA 3.3 UTILIZADAS:
 * 1. Optional Braces Syntax (Sintaxis basada en sangría): Se eliminan las llaves
 *    y los puntos y comas en favor de un código declarativo más limpio.
 * 2. Método @main annotation: Reemplaza el clásico `def main(args: Array[String]): Unit`
 *    por una anotación de punto de entrada limpia y nativa.
 * 3. Enlazado funcional con sintaxis de sangría `: `: Utilizado en los bloques de
 *    filter, map y sum para reemplazar iteraciones con bucles y mutabilidad por un pipeline inmutable.
 * 4. Extracción segura con Pattern Matching / Option: Manejo declarativo mediante
 *    get() e inmutabilidad garantizada con 'val' en lugar de 'var'.
 */

import scala.math.BigDecimal.RoundingMode

object P05TransactionPipeline_v33:

  /**
   * Procesa la lista de transacciones mediante un pipeline declarativo inmutable (filter -> map -> sum).
   *
   * @param transactions Lista de mapas que representan cada transacción.
   * @return Suma de montos aprobados con descuento del 10%, redondeada a 2 decimales.
   */
  def processTransactions(transactions: List[Map[String, Any]]): Double =
    val total = transactions
      .filter: tx =>
        val status = tx.get("status").collect { case s: String => s }.getOrElse("")
        val amount = tx.get("amount").collect {
          case d: Double => d
          case f: Float  => f.toDouble
          case i: Int    => i.toDouble
        }.getOrElse(0.0)

        status == "approved" && amount > 0.0
      .map: tx =>
        tx("amount").asInstanceOf[Double] * 0.9
      .sum

    BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toDouble

  def tx(id: Int, amount: Double, status: String, cat: String): Map[String, Any] =
    Map("id" -> id, "amount" -> amount, "status" -> status, "category" -> cat)

  @main def runTests(): Unit =
    val txs1 = List(
      tx(1, 100.0, "approved", "income"),
      tx(2, -50.0, "approved", "income"),
      tx(3, 200.0, "rejected", "income"),
      tx(4, 150.0, "approved", "expense")
    )
    assert(Math.abs(processTransactions(txs1) - 225.0) < 0.01)
    assert(processTransactions(List.empty) == 0.0)
    assert(Math.abs(processTransactions(List(tx(1, 1000.0, "approved", "x"))) - 900.0) < 0.01)

    println("All tests passed")

/*
 * EXPLICACIÓN DE LOS CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.3:
 *
 * 1. Eliminación de var y banderas (flags): Se reemplazó el bucle 'for' con mutabilidad
 *    y banderas 'isValid' por un pipeline declarativo encadenado (`filter -> map -> sum`).
 *    Esto elimina efectos secundarios e inestabilidades por mutabilidad.
 *
 * 2. Sintaxis de sangría `: `: Scala 3 permite pasar bloques lambda usando dos puntos y sangría
 *    (p. ej. `.filter: tx =>`), haciendo el código más legible sin exceso de paréntesis ni llaves.
 *
 * 3. Reemplazo del objeto ejecutable `App` o `main` verboso por `@main`:
 *    Scala 3 introduce `@main def runTests(): Unit` como forma estándar de definir ejecutables.
 *
 * 4. Precisión y redondeo decimal con `BigDecimal`: En lugar de la fórmula imperativa
 *    `Math.round(total * 100.0) / 100.0` susceptible a errores de coma flotante, se utilizó
 *    `BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toDouble`, garantizando precisión financiera.
 */