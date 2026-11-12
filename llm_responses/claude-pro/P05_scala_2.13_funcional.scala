import scala.annotation.tailrec
import scala.math.BigDecimal.RoundingMode

object TransactionPipeline {

  /** Transacción financiera inmutable. `category` tiene valor por defecto porque el
    * enunciado la lista como campo, pero su ejemplo la omite y la función no la usa.
    */
  final case class Transaction(id: Int, amount: Double, status: String, category: String = "")

  private val CommissionFactor = BigDecimal("0.9")   // comisión del 10 %

  /** Paso 1 (filtro): aprobada y con monto positivo. */
  private def isValid(t: Transaction): Boolean = t.status == "approved" && t.amount > 0

  /** Paso 2 (transformación): monto tras la comisión. BigDecimal(d) de Scala parte de la
    * representación decimal del Double (1.15 -> 1.15), no de su valor binario exacto.
    */
  private def netAmount(t: Transaction): BigDecimal = BigDecimal(t.amount) * CommissionFactor

  /** Redondeo comercial a 2 decimales: los .5 se redondean hacia arriba (1.035 -> 1.04). */
  private def toCents(total: BigDecimal): Double = total.setScale(2, RoundingMode.HALF_UP).toDouble

  /** Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
    * una comisión del 10 %, y retorna el total redondeado a 2 decimales.
    *
    * Es un pipeline puro que corresponde uno a uno con el enunciado:
    * filter (paso 1) -> map (paso 2) -> foldLeft (paso 3). Los cálculos se hacen con
    * BigDecimal para evitar los errores de representación de Double: por ejemplo,
    * 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con Double daría 1.03.
    */
  def processTransactions(transactions: List[Transaction]): Double =
    toCents(
      transactions
        .filter(isValid)
        .map(netAmount)
        .foldLeft(BigDecimal(0))(_ + _)   // valor inicial 0: lista vacía -> 0.0
    )

  def main(args: Array[String]): Unit = {
    val testCases: List[(String, List[Transaction], Double)] = List(
      // Caso 1: ejemplo del enunciado, tal cual (sin category)
      ("ejemplo del enunciado",
        List(Transaction(1, 100.0, "approved"), Transaction(2, 200.0, "rejected")), 90.0),
      // Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
      ("sin transacciones válidas",
        List(Transaction(1, 50.0, "rejected", "food"), Transaction(2, 0.0, "approved", "food"),
          Transaction(3, -30.0, "approved", "refund")), 0.0),
      // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
      ("redondeo de 1.035", List(Transaction(1, 1.15, "approved", "fees")), 1.04),
      ("suma con decimales",
        List(Transaction(1, 0.3, "approved", "food"), Transaction(2, 0.2, "approved", "food"),
          Transaction(3, 99.99, "rejected", "food")), 0.45)
    )

    assert(processTransactions(List.empty) == 0.0, "lista vacía -> 0.0")

    // Referencia 1, con @tailrec: recorre la lista acumulando el total en un parámetro.
    // El compilador garantiza que la recursión es de cola y no consume pila.
    def byTailrec(transactions: List[Transaction]): Double = {
      @tailrec
      def loop(rest: List[Transaction], acc: BigDecimal): BigDecimal = rest match {
        case Nil                       => acc
        case t :: tail if isValid(t)   => loop(tail, acc + netAmount(t))
        case _ :: tail                 => loop(tail, acc)
      }
      toCents(loop(transactions, BigDecimal(0)))
    }

    // Referencia 2, con flatMap + Option.when (nuevo en Scala 2.13): cada transacción
    // aporta Some(monto neto) si es válida o None si no, y flatMap descarta los None.
    def byFlatMap(transactions: List[Transaction]): Double =
      toCents(transactions.flatMap(t => Option.when(isValid(t))(netAmount(t))).sum)

    // map + filter + map: calcula cada resultado, conserva los incorrectos (frente al
    // valor esperado o a cualquiera de las dos referencias) y los convierte en mensajes.
    val failures: List[String] = testCases
      .map { case (name, txs, expected) => (name, txs, expected, processTransactions(txs)) }
      .filter { case (_, txs, expected, result) =>
        result != expected || result != byTailrec(txs) || result != byFlatMap(txs)
      }
      .map { case (name, _, expected, result) =>
        f"Caso '$name': esperado $expected%.2f, obtenido $result%.2f"
      }

    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}