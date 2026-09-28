// P01: Word Frequency — Scala 3.3 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, for ... do, if ... then. Es el estilo idiomatico recomendado
//    desde Scala 3.0 y consolidado en la version LTS 3.3, en lugar del estilo
//    con llaves heredado de Scala 2.
// 2. Definiciones de nivel superior (top-level definitions): la funcion
//    wordFrequency y el punto de entrada ya no necesitan estar envueltos en
//    un "object" explicito, ya que Scala 3 permite definir funciones y
//    valores directamente en el archivo.
// 3. "@main": reemplaza el tradicional "object { def main(args: Array[String]):
//    Unit = ... }" por la anotacion @main sobre una funcion, que genera el
//    punto de entrada del programa de forma mas concisa.
// 4. Nota de honestidad: el codigo base es deliberadamente imperativo
//    (mutable.Map + for) para "forzar el desafio funcional en el refactor",
//    pero este ejercicio pide migracion de version manteniendo el mismo
//    comportamiento y logica, no un cambio de paradigma. Por eso se conserva
//    intencionalmente el estilo imperativo con mutable.Map y bucle for,
//    solo actualizando la sintaxis a la idiomatica de Scala 3.3 (sin llaves,
//    top-level, @main). No se fuerza aqui un rediseño funcional, ya que eso
//    corresponderia a un ejercicio distinto (de paradigma), no de migracion
//    de version con comportamiento identico.

import scala.collection.mutable

def wordFrequency(words: List[String]): Map[String, Int] =
  val result = mutable.Map[String, Int]()
  for word <- words do
    if result.contains(word) then
      result(word) = result(word) + 1
    else
      result(word) = 1
  result.toMap

@main def runTests(): Unit =
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))
  assert(wordFrequency(List.empty) == Map.empty)
  assert(wordFrequency(List("hello")) == Map("hello" -> 1))
  println("All tests passed")

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se elimino el "object P01WordFrequency_v213 { ... }" que envolvia todo
//    el codigo, ya que Scala 3 permite definiciones de nivel superior
//    (funciones sueltas en el archivo). Esto reduce anidamiento e
//    indentacion innecesaria sin cambiar el comportamiento.
// 2. Se convirtio la sintaxis con llaves a sintaxis por indentacion
//    significativa: "def wordFrequency(...) = { ... }" -> "def
//    wordFrequency(...) = ..." (sin llaves), "for (word <- words) { ... }"
//    -> "for word <- words do ...", "if (result.contains(word)) { ... }
//    else { ... }" -> "if result.contains(word) then ... else ...". Es el
//    estilo idiomatico y recomendado en Scala 3.3.
// 3. Se reemplazo "def main(args: Array[String]): Unit = { ... }" dentro del
//    object por "@main def runTests(): Unit = ..." a nivel superior, usando
//    la caracteristica @main de Scala 3 para generar el punto de entrada del
//    programa de forma mas concisa (el nombre de la funcion anotada, no la
//    clase/object, es lo que determina el comando de ejecucion generado).
// 4. La logica interna se mantuvo exactamente igual: mismo mutable.Map como
//    acumulador, mismo bucle for recorriendo "words", misma condicion
//    contains/actualizacion, y misma conversion final a Map inmutable con
//    .toMap. Esto cumple el requisito de comportamiento identico y respeta
//    la intencion original del codigo base (imperativo con mutable.Map).
// 5. El nombre de la funcion "wordFrequency" y los tres casos de prueba con
//    "assert" se conservaron sin cambios logicos, ya que "assert",
//    "Map.empty" y las comparaciones de igualdad de Map ya eran validas y
//    compatibles en Scala 3.3 tal como estaban escritas en 2.13.