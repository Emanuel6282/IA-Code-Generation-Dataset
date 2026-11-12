// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sintaxis Basada en Indentación (Braceless Syntax): Scala 3.3 permite omitir las llaves {} en métodos y clases, utilizando la indentación para definir los bloques de código, reduciendo la verbosidad estructural y acercándose visualmente a Python.
// 2. Estructuras de Control Limpias ("Quiet Syntax"): Se reemplazan los paréntesis y llaves tradicionales de Scala 2 por las nuevas palabras clave `while ... do` e `if ... then`. Esto moderniza el flujo de control iterativo.
// 3. Anotación @main: Elimina la necesidad de crear un `object` envolvente con un método `def main(args: Array[String])`. El compilador de Scala 3 genera automáticamente el punto de entrada.
// 4. Paradigma Iterativo Puro: Se utiliza un iterador explícito, un bucle `while` y una variable local mutable (`var total`) para gestionar el estado, evitando el uso encubierto de funciones de orden superior asociadas al bucle `for` (que en Scala se traduce internamente a `foreach`, `map` o `filter`).

case class Transaction(id: Int, amount: Double, status: String, category: String)

def processTransactions(transactions: List[Transaction]): Double =
  // Estado mutable local para la acumulación iterativa
  var total = 0.0
  
  // Instanciación del iterador para recorrer la colección imperativamente
  val iterator = transactions.iterator
  
  // Scala 3: Bucle while limpio (sin paréntesis ni llaves)
  while iterator.hasNext do
    val tx = iterator.next()
    
    // Scala 3: Condicional if limpio usando 'then'
    if tx.status == "approved" && tx.amount > 0 then
      total += tx.amount * 0.9
      
  // Retorno implícito con redondeo matemático a 2 decimales
  Math.round(total * 100.0) / 100.0


// Scala 3: Punto de entrada simplificado
@main def runIterativeTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: filtrado correcto y aplicación de comisión
  val test1 = List(
    Transaction(1, 100.0, "approved", "General"),
    Transaction(2, 200.0, "rejected", "General")
  )
  assert(processTransactions(test1) == 90.0, "Error: Caso base esperado 90.0")

  // 2. Casos límite: lista vacía y transacciones con montos negativos/nulos
  assert(processTransactions(List.empty[Transaction]) == 0.0, "Error: Lista vacía esperado 0.0")
  
  val test2 = List(
    Transaction(3, -50.0, "approved", "Refund"),
    Transaction(4, 0.0, "approved", "Test")
  )
  assert(processTransactions(test2) == 0.0, "Error: Montos negativos o cero esperado 0.0")

  // 3. Caso de precisión matemática y acumulación múltiple
  // 10.55 * 0.9 = 9.495
  // 20.11 * 0.9 = 18.099
  // Suma iterativa acumulada = 27.594 -> Redondeo = 27.59
  val test3 = List(
    Transaction(5, 10.55, "approved", "Services"),
    Transaction(6, 20.11, "approved", "Products")
  )
  assert(processTransactions(test3) == 27.59, "Error: Fallo en el cálculo de precisión y redondeo")

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")