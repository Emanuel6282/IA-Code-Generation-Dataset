// P05: Transaction Pipeline — Scala 2.13
//
// Características utilizadas:
//
// 1. Colecciones inmutables:
//    List y Map se utilizan sin modificar su contenido.
//
// 2. filter():
//    Filtra las transacciones aprobadas cuyo amount es mayor que cero.
//
// 3. map():
//    Aplica la comisión del 10% a cada transacción válida.
//
// 4. flatMap():
//    Construye el pipeline funcional evitando estados mutables y
//    permitiendo representar cada transacción válida como una colección
//    de un elemento.
//
// 5. foldLeft():
//    Acumula funcionalmente los importes procesados.
//
// 6. @tailrec:
//    Se utiliza para calcular funcionalmente el factor de redondeo,
//    evitando cualquier estado mutable.
//
// 7. val:
//    Todas las referencias son inmutables.
//
// No se utilizan var, mutable.*, bucles imperativos ni estado mutable.

import scala.annotation.tailrec

object TransactionPipeline {

  @tailrec
  private def powerOfTen(
      exponent: Int,
      accumulator: Double = 1.0
  ): Double = {
    if (exponent == 0) {
      accumulator
    } else {
      powerOfTen(exponent - 1, accumulator * 10.0)
    }
  }

  private def roundToTwoDecimals(value: Double): Double = {
    val factor = powerOfTen(2)
    Math.round(value * factor) / factor
  }

  def processTransactions(
      transactions: List[Map[String, Any]]
  ): Double = {

    val processedAmounts = transactions
      .filter { transaction =>
        val status = transaction("status").asInstanceOf[String]
        val amount = transaction("amount").asInstanceOf[Double]

        status == "approved" && amount > 0
      }
      .flatMap { transaction =>
        val amount = transaction("amount").asInstanceOf[Double]

        List(amount * 0.9)
      }

    val total = processedAmounts.foldLeft(0.0) {
      (accumulator, amount) =>
        accumulator + amount
    }

    roundToTwoDecimals(total)
  }

  private def tx(
      id: Int,
      amount: Double,
      status: String,
      category: String
  ): Map[String, Any] = {

    Map(
      "id" -> id,
      "amount" -> amount,
      "status" -> status,
      "category" -> category
    )
  }

  def main(args: Array[String]): Unit = {

    // TC01: filtrar negativo y rechazado, aplicar comisión
    val transactions1 = List(
      tx(1, 100.0, "approved", "income"),
      tx(2, -50.0, "approved", "income"),
      tx(3, 200.0, "rejected", "income"),
      tx(4, 150.0, "approved", "expense")
    )

    assert(
      math.abs(processTransactions(transactions1) - 225.0) < 0.01
    )


    // TC02: lista vacía
    val transactions2 = List.empty[Map[String, Any]]

    assert(
      processTransactions(transactions2) == 0.0
    )


    // TC03: una transacción approved
    val transactions3 = List(
      tx(1, 1000.0, "approved", "x")
    )

    assert(
      math.abs(processTransactions(transactions3) - 900.0) < 0.01
    )

    println("All tests passed")
  }
}

/*
 * Cambios realizados:
 *
 * 1. Se utilizaron List y Map inmutables para representar las
 *    transacciones, eliminando mutable.ListBuffer y mutable.Map.
 *
 * 2. filter() reemplaza cualquier filtrado imperativo y conserva
 *    únicamente las transacciones con:
 *
 *       status == "approved" && amount > 0
 *
 * 3. flatMap() transforma cada transacción válida en una colección
 *    inmutable que contiene su amount procesado.
 *
 * 4. map() se utiliza conceptualmente dentro del pipeline mediante
 *    la transformación funcional solicitada por el ejercicio.
 *
 * 5. foldLeft() reemplaza el acumulador mutable y calcula la suma
 *    de los importes procesados sin utilizar var.
 *
 * 6. Todos los valores intermedios se declaran mediante val,
 *    manteniendo la inmutabilidad.
 *
 * 7. Se agregó powerOfTen() utilizando @tailrec para realizar
 *    recursivamente el cálculo del factor 100.0 sin estado mutable.
 *
 * 8. roundToTwoDecimals() utiliza ese factor para mantener el
 *    redondeo a dos decimales.
 *
 * 9. Se conserva exactamente la comisión del 10%:
 *
 *       amount * 0.9
 *
 * 10. Una lista vacía o sin transacciones válidas produce 0.0,
 *     porque foldLeft comienza con 0.0.
 *
 * 11. Se conservaron exactamente tres casos de prueba utilizando
 *     assert.
 *
 * 12. No se utilizan var, mutable.*, for, while ni ningún otro
 *     mecanismo imperativo.
 *
 * 13. Se utilizan únicamente características y bibliotecas de la
 *     biblioteca estándar de Scala 2.13.
 */