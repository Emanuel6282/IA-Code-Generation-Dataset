//> using scala 3.3.6

// P05: Transaction Pipeline — Scala 3.3
// Migración desde Scala 2.13
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite escribir bloques sin llaves y reduce
//    la cantidad de símbolos del código.
//
// 2. Sintaxis de dos puntos para lambdas (Scala 3):
//    Permite escribir expresiones como:
//      transactions.filter: tx => ...
//    Mejora la legibilidad de las operaciones encadenadas.
//
// 3. Sintaxis object Nombre: (Scala 3):
//    Define objetos mediante indentación significativa.
//
// 4. Colecciones inmutables:
//    Conserva List y Map del código original.
//
// 5. filter, map y foldLeft:
//    Sustituyen el bucle, los flags y el acumulador
//    mutable por operaciones declarativas.
//
// 6. Inferencia de tipos:
//    Permite mantener declaraciones concisas.
//
// NOTA:
// La indentación significativa y la sintaxis de
// dos puntos son características de Scala 3.
// filter, map y foldLeft ya existían en Scala 2.13.
//
// Biblioteca estándar únicamente.

object P05TransactionPipeline_v33:

  /**
   * Procesa una lista de transacciones:
   * 1. Filtra las aprobadas con importe positivo.
   * 2. Aplica una comisión del 10 %.
   * 3. Suma y redondea a dos decimales.
   */
  def processTransactions(
      transactions: List[Map[String, Any]]
  ): Double =

    // 1. Filtrar transacciones válidas.
    val validTransactions = transactions.filter: tx =>
      val status = tx("status").asInstanceOf[String]
      val amount = tx("amount").asInstanceOf[Double]

      status == "approved" && amount > 0

    // 2. Aplicar la comisión del 10 %.
    val netAmounts = validTransactions.map: tx =>
      val amount = tx("amount").asInstanceOf[Double]
      amount * 0.9

    // 3. Sumar sin utilizar acumuladores mutables.
    val total = netAmounts.foldLeft(0.0): (acc, amount) =>
      acc + amount

    // 4. Mantener el redondeo original.
    Math.round(total * 100.0) / 100.0

  // Construye una transacción inmutable.
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

    // TC01: Filtrar negativos y rechazados.
    assert(Math.abs(processTransactions(txs1) - 225.0) < 0.01)

    // TC02: Lista vacía.
    assert(processTransactions(List.empty) == 0.0)

    // TC03: Una transacción aprobada.
    assert(Math.abs(processTransactions(List(tx(1, 1000.0, "approved", "x"))) - 900.0) < 0.01)

    println("All tests passed")

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN SCALA 2.13 -> 3.3
 *
 * 1. INDENTACIÓN SIGNIFICATIVA
 *
 *    Antes:
 *      object P05TransactionPipeline_v213 {
 *        def processTransactions(...) = {
 *          ...
 *        }
 *      }
 *
 *    Ahora:
 *      object P05TransactionPipeline_v33:
 *        def processTransactions(...) =
 *          ...
 *
 *    Motivo:
 *    Scala 3 permite expresar la estructura del programa
 *    mediante indentación, evitando llaves innecesarias.
 *
 * 2. ELIMINACIÓN DE VARIABLES MUTABLES
 *
 *    Antes:
 *      var total = 0.0
 *      var isValid = false
 *
 *    Ahora:
 *      val validTransactions = ...
 *      val netAmounts = ...
 *      val total = ...
 *
 *    Motivo:
 *    El uso de val reduce el estado mutable y facilita
 *    el razonamiento sobre el procesamiento.
 *
 * 3. SUSTITUCIÓN DEL BUCLE POR FILTER
 *
 *    Antes:
 *      for (tx <- transactions) {
 *        ...
 *      }
 *
 *    Ahora:
 *      transactions.filter: tx =>
 *        ...
 *
 *    Motivo:
 *    Expresa directamente qué transacciones deben
 *    conservarse, sin administrar flags manualmente.
 *
 * 4. SUSTITUCIÓN DE LA TRANSFORMACIÓN POR MAP
 *
 *    Antes:
 *      total = total + amount * 0.9
 *
 *    Ahora:
 *      validTransactions.map: tx =>
 *        tx("amount").asInstanceOf[Double] * 0.9
 *
 *    Motivo:
 *    Separa la aplicación de la comisión del cálculo
 *    de la suma.
 *
 * 5. SUSTITUCIÓN DEL ACUMULADOR POR FOLDLEFT
 *
 *    Antes:
 *      var total = 0.0
 *
 *    Ahora:
 *      netAmounts.foldLeft(0.0): (acc, amount) =>
 *        acc + amount
 *
 *    Motivo:
 *    Calcula la suma mediante una reducción funcional.
 *    El valor inicial 0.0 conserva el comportamiento
 *    cuando la lista está vacía.
 *
 * 6. CONSERVACIÓN DE LA FIRMA PÚBLICA
 *
 *    Se mantiene:
 *      processTransactions(
 *        transactions: List[Map[String, Any]]
 *      ): Double
 *
 *    Motivo:
 *    Evitar cambios en el contrato de la función.
 *
 * 7. CONSERVACIÓN DE LAS REGLAS FINANCIERAS
 *
 *    Se mantienen:
 *      status == "approved"
 *      amount > 0
 *      amount * 0.9
 *      Math.round(total * 100.0) / 100.0
 *
 *    Motivo:
 *    Preservar los resultados del código original.
 *
 * 8. CONSERVACIÓN DE LOS TESTS
 *
 *    TC01: 225.0
 *    TC02: 0.0
 *    TC03: 900.0
 *
 *    Las tres aserciones originales se conservan.
 *
 * 9. COMPATIBILIDAD CON SCALA 3.3
 *
 *    Se utilizan características estables de Scala 3
 *    y únicamente la biblioteca estándar.
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(n).
 */