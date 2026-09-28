// Scala 2.13 - Paradigma Funcional Estricto
// Aplica inmutabilidad total (val, List inmutable, Case Class), combinadores funcionales
// (filter, map, flatMap, foldLeft) y recursión de cola optimizada (@tailrec).

import scala.annotation.tailrec
import scala.math.BigDecimal.RoundingMode

// Modelo inmutable representado con un Case Class
case class Transaction(id: Int, amount: Double, status: String, category: String)

object TransactionPipelineFP {

  /**
   * Procesa una lista de transacciones mediante un pipeline puramente funcional.
   *
   * @param transactions Lista inmutable de objetos Transaction
   * @return Suma de montos aprobados con el 10% de comisión aplicado, redondeada a 2 decimales.
   */
  def processTransactions(transactions: List[Transaction]): Double = {
    // Transformación funcional de lista nula o vacía usando Option y flatMap
    val validTransactions: List[Transaction] = Option(transactions)
      .toList
      .flatMap(identity)

    // 1. Filtrar: status == "approved" AND amount > 0 (uso de filter)
    val approvedTransactions: List[Transaction] = validTransactions.filter { tx =>
      Option(tx).exists(t => "approved".equalsIgnoreCase(t.status) && t.amount > 0.0)
    }

    // 2. Aplicar comisión del 10% (multiplicar cada amount por 0.9) (uso de map)
    val discountedAmounts: List[Double] = approvedTransactions.map(tx => tx.amount * 0.9)

    // Acumulación recursiva de cola garantizada mediante la anotación @tailrec
    @tailrec
    def sumTailRec(list: List[Double], accumulator: Double): Double = list match {
      case Nil          => accumulator
      case head :: tail => sumTailRec(tail, accumulator + head)
    }

    // Nota: También se puede expresar alternativamente usando foldLeft:
    // val rawTotal = discountedAmounts.foldLeft(0.0)(_ + _)
    val rawTotal: Double = sumTailRec(discountedAmounts, 0.0)

    // 3. Retornar la suma total redondeada a 2 decimales usando BigDecimal
    BigDecimal(rawTotal)
      .setScale(2, RoundingMode.HALF_UP)
      .toDouble
  }

  /**
   * Sobrecarga funcional útil para procesar estructuras de datos tipo Map[String, Any]
   * utilizando for-comprehension (flatMap / map) sin mutabilidad.
   */
  def processTransactionsFromMaps(maps: List[Map[String, Any]]): Double = {
    val txs: List[Transaction] = Option(maps).getOrElse(Nil).flatMap { map =>
      for {
        id     <- map.get("id").collect { case i: Int => i }
        amount <- map.get("amount").collect {
                    case d: Double => d
                    case f: Float  => f.toDouble
                    case i: Int    => i.toDouble
                  }
        status <- map.get("status").collect { case s: String => s }
        cat    =  map.get("category").collect { case c: String => c }.getOrElse("")
      } yield Transaction(id, amount, status, cat)
    }
    processTransactions(txs)
  }

  def main(args: Array[String]): Unit = {
    // CASO DE PRUEBA 1: Filtrar montos negativos y rechazados, aplicando comisión
    val tc1 = List(
      Transaction(1, 100.0, "approved", "income"),
      Transaction(2, -50.0, "approved", "income"),
      Transaction(3, 200.0, "rejected", "income"),
      Transaction(4, 150.0, "approved", "expense")
    )
    // Transacciones válidas: (100.0 * 0.9) + (150.0 * 0.9) = 90.0 + 135.0 = 225.0
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

    // CASO DE PRUEBA 3: Múltiples transacciones válidas y precisión decimal con HALF_UP
    val tc3 = List(
      Transaction(7, 100.0, "approved", "tech"),
      Transaction(8, 50.55, "approved", "services"),
      Transaction(9, 0.0, "approved", "other")
    )
    // Cálculo: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
    val result3 = processTransactions(tc3)
    assert(result3 == 135.5, s"TC03 Falló: esperado 135.5 pero fue $result3")

    println("¡Todos los casos de prueba pasaron correctamente con el paradigma funcional estricto!")
  }
}