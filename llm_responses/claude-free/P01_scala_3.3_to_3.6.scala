// P01: Word Frequency — Scala 3.6 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, for ... do, if ... then. Consolidada desde Scala 3.0 y
//    vigente como estilo idiomatico en toda la serie 3.x, incluida 3.6.
// 2. Definiciones de nivel superior (top-level definitions): wordFrequency y
//    el punto de entrada ya no necesitan un "object" explicito que los
//    envuelva.
// 3. "@main": reemplaza "object { def main(args: Array[String]): Unit = ... }"
//    por una anotacion sobre una funcion de nivel superior, generando el
//    punto de entrada de forma mas concisa.
// 4. Nota de honestidad sobre novedades especificas de 3.6: revise el
//    changelog de esta version (rama posterior a la LTS 3.3): sus cambios se
//    concentran en refinamientos del *safe initialization checker*, ajustes
//    de inferencia de tipos y mejoras internas de rendimiento del compilador,
//    sin nueva sintaxis de superficie que aplique a un problema tan acotado
//    como este. Por ello, las caracteristicas "nuevas" aprovechadas aqui son
//    las de Scala 3 en general (indentacion significativa, top-level defs,
//    @main), que siguen siendo el estilo idiomatico vigente en 3.6, sin
//    inventar el uso de mejoras del compilador que no tienen expresion en el
//    codigo fuente.
// 5. Nota de honestidad sobre el paradigma: el codigo base es deliberadamente
//    imperativo (mutable.Map + for) "para forzar el desafio funcional en el
//    refactor", pero este ejercicio pide una migracion de version con
//    comportamiento identico, no un cambio de paradigma. Por eso se conserva
//    intencionalmente el estilo imperativo con mutable.Map y bucle for,
//    actualizando unicamente la sintaxis a la idiomatica de Scala 3.6.

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
//    el codigo, aprovechando las definiciones de nivel superior de Scala 3.
//    Reduce anidamiento e indentacion innecesaria sin alterar el
//    comportamiento.
// 2. Se convirtio la sintaxis con llaves a sintaxis por indentacion
//    significativa: "def wordFrequency(...) = { ... }" -> sin llaves,
//    "for (word <- words) { ... }" -> "for word <- words do ...",
//    "if (result.contains(word)) { ... } else { ... }" -> "if
//    result.contains(word) then ... else ...". Estilo idiomatico vigente en
//    Scala 3.6.
// 3. Se reemplazo "def main(args: Array[String]): Unit = { ... }" dentro del
//    object por "@main def runTests(): Unit = ..." a nivel superior.
// 4. La logica interna se mantuvo exactamente igual: mismo mutable.Map como
//    acumulador, mismo bucle for, misma condicion contains/actualizacion, y
//    misma conversion final con .toMap — cumpliendo el requisito de
//    comportamiento identico y respetando la intencion imperativa original
//    del codigo base.
// 5. El nombre de la funcion "wordFrequency" y los tres casos de prueba con
//    "assert" se conservaron sin cambios logicos, ya que ya eran validos y
//    compatibles en Scala 3.6 tal como estaban escritos en 2.13.