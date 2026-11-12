// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Sintaxis Braceless (Indentación Significativa): En Scala 3.6, la omisión de llaves {} es el estándar idiomático absoluto. Reduce el ruido visual drásticamente y estructura el código jerárquicamente a través de la indentación.
// 2. Anotación @main: Elimina por completo el "boilerplate" de definir un `object` con un método `main(args: Array[String])`. El compilador genera el punto de entrada automáticamente.
// 3. Tipado Estático vs Diccionarios Dinámicos: Se descarta el uso de `Map[String, Any]` heredado del código base. En su lugar, se define un `case class` ligero que proporciona seguridad de tipos en tiempo de compilación y elimina los riesgosos `asInstanceOf`.
// 4. Pipeline Funcional Declarativo: Se resuelve el desafío planteado encadenando `filter -> map -> sum`, erradicando las variables mutables y las banderas lógicas imperativas.

// Definición de la entidad financiera fuertemente tipada
case class Transaction(id: Int, amount: Double, status: String, category: String)

def processTransactions(transactions: List[Transaction]): Double =
  // Transformación inmutable del flujo de datos
  val total = transactions
    .filter(tx => tx.status == "approved" && tx.amount > 0)
    .map(tx => tx.amount * 0.9)
    .sum
    
  // Retorno con la precisión decimal requerida
  Math.round(total * 100.0) / 100.0


// Scala 3.6: Punto de entrada simplificado
@main def runPipelineRefactor36(): Unit =
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