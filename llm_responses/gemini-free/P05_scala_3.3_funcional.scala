// Scala 3.3 LTS - Paradigma Funcional Estricto
// Características de Scala 3 utilizadas:
// 1. Sintaxis basada en sangría sin llaves (Optional Braces).
// 2. Definiciones de extensiones y Pattern Matching exhaustivo.
// 3. Enums y Case Classes inmutables con sintaxis concisa de Scala 3.
// 4. Inmutabilidad estricta: 'val', combinadores (filter, map, foldLeft, flatMap) y recursión de cola (@tailrec).

import scala.annotation.tailrec
import scala.math.BigDecimal.RoundingMode

// Modelo inmutable de dominio en Scala 3
enum TransactionStatus:
  case Approved, Rejected

object TransactionStatus:
  def parse(status: String): Option[TransactionStatus] =
    Option(status).map(_.trim.toLowerCase) match
      case Some("approved") => Some(Approved)
      case Some("rejected") => Some(Rejected)
      case _                => None

case class Transaction(id: Int, amount: Double, status: String, category: String):
  def statusEnum: Option[TransactionStatus] = TransactionStatus.parse(status)

object TransactionPipelineFP:

  /**
   * Procesa una lista de transacciones mediante un pipeline puramente funcional.
   *
   * @param transactions Lista inmutable de transacciones
   * @return Suma total con comisión del 10% aplicada, redondeada a 2 decimales.
   */
  def processTransactions(transactions: List[Transaction]): Double =
    // Uso de Option y flatMap para garantizar la inmutabilidad y evitar valores nulos
    val safeTransactions: List[Transaction] = Option(transactions)
      .toList
      .flatMap(identity)

    // 1. Filtrar: status == "approved" AND amount > 0 (Uso de filter)
    val approvedTransactions = safeTransactions.filter: tx =>
      tx.statusEnum.contains(TransactionStatus.Approved) && tx.amount > 0.0

    // 2. Aplicar: comisión del 10% (multiplicar cada amount por 0.9) (Uso de map)
    val discountedAmounts = approvedTransactions.map(_.amount * 0.9)

    // Suma con recursión de cola pura usando @tailrec
    @tailrec
    def sumTailRec(list: List[Double], acc: Double): Double = list match
      case Nil          => acc
      case head :: tail => sumTailRec(tail, acc + head)

    // Nota: También se puede acumular usando foldLeft expresivamente:
    // val rawTotal = discountedAmounts.foldLeft(0.0)(_ + _)
    val rawTotal = sumTailRec(discountedAmounts, 0.0)

    // 3. Retornar la suma total redondeada a 2 decimales mediante BigDecimal
    BigDecimal(rawTotal)
      .setScale(2, RoundingMode.HALF_UP)
      .toDouble

  /**
   * Sobrecarga funcional útil para procesar diccionarios/mapas inmutables (Map[String, Any])
   * mediante for-comprehensions (flatMap / map).
   */
  def processTransactionsFromMaps(maps: List[Map[String, Any]]): Double =
    val txs = Option(maps).getOrElse(Nil).flatMap: map =>
      for
        id     <- map.get("id").collect { case i: Int => i }
        amount <- map.get("amount").collect {
                    case d: Double => d
                    case f: Float  => f.toDouble
                    case i: Int    => i.toDouble
                  }
        status <- map.get("status").collect { case s: String => s }
        cat    =  map.get("category").collect { case c: String => c }.getOrElse("")
      yield Transaction(id, amount, status, cat)

    processTransactions(txs)


@main def runTests(): Unit =
  import TransactionPipelineFP._

  // CASO DE PRUEBA 1: Filtrar montos negativos y rechazados, aplicando comisión
  val tc1 = List(
    Transaction(1, 100.0, "approved", "income"),
    Transaction(2, -50.0, "approved", "income"),
    Transaction(3, 200.0, "rejected", "income"),
    Transaction(4, 150.0, "approved", "expense")
  )
  // Cálculo: (100.0 * 0.9) + (150.0 * 0.9) = 90.0 + 135.0 = 225.0
  val result1 = processTransactions(tc1)
  assert(Math.abs(result1 - 225.0) < 0.001, s"TC01 Falló: esperado 225.0 pero fue $result1")

  // CASO DE PRUEBA 2: Lista vacía o sin transacciones válidas
  val tc2Empty = List.empty[Transaction]
  assert(processTransactions(tc2Empty) == 0.0, "TC02a Falló: Lista vacía debe retornar 0.0")

  val tc2Invalid = List(
    Transaction(5, -100.0, "approved", "tech"),
    Transaction(6, 200.0, "rejected", "books")
  )
  assert(processTransactions(tc2Invalid) == 0.0, "TC02b Falló: Sin válidas debe retornar 0.0")

  // CASO DE PRUEBA 3: Ejemplo del enunciado y precisión de redondeo HALF_UP
  val tc3 = List(
    Transaction(1, 100.0, "approved", "income"),
    Transaction(2, 200.0, "rejected", "income"),
    Transaction(3, 50.55, "approved", "services")
  )
  // Cálculo: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
  val result3 = processTransactions(tc3)
  assert(result3 == 135.5, s"TC03 Falló: esperado 135.5 pero fue $result3")

  println("¡Todos los casos de prueba pasaron correctamente en Scala 3.3 con el paradigma funcional estricto!")