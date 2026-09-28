// P05: Transaction Pipeline — Scala 3.6
//
// Características utilizadas:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite expresar bloques de código sin llaves innecesarias.
//
// 2. Colecciones inmutables:
//    List y Map representan las transacciones sin utilizar estado mutable.
//
// 3. filter():
//    Conserva únicamente las transacciones aprobadas con amount > 0.
//
// 4. map():
//    Aplica la comisión del 10% a cada amount válido.
//
// 5. flatMap():
//    Se utiliza para transformar cada resultado en una colección
//    inmutable y mantener todo el procesamiento dentro del pipeline
//    funcional.
//
// 6. foldLeft():
//    Acumula los amounts procesados sin utilizar estado mutable.
//
// 7. @tailrec:
//    Se utiliza en la función auxiliar de redondeo para realizar
//    el cálculo del factor de escala mediante recursión de cola.
//
// No se utilizan var, mutable.*, for ni while.

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

    // TC01: solo se procesan las transacciones approved y positivas
    val transactions1 =
      List(
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

    // TC03: una transacción válida
    val transactions3 =
      List(
        tx(1, 1000.0, "approved", "income")
      )

    assert(
      math.abs(processTransactions(transactions3) - 900.0) < 0.01
    )

    println("All tests passed")

/*
 * Cambios realizados:
 *
 * 1. Se utilizan exclusivamente colecciones inmutables List y Map.
 *
 * 2. El filtrado se realiza mediante filter(), conservando únicamente
 *    las transacciones con status "approved" y amount > 0.
 *
 * 3. La comisión del 10% se aplica mediante map().
 *
 * 4. flatMap() mantiene el procesamiento dentro de un pipeline funcional
 *    y produce una colección inmutable.
 *
 * 5. foldLeft() calcula la suma sin utilizar un acumulador mutable.
 *
 * 6. @tailrec se utiliza en powerOfTen() para realizar la recursión
 *    de manera optimizada como recursión de cola.
 *
 * 7. Se utiliza la sintaxis de indentación de Scala 3.
 *
 * 8. No se utilizan var, mutable.*, for, while ni estado mutable.
 *
 * 9. Una lista vacía o sin transacciones válidas produce 0.0 porque
 *    foldLeft() comienza con el valor inicial 0.0.
 *
 * 10. Se incluyen exactamente tres casos de prueba mediante assert.
 *
 * 11. Solo se utiliza la biblioteca estándar de Scala.
 *
 * 12. No se fuerzan características exclusivas de Scala 3.6 que no
 *     aporten una mejora real al algoritmo. Las características de
 *     Scala 3 utilizadas aquí mantienen el código idiomático y funcional.
 */