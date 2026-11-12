// P05: Transaction Pipeline — migrado a Scala 3.6
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Pipeline funcional extraer -> filter -> map -> sum sobre un iterador, en lugar de
//    `for` + `var total` + bandera `var isValid` + `if` anidados: era el objetivo
//    declarado del código base ("encadenar filter -> map -> sum"). El iterador hace una
//    sola pasada, sin crear listas intermedias.
// 2. Desempaquetado de parámetros en lambdas (Scala 3): `.filter((status, amount) => ...)`
//    recibe la tupla ya separada en sus componentes, sin `{ case (s, a) => ... }`.
// 3. Sintaxis sin llaves (indentación significativa, Scala 3): la estructura la da la
//    indentación.
// 4. Definiciones de nivel superior y @main (Scala 3): processTransactions no necesita
//    un `object` contenedor y el punto de entrada no necesita `def main(args: ...)`.
//
// Nota sobre las novedades de Scala 3.6 (verificado con el compilador 3.6.4):
// - Tuplas con nombre: encajarían para el par extraído, (status = ..., amount = ...),
//   pero siguen siendo experimentales en 3.6 (requieren -experimental), así que se usa
//   una tupla normal con desempaquetado de parámetros. runtimeChecked: también
//   experimental.
// - Estable: context bounds con nombre y nueva sintaxis de given (SIP-64). Sirven para
//   abstracciones con type classes; aquí no hay parámetros de tipo que abstraer.
// Tampoco se usan boundary/break (hay que recorrer toda la lista), ni una case class ni
// pattern matching sobre los valores del Map, porque cambiarían la firma pública o el
// comportamiento con datos mal formados (ver el cambio 3 al final).

def processTransactions(transactions: List[Map[String, Any]]): Double =
  val total = transactions.iterator
    .map(tx => (tx("status").asInstanceOf[String], tx("amount").asInstanceOf[Double]))
    .filter((status, amount) => status == "approved" && amount > 0)
    .map((_, amount) => amount * 0.9)
    .sum
  Math.round(total * 100.0) / 100.0

def tx(id: Int, amount: Double, status: String, cat: String): Map[String, Any] =
  Map("id" -> id, "amount" -> amount, "status" -> status, "category" -> cat)

@main def P05TransactionPipeline_v36(): Unit =
  val txs1 = List(
    tx(1, 100.0, "approved", "income"),
    tx(2, -50.0, "approved", "income"),
    tx(3, 200.0, "rejected", "income"),
    tx(4, 150.0, "approved", "expense")
  )
  assert(Math.abs(processTransactions(txs1) - 225.0) < 0.01)
  assert(processTransactions(List.empty) == 0.0)
  assert(Math.abs(processTransactions(List(tx(1, 1000.0, "approved", "x"))) - 900.0) < 0.01)
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6
// 1. `for` + `var total` + `var isValid` + dos `if` anidados -> pipeline sin estado
//    mutable. La bandera desaparece: la condición del filtro es la misma expresión
//    (`status == "approved" && amount > 0`) que antes se repartía en dos niveles.
// 2. Mismo resultado al bit: `sum` acumula los mismos productos en el mismo orden que el
//    bucle original (0.0 + x == x para los montos positivos que llegan a sumarse), y se
//    conserva `Math.round(total * 100.0) / 100.0`.
// 3. Se conservan los `asInstanceOf` y se extraen status y amount de CADA transacción
//    antes de filtrar, igual que hacía el original al principio de cada vuelta. Así,
//    los datos mal formados fallan igual: una clave ausente lanza
//    NoSuchElementException y un amount que no es Double (por ejemplo, un Int) lanza
//    ClassCastException, aunque la transacción esté rechazada. Un `&&` que leyera
//    amount solo si status es "approved" habría dejado pasar esos casos.
// 4. `.iterator` antes de map/filter/map: evaluación perezosa en una sola pasada. Las
//    excepciones aparecen en la misma transacción que en el original.
// 5. `object P05TransactionPipeline_v213 { ... }` -> definiciones de nivel superior, y
//    `def main(args: Array[String])` -> `@main def P05TransactionPipeline_v36()`.
// 6. Llaves -> sintaxis por indentación, el estilo recomendado en Scala 3.
// 7. Se actualizó la cabecera: el código base decía "Scala 2.13".
// 8. Los tres asserts y el mensaje "All tests passed" no cambian. Comportamiento
//    verificado contra el original con miles de listas aleatorias.
//
// NOTA SOBRE PRECISIÓN (no se cambia para respetar el comportamiento idéntico): el
// original, y por tanto esta migración, calcula con Double y redondea con
// Math.round(total * 100) / 100. Con dinero eso da resultados incorrectos en algunos
// casos: por ejemplo, para amount = 1.15 el valor exacto es 1.035, que debería
// redondearse a 1.04, pero devuelve 1.03. Para corregirlo habría que calcular con
// BigDecimal y setScale(2, RoundingMode.HALF_UP), lo que cambiaría esos resultados.