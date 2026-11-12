// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Colecciones Inmutables: Scala 2.13 introdujo una reescritura masiva de su arquitectura de colecciones (colecciones 2.13) mejorando el rendimiento de las operaciones inmutables. Aquí usamos `List` que garantiza la inmutabilidad estructural.
// 2. Funciones de Orden Superior (Higher-Order Functions): Construcción de un pipeline declarativo puro usando `filter` y `map`. No hay efectos secundarios, cada operación retorna una nueva colección inmutable o un nuevo valor.
// 3. Plegado Funcional Puro (foldLeft): En lugar de mutar un acumulador con un bucle o usar el atajo `.sum`, empleamos `foldLeft(0.0)(_ + _)`. Esto demuestra el principio matemático fundamental de la programación funcional: propagar el estado hacia adelante a través de argumentos de función de forma inmutable, usando un valor inicial explícito (0.0).
// 4. Transparencia Referencial y 'val': No existen variables ('var'). Todo el flujo lógico es determinista y no altera ningún estado externo.

case class Transaction(id: Int, amount: Double, status: String, category: String)

object TransactionPipeline_Functional {

  /**
   * Procesa una lista de transacciones mediante un pipeline estrictamente funcional.
   */
  def processTransactions(transactions: List[Transaction]): Double = {
    // Pipeline inmutable de procesamiento de datos
    val total: Double = transactions
      // 1. Filter: Predicado puro (sin efectos secundarios)
      .filter(tx => tx.status == "approved" && tx.amount > 0)
      
      // 2. Map: Proyección del monto con la comisión (pureza referencial)
      .map(tx => tx.amount * 0.9)
      
      // 3. FoldLeft: Reducción del iterador combinando los resultados.
      // Comienza con un acumulador en 0.0, protegiendo matemáticamente contra listas vacías.
      .foldLeft(0.0)(_ + _)

    // Retorno final aplicando el redondeo como una transformación matemática
    Math.round(total * 100.0) / 100.0
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: filtrado inmutable correcto y aplicación de comisión
    val test1 = List(
      Transaction(1, 100.0, "approved", "General"),
      Transaction(2, 200.0, "rejected", "General")
    )
    assert(processTransactions(test1) == 90.0, "Error: Caso base funcional esperado 90.0")

    // 2. Casos límite: lista vacía (prueba el foldLeft) y montos negativos/nulos
    assert(processTransactions(List.empty[Transaction]) == 0.0, "Error: Lista vacía esperado 0.0")
    
    val test2 = List(
      Transaction(3, -50.0, "approved", "Refund"),
      Transaction(4, 0.0, "approved", "Test")
    )
    assert(processTransactions(test2) == 0.0, "Error: Montos negativos o cero esperado 0.0")

    // 3. Caso de precisión matemática y acumulación funcional múltiple
    // 10.55 * 0.9 = 9.495
    // 20.11 * 0.9 = 18.099
    // Suma reducida = 27.594 -> Redondeo = 27.59
    val test3 = List(
      Transaction(5, 10.55, "approved", "Services"),
      Transaction(6, 20.11, "approved", "Products")
    )
    assert(processTransactions(test3) == 27.59, "Error: Fallo en el cálculo de precisión y redondeo")

    println("Todos los tests funcionales en Scala 2.13 pasaron correctamente.")
  }
}