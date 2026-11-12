// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sintaxis "Braceless" y "Quiet Control Syntax" Consolidadas: En Scala 3.6, el uso de bloques basados en indentación y palabras clave como `while ... do` e `if ... then` es el estándar absoluto, dejando atrás por completo la verbosidad heredada de Java.
// 2. Control Imperativo Estricto: Para garantizar que el compilador no traduzca internamente las abstracciones a métodos funcionales (como ocurre frecuentemente con los bucles `for` en Scala, que sufren un proceso de "desugaring" hacia `foreach` o `map`), empleamos un control de bajo nivel utilizando `transactions.iterator` y un bucle `while`.
// 3. Mutación Local y Anotación @main: Se utiliza la anotación `@main` introducida en Scala 3 para autogenerar el punto de entrada, y una variable `var total` que acumula el estado de forma explícitamente imperativa en cada ciclo válido.

case class Transaction(id: Int, amount: Double, status: String, category: String)

def processTransactions(transactions: List[Transaction]): Double =
  // 1. Estado local mutable para la acumulación del pipeline
  var total = 0.0
  
  // 2. Extracción del iterador para un control manual del flujo
  val iterator = transactions.iterator
  
  // 3. Iteración imperativa mediante while/do
  while iterator.hasNext do
    val tx = iterator.next()
    
    // 4. Lógica de negocio (filtrado por estado y monto positivo)
    if tx.status == "approved" && tx.amount > 0 then
      total += tx.amount * 0.9
      
  // Retorno final redondeado a 2 decimales usando matemática estándar
  Math.round(total * 100.0) / 100.0


// Punto de entrada autogenerado por Scala 3.6
@main def runIterativeTests36(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: mezcla de aprobadas y rechazadas
  val test1 = List(
    Transaction(1, 100.0, "approved", "General"),
    Transaction(2, 200.0, "rejected", "General")
  )
  assert(processTransactions(test1) == 90.0, "Error: Caso base esperado 90.0")

  // 2. Casos límite: lista vacía y montos negativos/nulos
  assert(processTransactions(List.empty[Transaction]) == 0.0, "Error: Lista vacía esperado 0.0")
  
  val test2 = List(
    Transaction(3, -50.0, "approved", "Refund"),
    Transaction(4, 0.0, "approved", "Test")
  )
  assert(processTransactions(test2) == 0.0, "Error: Montos negativos o cero esperado 0.0")

  // 3. Caso de precisión matemática y acumulación múltiple
  // 10.55 * 0.9 = 9.495
  // 20.11 * 0.9 = 18.099
  // Suma acumulada = 27.594 -> Redondeo a 2 decimales = 27.59
  val test3 = List(
    Transaction(5, 10.55, "approved", "Services"),
    Transaction(6, 20.11, "approved", "Products")
  )
  assert(processTransactions(test3) == 27.59, "Error: Fallo en el cálculo de precisión y redondeo")

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")