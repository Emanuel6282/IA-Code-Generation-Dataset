// Scala 3.6 - Paradigma Funcional Estricto
//
// Características de Scala 3.6 utilizadas:
// 1. Sintaxis basada en sangría (Optional Braces) sin llaves.
// 2. Case Classes inmutables con sintaxis moderna.
// 3. Modificadores de métodos y funciones de extensión.
// 4. Inmutabilidad estricta: 'val', combinadores (filter, map, foldLeft, flatMap) y @tailrec.
// 5. Total ausencia de variables mutables (var), bucles o colecciones mutables.

import scala.annotation.tailrec
import scala.math.BigDecimal.RoundingMode

// Modelo inmutable de datos en Scala 3.6
case class Transaction(
  id: Int,
  amount: Double,
  status: String,
  category: String
)

object TransactionPipelineFP:

  /**
   * Procesa una lista de transacciones mediante un pipeline puramente funcional.
   *
   * @param transactions Lista inmutable de transacciones
   * @return Suma de montos aprobados con el 10% de comisión aplicado, redondeada a 2 decimales.
   */
  def processTransactions(transactions: List[Transaction]): Double =
    // Manejo inmutable de posible entrada nula mediante Option y flatMap
    val safeList: List[Transaction] = Option(transactions)
      .toList
      .flatMap(identity)

    // 1. Filtrar: status == "approved" Y amount > 0 (Uso de filter)
    val approvedTransactions: List[Transaction] = safeList.filter: tx =>
      Option(tx).exists(t => "approved".equalsIgnoreCase(t.status) && t.amount > 0.0)

    // 2. Aplicar: comisión del 10% (multiplicar cada amount por 0.9) (Uso de map)
    val discountedAmounts: List[Double] = approvedTransactions.map(_.amount * 0.9)

    // Acumulación con recursión de cola garantizada mediante @tailrec
    @tailrec
    def sumTailRec(list: List[Double], accumulator: Double): Double = list match
      case Nil          => accumulator
      case head :: tail => sumTailRec(tail, accumulator + head)

    // Alternativamente se puede sumar con foldLeft: discountedAmounts.foldLeft(0.0)(_ + _)
    val rawTotal: Double = sumTailRec(discountedAmounts, 0.0)

    // 3. Retornar la suma total de los amounts resultantes, redondeada a 2 decimales
    BigDecimal(rawTotal)
      .setScale(2, RoundingMode.HALF_UP)
      .toDouble

  /**
   * Sobrecarga funcional para procesar diccionarios/mapas inmutables (Map[String, Any])
   * mediante for-comprehension (flatMap / map).
   */
  def processTransactionsFromMaps(maps: List[Map[String, Any]]): Double =
    val txs: List[Transaction] = Option(maps).getOrElse(Nil).flatMap: map =>
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

  // PRUEBA 1: Ejemplo base del enunciado (Aprobada vs Rechazada)
  val tc1 = List(
    Transaction(1, 100.0, "approved", "income"),
    Transaction(2, 200.0, "rejected", "income")
  )
  val result1 = processTransactions(tc1)
  assert(result1 == 90.0, s"TC01 Falló: esperado 90.0 pero fue $result1")

  // PRUEBA 2: Lista vacía o sin transacciones válidas (monto negativo o rechazadas)
  val tc2Empty = List.empty[Transaction]
  assert(processTransactions(tc2Empty) == 0.0, "TC02a Falló: Lista vacía debe retornar 0.0")

  val tc2Invalid = List(
    Transaction(3, -50.0, "approved", "tech"),
    Transaction(4, 150.0, "rejected", "books")
  )
  assert(processTransactions(tc2Invalid) == 0.0, "TC02b Falló: Sin válidas debe retornar 0.0")

  // PRUEBA 3: Múltiples válidas y precisión de redondeo decimal (HALF_UP)
  val tc3 = List(
    Transaction(5, 100.0, "approved", "tech"),
    Transaction(6, 50.55, "approved", "services"),
    Transaction(7, 0.0, "approved", "other")
  )
  // Cálculo: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
  val result3 = processTransactions(tc3)
  assert(result3 == 135.5, s"TC03 Falló: esperado 135.5 pero fue $result3")

  println("¡Todos los casos de prueba pasaron correctamente en Scala 3.6 bajo el paradigma funcional estricto!")