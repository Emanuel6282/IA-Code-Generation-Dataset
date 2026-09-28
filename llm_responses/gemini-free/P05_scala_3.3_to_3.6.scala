/*
 * CARACTERÍSTICAS NUEVAS DE SCALA 3.6 UTILIZADAS:
 * 1. Optional Braces Syntax (Sintaxis basada en sangría): Se eliminan las llaves
 *    y los puntos y comas en favor de una estructura limpia y declarativa basada en sangría.
 * 2. Bloques de argumento con `: ` (Indent argument blocks): Permite encadenar expresiones
 *    lambda en colecciones (.filter:, .map:) sin parentesis ni llaves innecesarias.
 * 3. Anotación `@main`: Reemplaza la sintaxis verbosa de `main(args: Array[String]): Unit`
 *    por una función ejecutable limpia propia del ecosistema moderno de Scala 3.
 * 4. Pattern Matching con colecciones seguras (`collect` / `Option`): Permite una extracción
 *    de tipos segura y declarativa del mapa sin lanzar excepciones de cast (`asInstanceOf`).
 */

import scala.math.BigDecimal.RoundingMode

object P05TransactionPipeline_v36:

  /**
   * Procesa la lista de transacciones mediante un pipeline declarativo funcional encadenado
   * (filter -> map -> sum).
   *
   * @param transactions Lista de mapas inmutables que representan las transacciones.
   * @return Suma total con descuento del 10% aplicada, redondeada a 2 decimales.
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
 * EXPLICACIÓN DE LOS CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6:
 *
 * 1. Refactorización de imperativo a declarativo encadenado (filter -> map -> sum):
 *    Se eliminó por completo el bucle 'for' con mutabilidad (`var total = 0.0`) y banderas
 *    (`var isValid = false`). Reemplazar esto por combinadores inmutables de colecciones elimina
 *    efectos secundarios y posibles errores de estado mutable.
 *
 * 2. Sintaxis de sangría `: `:
 *    En Scala 3.6, pasar lambdas de múltiples líneas a métodos de colección usando `: `
 *    (`.filter: tx =>`) hace que el código sea significativamente más claro y legible sin
 *    requerir llaves `{}`.
 *
 * 3. Extracción de valores segura con `get` y `collect`:
 *    En lugar de realizar casting directo con `.asInstanceOf[String]` o `.asInstanceOf[Double]`
 *    (que fallaría con una excepción si un campo es de un tipo inesperado o no existe),
 *    se utiliza la extracción segura con `collect` sobre `Option`.
 *
 * 4. Redondeo decimal preciso mediante `BigDecimal`:
 *    Se reemplaza la operación imperativa `Math.round(total * 100.0) / 100.0` por el método
 *    estándar `BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toDouble`, evitando
 *    imprecisiones en el cálculo de punto flotante.
 *
 * 5. Punto de entrada ejecutable `@main`:
 *    Reemplaza la declaración `def main(args: Array[String]): Unit` con la anotación moderna `@main`,
 *    que simplifica los scripts y puntos de entrada ejecutables en Scala 3.
 */