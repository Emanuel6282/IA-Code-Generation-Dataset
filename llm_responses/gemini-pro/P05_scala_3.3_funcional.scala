// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis Basada en Indentación (Braceless Syntax): Scala 3 abraza un diseño más declarativo y minimalista, eliminando las llaves {} en favor de la indentación significativa.
// 2. Punto de entrada @main: Genera automáticamente el código necesario para ejecutar la aplicación, eliminando el "boilerplate" del clásico `object` con `def main`.
// 3. Pipeline Inmutable Estricto: Ausencia total de `var` o iteradores imperativos. Todo el flujo se modela como una transformación de colecciones inmutables (`List`).
// 4. Funciones de Orden Superior y Plegado (foldLeft): Se delega el control de flujo a `filter` y `map`. La acumulación del resultado se realiza mediante `foldLeft`, propagando el estado hacia adelante sin mutaciones colaterales y protegiendo el flujo con un inicializador seguro (0.0).

case class Transaction(id: Int, amount: Double, status: String, category: String)

def processTransactions(transactions: List[Transaction]): Double =
  // Construcción de la tubería de datos (pipeline) puramente funcional
  val total = transactions
    // 1. Filter: Retiene transacciones válidas sin alterar la lista original
    .filter(tx => tx.status == "approved" && tx.amount > 0)
    
    // 2. Map: Proyecta los montos aplicando la comisión (identidad matemática)
    .map(tx => tx.amount * 0.9)
    
    // 3. FoldLeft: Reduce el flujo a un único valor. 
    // (_ + _) es la notación concisa en Scala para (acc, val) => acc + val
    .foldLeft(0.0)(_ + _)
    
  // Evaluación final y redondeo preservando la transparencia referencial
  Math.round(total * 100.0) / 100.0


// Scala 3.3: Directiva moderna para la ejecución del programa
@main def runFunctionalTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: filtrado inmutable correcto y aplicación de comisión
  val test1 = List(
    Transaction(1, 100.0, "approved", "General"),
    Transaction(2, 200.0, "rejected", "General")
  )
  assert(processTransactions(test1) == 90.0, "Error: Caso base esperado 90.0")

  // 2. Casos límite: lista vacía (prueba la robustez del foldLeft) y montos negativos/nulos
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

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")