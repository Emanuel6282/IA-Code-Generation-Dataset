// P05: Transaction Pipeline — Scala 3.3
//
// Características utilizadas:
//
// 1. Sintaxis de Scala 3:
//    Se utiliza la sintaxis de indentación de Scala 3 para expresar
//    bloques de código sin llaves innecesarias.
//
// 2. Colecciones inmutables:
//    List y Map se utilizan sin estado mutable.
//
// 3. filter():
//    Conserva únicamente las transacciones aprobadas con amount > 0.
//
// 4. map():
//    Aplica la comisión del 10% a los amounts válidos.
//
// 5. flatMap():
//    Mantiene el pipeline funcional y permite representar cada importe
//    procesado como una colección inmutable.
//
// 6. foldLeft():
//    Acumula los amounts procesados sin utilizar estado mutable.
//
// 7. @tailrec:
//    Se utiliza para calcular el factor de redondeo de forma recursiva
//    y segura para la pila.
//
// No se utilizan var, mutable.*, for, while ni estado mutable.

import scala.annotation.tailrec

object TransactionPipeline:

  @tailrec
  private def powerOfTen(
      exponent: Int,
      accumulator: Double = 1.0
  ): Double =
    if exponent == 0 then
      accumulator
    else
      powerOfTen(exponent - 1, accumulator * 10.0)

  private def roundToTwoDecimals(value: Double): Double =
    val factor = powerOfTen(2)
    Math.round(value * factor) / factor

  def processTransactions(
      transactions: List[Map[String, Any]]
  ): Double =

    val processedAmounts =
      transactions
        .filter { transaction =>
          val status = transaction("status").asInstanceOf[String]
          val amount = transaction("amount").asInstanceOf[Double]

          status == "approved" && amount > 0
        }
        .map { transaction =>
          val amount = transaction("amount").asInstanceOf[Double]

          amount * 0.9
        }
        .flatMap { amount =>
          List(amount)
        }

    val total =
      processedAmounts.foldLeft(0.0) { (accumulator, amount) =>
        accumulator + amount
      }

    roundToTwoDecimals(total)

  private def tx(
      id: Int,
      amount: Double,
      status: String,
      category: String
  ): Map[String, Any] =
    Map(
      "id" -> id,
      "amount" -> amount,
      "status" -> status,
      "category" -> category
    )

  def main(args: Array[String]): Unit =

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
    val transactions2 =
      List.empty[Map[String, Any]]

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

/*
 * Cambios realizados:
 *
 * 1. Se utilizan List y Map inmutables, eliminando completamente
 *    mutable.ListBuffer y mutable.Map.
 *
 * 2. filter() realiza el filtrado de forma declarativa:
 *
 *       status == "approved" && amount > 0
 *
 * 3. map() aplica la comisión del 10%:
 *
 *       amount * 0.9
 *
 * 4. flatMap() forma parte explícita del pipeline funcional solicitado.
 *    Cada amount procesado se representa como una List de un elemento.
 *
 * 5. foldLeft() reemplaza cualquier acumulador mutable y calcula
 *    la suma de los importes de forma funcional.
 *
 * 6. Todos los valores se declaran mediante val, manteniendo
 *    la inmutabilidad.
 *
 * 7. @tailrec se utiliza en powerOfTen() para calcular el factor
 *    utilizado por el redondeo sin utilizar estado mutable.
 *
 * 8. La sintaxis de indentación de Scala 3 elimina llaves innecesarias
 *    y es una característica idiomática del lenguaje Scala 3.
 *
 * 9. El redondeo conserva la misma lógica:
 *
 *       Math.round(total * 100.0) / 100.0
 *
 * 10. Una lista vacía o sin transacciones válidas produce 0.0,
 *     ya que foldLeft comienza con 0.0.
 *
 * 11. Se conservaron exactamente tres casos de prueba mediante assert.
 *
 * 12. No se utilizan var, mutable.*, for ni while.
 *
 * 13. No se utilizan librerías de terceros; todo pertenece a la
 *     biblioteca estándar de Scala 3.3.
 */