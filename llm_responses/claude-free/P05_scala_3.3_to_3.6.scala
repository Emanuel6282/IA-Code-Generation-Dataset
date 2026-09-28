// P05: Transaction Pipeline — Scala 3.6 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, for ... do, if ... then. Consolidada desde Scala 3.0 y
//    vigente como estilo idiomatico en toda la serie 3.x, incluida 3.6.
// 2. Definiciones de nivel superior (top-level definitions): processTransactions
//    y el metodo helper "tx" ya no necesitan estar envueltos en un "object"
//    explicito.
// 3. "@main": reemplaza "object { def main(args: Array[String]): Unit =
//    ... }" por una anotacion sobre una funcion de nivel superior.
// 4. Nota de honestidad sobre el tipo de dato: el codigo base modela cada
//    transaccion como "Map[String, Any]" con "asInstanceOf[String]" y
//    "asInstanceOf[Double]" para extraer los campos, en lugar de una case
//    class tipada. Introducir aqui "case class Transaction(...)" cambiaria
//    la firma publica del metodo y la forma de construir los casos de
//    prueba, excediendo el alcance de una migracion de version con
//    "comportamiento identico". Por eso se conserva intencionalmente
//    "Map[String, Any]" con los mismos "asInstanceOf".
// 5. Nota de honestidad sobre novedades especificas de 3.6: revise el
//    changelog de esta version (rama posterior a la LTS 3.3): sus cambios
//    se concentran en refinamientos del *safe initialization checker*,
//    ajustes de inferencia de tipos y mejoras internas de rendimiento del
//    compilador, sin nueva sintaxis de superficie que aplique a este
//    algoritmo de filtrado y agregacion con Map/asInstanceOf. Por ello,
//    las caracteristicas "nuevas" aprovechadas aqui son las de Scala 3 en
//    general (indentacion significativa, top-level defs, @main), vigentes
//    en 3.6.
// 6. Nota de honestidad sobre el paradigma: el codigo base es
//    deliberadamente imperativo (var total, var isValid, for anidado con
//    banderas) "para forzar el desafio funcional en el refactor", pero
//    este ejercicio pide una migracion de version con comportamiento
//    identico, no un cambio de paradigma. Se conserva intencionalmente el
//    estilo imperativo con var/for/bandera "isValid", ya que alterar esa
//    logica excederia el alcance de una simple migracion sintactica.

def processTransactions(transactions: List[Map[String, Any]]): Double =
  var total = 0.0
  for tx <- transactions do
    val status = tx("status").asInstanceOf[String]
    val amount = tx("amount").asInstanceOf[Double]
    var isValid = false
    if status == "approved" then
      if amount > 0 then
        isValid = true
    if isValid then
      total = total + amount * 0.9
  Math.round(total * 100.0) / 100.0

def tx(id: Int, amount: Double, status: String, cat: String): Map[String, Any] =
  Map("id" -> id, "amount" -> amount, "status" -> status, "category" -> cat)

@main def runTests(): Unit =
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

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se elimino el "object P05TransactionPipeline_v213 { ... }" que
//    envolvia todo el codigo, aprovechando las definiciones de nivel
//    superior de Scala 3. Reduce anidamiento e indentacion innecesaria sin
//    alterar el comportamiento.
// 2. Se convirtio la sintaxis con llaves a sintaxis por indentacion
//    significativa: "def processTransactions(...) = { ... }" -> sin
//    llaves, "for (tx <- transactions) { ... }" -> "for tx <- transactions
//    do ...", "if (status == \"approved\") { if (amount > 0) { ... } }" ->
//    "if status == \"approved\" then if amount > 0 then ...". Estilo
//    idiomatico vigente en Scala 3.6.
// 3. Se reemplazo "def main(args: Array[String]): Unit = { ... }" dentro
//    del object por "@main def runTests(): Unit = ..." a nivel superior.
// 4. La logica interna se mantuvo exactamente igual: mismo "Map[String,
//    Any]" con "asInstanceOf", misma variable "total", misma bandera
//    "isValid" con la doble comprobacion anidada, y el mismo redondeo
//    final con "Math.round(total * 100.0) / 100.0" — cumpliendo el
//    requisito de comportamiento identico y respetando la intencion
//    original del codigo base (imperativo).
// 5. El nombre de la funcion "processTransactions", el metodo helper "tx",
//    y los tres casos de prueba con "assert" se conservaron sin cambios
//    logicos, ya que ya eran validos y compatibles en Scala 3.6 tal como
//    estaban escritos en 2.13.