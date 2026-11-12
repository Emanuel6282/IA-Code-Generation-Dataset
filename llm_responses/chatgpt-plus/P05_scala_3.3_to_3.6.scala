//> using scala 3.6.4

// P05: Transaction Pipeline — Scala 3.6
// Migración del código proporcionado a Scala 3.6
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite definir bloques sin llaves y mejora
//    la legibilidad del código.
//
// 2. Sintaxis if ... then (Scala 3):
//    Simplifica las condiciones y elimina paréntesis.
//
// 3. Sintaxis for ... do (Scala 3):
//    Permite escribir recorridos iterativos sin llaves.
//
// 4. Sintaxis object Nombre: (Scala 3):
//    Define objetos mediante indentación significativa.
//
// 5. Inferencia de tipos:
//    Mantiene declaraciones concisas con val y var.
//
// 6. Colecciones inmutables List y Map:
//    Conservan la estructura de datos original.
//
// NOTA SOBRE SCALA 3.6:
// Se utilizan características estables de Scala 3
// compatibles con Scala 3.6. No se incorporan
// características experimentales porque no aportan
// mejoras necesarias a este algoritmo.
//
// Biblioteca estándar únicamente.

object P05TransactionPipeline_v36:

  /**
   * Procesa una lista de transacciones:
   * 1. Filtra: status == "approved" y amount > 0
   * 2. Aplica una comisión del 10 %
   * 3. Retorna la suma redondeada a dos decimales
   */
  def processTransactions(
      transactions: List[Map[String, Any]]
  ): Double =

    var total = 0.0

    // Recorrido iterativo con sintaxis de Scala 3.
    for tx <- transactions do

      val status = tx("status").asInstanceOf[String]
      val amount = tx("amount").asInstanceOf[Double]

      var isValid = false

      // Conservar las condiciones originales.
      if status == "approved" then
        if amount > 0 then
          isValid = true

      // Acumular únicamente las transacciones válidas.
      if isValid then
        total = total + amount * 0.9

    // Mantener el redondeo original.
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
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN A SCALA 3.6
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
 *      object P05TransactionPipeline_v36:
 *        def processTransactions(...) =
 *          ...
 *
 *    Motivo:
 *    Scala 3 permite utilizar indentación para
 *    delimitar bloques sin llaves.
 *
 * 2. MODERNIZACIÓN DEL BUCLE FOR
 *
 *    Antes:
 *      for (tx <- transactions) {
 *        ...
 *      }
 *
 *    Ahora:
 *      for tx <- transactions do
 *        ...
 *
 *    Motivo:
 *    Utiliza la sintaxis moderna de Scala 3
 *    sin modificar el comportamiento iterativo.
 *
 * 3. MODERNIZACIÓN DE LAS CONDICIONES
 *
 *    Antes:
 *      if (status == "approved") {
 *        if (amount > 0) {
 *          isValid = true
 *        }
 *      }
 *
 *    Ahora:
 *      if status == "approved" then
 *        if amount > 0 then
 *          isValid = true
 *
 *    Motivo:
 *    Reduce los delimitadores y mejora la legibilidad.
 *
 * 4. CONSERVACIÓN DEL ESTADO MUTABLE
 *
 *    Se mantienen:
 *      var total = 0.0
 *      var isValid = false
 *
 *    Motivo:
 *    Preservar el algoritmo original y su orden
 *    de procesamiento.
 *
 * 5. CONSERVACIÓN DE LA FIRMA
 *
 *    Se mantiene:
 *      processTransactions(
 *        transactions: List[Map[String, Any]]
 *      ): Double
 *
 *    Motivo:
 *    Evitar cambios en la interfaz pública.
 *
 * 6. CONSERVACIÓN DE LAS REGLAS FINANCIERAS
 *
 *    Se mantienen:
 *      status == "approved"
 *      amount > 0
 *      amount * 0.9
 *      Math.round(total * 100.0) / 100.0
 *
 *    Motivo:
 *    Garantizar resultados equivalentes.
 *
 * 7. CONSERVACIÓN DE LOS TESTS
 *
 *    TC01: 225.0
 *    TC02: 0.0
 *    TC03: 900.0
 *
 *    Las tres aserciones originales se conservan.
 *
 * 8. COMPATIBILIDAD CON SCALA 3.6
 *
 *    Se utiliza sintaxis estable de Scala 3,
 *    sin dependencias externas ni funciones preview.
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(1).
 */