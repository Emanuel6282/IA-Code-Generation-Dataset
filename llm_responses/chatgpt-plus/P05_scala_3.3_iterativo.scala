// P05: Transaction Pipeline — Scala 3.3
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación significativa (Scala 3):
//    Permite definir bloques sin llaves y mejora la legibilidad.
//
// 2. Sintaxis "object ...:" y "def ... =":
//    Utiliza el estilo moderno de Scala 3.
//
// 3. var:
//    Declara variables mutables para el índice y el total.
//
// 4. mutable.ListBuffer:
//    Almacena las transacciones en una colección mutable.
//
// 5. mutable.Map:
//    Representa cada transacción mediante pares clave-valor.
//
// 6. while:
//    Recorre la colección mediante un índice mutable.
//
// 7. Pattern matching:
//    Permite extraer importes numéricos de diferentes tipos.
//
// NOTA:
// La sintaxis de indentación es propia de Scala 3.
// var, while, ListBuffer, Map y pattern matching ya
// existían en versiones anteriores.
//
// Se utiliza únicamente la biblioteca estándar.
// Sin map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object P05TransactionPipeline_v33:

  // Procesa una lista mutable de transacciones.
  def processTransactions(
      transactions: mutable.ListBuffer[mutable.Map[String, Any]]
  ): Double =

    var total: Double = 0.0
    var i: Int = 0

    // Recorrido estrictamente iterativo.
    while i < transactions.length do

      val transaction = transactions(i)

      // Obtener los campos necesarios.
      val status = transaction("status").asInstanceOf[String]

      val amount: Double = transaction("amount") match
        case number: java.lang.Number => number.doubleValue()
        case _ => 0.0

      // Filtrar y aplicar la comisión.
      if status == "approved" && amount > 0.0 then
        val netAmount = amount * 0.9
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