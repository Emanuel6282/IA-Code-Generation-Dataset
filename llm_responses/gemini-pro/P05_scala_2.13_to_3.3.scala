// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis Braceless (Indentación Significativa): Se eliminan las llaves {} y se utiliza la indentación para definir los bloques lógicos. Esto reduce drásticamente el ruido visual y alinea Scala con lenguajes como Python.
// 2. Anotación @main: Elimina el "boilerplate" (código repetitivo) de tener que envolver el punto de entrada en un `object` con un método `def main(args: Array[String])`.
// 3. Case Classes vs "Stringly-Typed" Maps: Se reemplaza la estructura dinámica `Map[String, Any]` por un `case class`. En Scala 3, esto garantiza seguridad de tipos absoluta, eliminando los peligrosos `asInstanceOf`.
// 4. Paradigma Funcional (filter -> map -> sum): Se resuelve directamente el desafío funcional, erradicando por completo las variables mutables (`var`) y las banderas imperativas (`isValid`), reemplazándolas por un pipeline declarativo.

// Estructura inmutable fuertemente tipada
case class Transaction(id: Int, amount: Double, status: String, category: String)

def processTransactions(transactions: List[Transaction]): Double =
  // Pipeline puramente funcional
  val total = transactions
    .filter(tx => tx.status == "approved" && tx.amount > 0)
    .map(tx => tx.amount * 0.9)
    .sum
    
  // Redondeo final a 2 decimales
  Math.round(total * 100.0) / 100.0


// Punto de entrada autogenerado y limpio en Scala 3.3
@main def runRefactoredPipeline(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // TC01: filtrar negativo y rechazado, aplicar fee
  val txs1 = List(
    Transaction(1, 100.0, "approved", "income"),
    Transaction(2, -50.0, "approved", "income"),
    Transaction(3, 200.0, "rejected", "income"),
    Transaction(4, 150.0, "approved", "expense")
  )
  assert(Math.abs(processTransactions(txs1) - 225.0) < 0.01, "TC01 fallo")

  // TC02: lista vacia
  assert(processTransactions(List.empty) == 0.0, "TC02 fallo")

  // TC03: un approved
  val txs3 = List(Transaction(1, 1000.0, "approved", "x"))
  assert(Math.abs(processTransactions(txs3) - 900.0) < 0.01, "TC03 fallo")

  println("All tests passed")