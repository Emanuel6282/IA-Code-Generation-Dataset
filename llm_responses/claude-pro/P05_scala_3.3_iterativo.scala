import scala.collection.mutable
import scala.math.BigDecimal.RoundingMode

/** Transacción financiera inmutable. `category` tiene valor por defecto porque el
  * enunciado la lista como campo, pero su ejemplo la omite y la función no la usa.
  */
final case class Transaction(id: Int, amount: Double, status: String, category: String = "")

private val CommissionFactor = BigDecimal("0.9")   // comisión del 10 %

/** Suma los montos de las transacciones aprobadas con monto positivo, tras descontar
  * una comisión del 10 %, y retorna el total redondeado a 2 decimales.
  *
  * Definición de nivel superior (Scala 3): no necesita un `object` contenedor.
  *
  * Los cálculos se hacen con BigDecimal para evitar los errores de representación de
  * Double: por ejemplo, 1.15 * 0.9 = 1.035 debe redondear a 1.04, pero con Double el
  * producto vale un poco menos y math.round(x * 100) / 100.0 devuelve 1.03.
  */
def processTransactions(transactions: List[Transaction]): Double =
  var total = BigDecimal(0)
  val it = transactions.iterator
  while it.hasNext do
    val t = it.next()
    // 1. Filtrar
    if t.status == "approved" && t.amount > 0 then
      // 2. Aplicar la comisión. BigDecimal(d) de Scala parte de la representación
      //    decimal del Double (1.15 -> 1.15), no de su valor binario exacto.
      val net = BigDecimal(t.amount) * CommissionFactor
      // 3. Acumular
      total += net
  // Redondeo comercial: los .5 se redondean hacia arriba (1.035 -> 1.04).
  total.setScale(2, RoundingMode.HALF_UP).toDouble

/** Caso de prueba como case class. */
case class TestCase(name: String, transactions: List[Transaction], expected: Double)

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = mutable.ListBuffer.empty[TestCase]
  // Caso 1: ejemplo del enunciado, tal cual (sin category)
  testCases += TestCase("ejemplo del enunciado",
    List(Transaction(1, 100.0, "approved"), Transaction(2, 200.0, "rejected")), 90.0)
  // Caso 2: lista sin transacciones válidas (rechazada, cero, negativa)
  testCases += TestCase("sin transacciones válidas",
    List(Transaction(1, 50.0, "rejected", "food"), Transaction(2, 0.0, "approved", "food"),
      Transaction(3, -30.0, "approved", "refund")), 0.0)
  // Caso 3: decimales y redondeo (1.15 * 0.9 = 1.035 -> 1.04; 0.3 y 0.2 -> 0.45)
  testCases += TestCase("redondeo de 1.035", List(Transaction(1, 1.15, "approved", "fees")), 1.04)
  testCases += TestCase("suma con decimales",
    List(Transaction(1, 0.3, "approved", "food"), Transaction(2, 0.2, "approved", "food"),
      Transaction(3, 99.99, "rejected", "food")), 0.45)

  assert(processTransactions(Nil) == 0.0, "lista vacía -> 0.0")

  var i = 0
  while i < testCases.length do
    val TestCase(name, transactions, expected) = testCases(i)
    val result = processTransactions(transactions)
    assert(result == expected, f"Caso '$name': esperado $expected%.2f, obtenido $result%.2f")
    i += 1

  println("Todas las pruebas pasaron.")