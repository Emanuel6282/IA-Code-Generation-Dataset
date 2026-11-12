// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Colecciones Mutables / Estado Mutable: Scala 2.13 rediseñó por completo su arquitectura de colecciones, pero sigue brindando soporte total al estado mutable (var) y a los iteradores clásicos. En este caso estricto iterativo, evitamos las abstracciones funcionales monádicas.
// 2. Bucle While e Iterador: Para evitar que el compilador transforme internamente un bucle 'for' en operaciones 'foreach' o 'filter' (desugaring), utilizamos explícitamente un 'while' controlado por un iterador de la lista.
// 3. Mutación de Estado Local: Acumulamos el resultado en una variable 'var total' modificándola de forma imperativa en cada paso válido. No hay inmutabilidad, map, filter ni foldLeft.

case class Transaction(id: Int, amount: Double, status: String, category: String)

object TransactionPipeline_Iterative {

  def processTransactions(transactions: List[Transaction]): Double = {
    // 1. Estado mutable para acumular el total
    var total: Double = 0.0
    
    // 2. Obtener un iterador explícito para evitar métodos funcionales de orden superior
    val iterator = transactions.iterator
    
    // 3. Bucle iterativo clásico
    while (iterator.hasNext) {
      val tx = iterator.next()
      
      // Control de flujo imperativo mediante condicionales 'if'
      if (tx.status == "approved" && tx.amount > 0) {
        // Mutación de la variable local acumuladora
        total += tx.amount * 0.9
      }
    }
    
    // 4. Retorno aplicando la regla de redondeo
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
    assert(processTransactions(test1) == 90.0, "Error: Caso base iterativo esperado 90.0")

    // 2. Casos límite: lista vacía y montos negativos/cero
    assert(processTransactions(List.empty[Transaction]) == 0.0, "Error: Lista vacía iterativa esperado 0.0")
    
    val test2 = List(
      Transaction(3, -50.0, "approved", "Refund"),
      Transaction(4, 0.0, "approved", "Test")
    )
    assert(processTransactions(test2) == 0.0, "Error: Montos negativos o cero esperado 0.0")

    // 3. Caso de precisión matemática y acumulación múltiple
    // 10.55 * 0.9 = 9.495
    // 20.11 * 0.9 = 18.099
    // Suma acumulada iterativamente = 27.594 -> Redondeo = 27.59
    val test3 = List(
      Transaction(5, 10.55, "approved", "Services"),
      Transaction(6, 20.11, "approved", "Products")
    )
    assert(processTransactions(test3) == 27.59, "Error: Fallo en el cálculo de precisión y redondeo")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}