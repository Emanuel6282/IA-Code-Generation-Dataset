// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis Declarativa "Braceless": Consolidación total del estilo de indentación significativa de Scala 3. La ausencia de llaves {} obliga a estructurar el código de manera más visual y matemática.
// 2. Funciones Anónimas Concisas: Se utiliza la sintaxis `_.amount * 0.9` (placeholder syntax) dentro del `map`, que es idiomática en flujos funcionales cortos para mejorar la legibilidad.
// 3. Plegado Seguro (foldLeft): En lugar de un acumulador iterativo, el estado avanza de forma pura a través de `foldLeft(0.0)(_ + _)`. Esto garantiza el principio de inmutabilidad y previene errores con colecciones vacías mediante el valor semilla (0.0).
// 4. Inmutabilidad por Defecto: Todo el flujo de transformación garantiza la transparencia referencial. Cada paso (`filter`, `map`) genera una nueva colección inmutable intermedia, y `val total` jamás reasigna su valor.

case class Transaction(id: Int, amount: Double, status: String, category: String)

def processTransactions(transactions: List[Transaction]): Double =
  // Pipeline puramente funcional y perezoso en su concepción lógica
  val total = transactions
    // 1. Filter: Evalúa la condición preservando la inmutabilidad de la lista origen
    .filter(tx => tx.status == "approved" && tx.amount > 0)
    
    // 2. Map: Transforma el objeto en el valor comisionado (Double)
    .map(tx => tx.amount * 0.9)
    
    // 3. FoldLeft: Función de orden superior que reduce la colección combinando los montos
    .foldLeft(0.0)(_ + _)
    
  // Retorno garantizando pureza en el cálculo matemático final
  Math.round(total * 100.0) / 100.0


// Punto de entrada autogenerado de Scala 3
@main def runFunctionalTests36(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: filtrado inmutable y aplicación de comisión
  val test1 = List(
    Transaction(1, 100.0, "approved", "General"),
    Transaction(2, 200.0, "rejected", "General")
  )
  assert(processTransactions(test1) == 90.0, "Error: Caso base esperado 90.0")

  // 2. Casos límite: lista vacía (prueba el inicializador de foldLeft) y montos negativos
  assert(processTransactions(List.empty[Transaction]) == 0.0, "Error: Lista vacía esperado 0.0")
  
  val test2 = List(
    Transaction(3, -50.0, "approved", "Refund"),
    Transaction(4, 0.0, "approved", "Test")
  )
  assert(processTransactions(test2) == 0.0, "Error: Montos negativos o cero esperado 0.0")

  // 3. Caso de precisión matemática y acumulación múltiple
  // 10.55 * 0.9 = 9.495
  // 20.11 * 0.9 = 18.099
  // Suma reducida = 27.594 -> Redondeo = 27.59
  val test3 = List(
    Transaction(5, 10.55, "approved", "Services"),
    Transaction(6, 20.11, "approved", "Products")
  )
  assert(processTransactions(test3) == 27.59, "Error: Fallo en el cálculo de precisión y redondeo")

  println("Todos los tests funcionales en Scala 3.6 pasaron correctamente.")