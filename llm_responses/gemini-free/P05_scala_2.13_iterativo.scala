// Scala 2.13 - Paradigma Iterativo Estricto
// Utiliza acumuladores mutables (var), bucles 'while', mutabilidad explícita y colecciones mutables (scala.collection.mutable)
// Evita estrictamente metodos funcionales como map(), filter(), foldLeft() y recursión.

import scala.collection.mutable
import scala.math.BigDecimal.RoundingMode

object TransactionPipelineIterative {

  /**
   * Procesa una lista de transacciones usando un enfoque iterativo imperativo.
   *
   * @param transactions Lista de mapas mutables que representan las transacciones
   * @return Suma acumulada con la comisión del 10% aplicada, redondeada a 2 decimales
   */
  def processTransactions(transactions: List[mutable.Map[String, Any]]): Double = {
    if (transactions == null || transactions.isEmpty) {
      return 0.0
    }

    var totalAmount: Double = 0.0
    var index: Int = 0
    val size: Int = transactions.length

    // Bucle imperativo while
    while (index < size) {
      val tx = transactions(index)

      if (tx != null) {
        // Extracción manual con casting
        val statusOpt = tx.get("status")
        val amountOpt = tx.get("amount")

        var isApproved = false
        if (statusOpt.isDefined && statusOpt.get != null) {
          if (statusOpt.get.toString.equalsIgnoreCase("approved")) {
            isApproved = true
          }
        }

        if (isApproved && amountOpt.isDefined && amountOpt.get != null) {
          val amountVal = amountOpt.get match {
            case d: Double => d
            case f: Float  => f.toDouble
            case i: Int    => i.toDouble
            case l: Long   => l.toDouble
            case _         => 0.0
          }

          // 1. Filtrar: status == "approved" Y amount > 0
          if (amountVal > 0.0) {
            // 2. Aplicar: comisión del 10% (multiplicar por 0.9)
            val discounted = amountVal * 0.9

            // Acumulación explícita en variable mutable
            totalAmount = totalAmount + discounted
          }
        }
      }

      // Incremento manual del índice
      index = index + 1
    }

    // 3. Retornar la suma total redondeada a 2 decimales
    BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_UP).toDouble
  }

  def main(args: Array[String]): Unit = {
    // Función auxiliar imperativa para construir mapas mutables de prueba
    def createTx(id: Int, amount: Double, status: String, cat: String): mutable.Map[String, Any] = {
      val m = mutable.Map[String, Any]()
      m.put("id", id)
      m.put("amount", amount)
      m.put("status", status)
      m.put("category", cat)
      m
    }

    // CASO DE PRUEBA 1: Ejemplo base (Aprobada vs Rechazada)
    val testList1 = mutable.ListBuffer[mutable.Map[String, Any]]()
    testList1.append(createTx(1, 100.0, "approved", "income"))
    testList1.append(createTx(2, 200.0, "rejected", "food"))

    val result1 = processTransactions(testList1.toList)
    assert(result1 == 90.0, s"TC01 Falló: esperado 90.0 pero fue $result1")

    // CASO DE PRUEBA 2: Lista vacía o sin transacciones válidas (monto negativo / rechazada)
    val testList2Empty = List[mutable.Map[String, Any]]()
    assert(processTransactions(testList2Empty) == 0.0, "TC02a Falló: Lista vacía debe retornar 0.0")

    val testList2Invalid = mutable.ListBuffer[mutable.Map[String, Any]]()
    testList2Invalid.append(createTx(3, -50.0, "approved", "tech"))
    testList2Invalid.append(createTx(4, 150.0, "rejected", "books"))

    val result2 = processTransactions(testList2Invalid.toList)
    assert(result2 == 0.0, s"TC02b Falló: esperado 0.0 pero fue $result2")

    // CASO DE PRUEBA 3: Múltiples válidas y precisión de redondeo HALF_UP
    val testList3 = mutable.ListBuffer[mutable.Map[String, Any]]()
    testList3.append(createTx(5, 100.0, "approved", "tech"))
    testList3.append(createTx(6, 50.55, "approved", "services"))
    testList3.append(createTx(7, 0.0, "approved", "other"))

    // Cálculo: (100 * 0.9) + (50.55 * 0.9) = 90.0 + 45.495 = 135.495 -> HALF_UP -> 135.5
    val result3 = processTransactions(testList3.toList)
    assert(result3 == 135.5, s"TC03 Falló: esperado 135.5 pero fue $result3")

    println("¡Todos los casos de prueba pasaron correctamente con el enfoque iterativo!")
  }
}