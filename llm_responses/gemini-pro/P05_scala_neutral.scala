// Definición de la estructura inmutable de la transacción
case class Transaction(id: Int, amount: Double, status: String, category: String)

object TransactionPipeline {

  /**
   * Procesa una lista de transacciones filtrando las aprobadas y con monto positivo,
   * aplica una comisión del 10% y retorna la suma total redondeada a 2 decimales.
   */
  def processTransactions(transactions: List[Transaction]): Double = {
    // Pipeline declarativo/funcional estándar en Scala
    val total = transactions
      // 1. Filtrar: status == "approved" Y amount > 0
      .filter(tx => tx.status == "approved" && tx.amount > 0)
      // 2. Aplicar: comisión del 10% (multiplicar por 0.9)
      .map(tx => tx.amount * 0.9)
      // Sumar todos los elementos resultantes (retorna 0.0 si la lista está vacía)
      .sum

    // 3. Retornar: suma total redondeada a 2 decimales
    Math.round(total * 100.0) / 100.0
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
    val test1 = List(
      Transaction(1, 100.0, "approved", "General"),
      Transaction(2, 200.0, "rejected", "General")
    )
    assert(processTransactions(test1) == 90.0, "Error en el caso base")

    // 2. Casos límite: lista vacía y montos negativos/nulos
    assert(processTransactions(List.empty[Transaction]) == 0.0, "Error en lista vacía")
    
    val test2 = List(
      Transaction(3, -50.0, "approved", "Refund"),
      Transaction(4, 0.0, "approved", "Test")
    )
    assert(processTransactions(test2) == 0.0, "Error en montos negativos o cero")

    // 3. Caso de precisión matemática y acumulación múltiple
    // 10.55 * 0.9 = 9.495
    // 20.11 * 0.9 = 18.099
    // Suma acumulada = 27.594 -> Redondeo a 2 decimales = 27.59
    val test3 = List(
      Transaction(5, 10.55, "approved", "Services"),
      Transaction(6, 20.11, "approved", "Products")
    )
    assert(processTransactions(test3) == 27.59, "Error en el cálculo de precisión y redondeo")

    println("Todos los casos de prueba pasaron correctamente.")
  }
}