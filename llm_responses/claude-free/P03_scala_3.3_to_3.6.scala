// P03: Flatten Nested List — Scala 3.6 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, while ... do, match sin llaves con case alineado por
//    indentacion. Consolidada desde Scala 3.0 y vigente como estilo
//    idiomatico en toda la serie 3.x, incluida 3.6.
// 2. Definiciones de nivel superior (top-level definitions): flatten ya no
//    necesita estar envuelta en un "object" explicito.
// 3. "@main": reemplaza "object { def main(args: Array[String]): Unit =
//    ... }" por una anotacion sobre una funcion de nivel superior.
// 4. Nota de honestidad sobre el tipo de entrada: Scala 3 introduce union
//    types recursivos (p. ej. "type Nested = Int | List[Nested]"), que
//    hubieran permitido reemplazar el parametro "Any" por un tipo mas
//    preciso (como se hizo en la version funcional de esta misma serie).
//    Sin embargo, este ejercicio pide una migracion de version con
//    comportamiento identico, no un cambio de firma o de modelado de
//    tipos; cambiar "Any" alteraria la firma publica de "flatten" y
//    excederia el alcance de una simple migracion sintactica. Se conserva
//    "Any" tal cual, aplicando unicamente mejoras de sintaxis de
//    superficie que no alteran la firma ni el comportamiento.
// 5. Nota de honestidad sobre novedades especificas de 3.6: revise el
//    changelog de esta version (rama posterior a la LTS 3.3): sus cambios
//    se concentran en refinamientos del *safe initialization checker*,
//    ajustes de inferencia de tipos y mejoras internas de rendimiento del
//    compilador, sin nueva sintaxis de superficie que aplique a este
//    algoritmo de aplanado con Stack/ListBuffer mutables. Por ello, las
//    caracteristicas "nuevas" aprovechadas aqui son las de Scala 3 en
//    general (indentacion significativa, top-level defs, @main), vigentes
//    en 3.6.
// 6. Nota de honestidad sobre el paradigma: el codigo base es
//    deliberadamente imperativo con Stack mutable explicita; el pattern
//    matching ya presente (List[_]/Int/otros) se conserva intacto junto
//    con la logica mutable de Stack/ListBuffer, respetando el requisito
//    de comportamiento identico.

import scala.collection.mutable

def flatten(nested: Any): List[Int] =
  val result = mutable.ListBuffer[Int]()
  val stack = mutable.Stack[Any](nested)

  while stack.nonEmpty do
    stack.pop() match
      case list: List[_] =>
        // Agregar en orden inverso para preservar orden
        list.reverse.foreach(stack.push)
      case n: Int =>
        result += n
      case _ => // ignorar otros tipos

  result.toList

@main def runTests(): Unit =
  assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))
  assert(flatten(List.empty) == List.empty)
  assert(flatten(List(1, 2, 3)) == List(1, 2, 3))
  println("All tests passed")

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se elimino el "object P03FlattenList_v213 { ... }" que envolvia todo
//    el codigo, aprovechando las definiciones de nivel superior de Scala 3.
//    Reduce anidamiento e indentacion innecesaria sin alterar el
//    comportamiento.
// 2. Se convirtio la sintaxis con llaves a sintaxis por indentacion
//    significativa: "def flatten(...) = { ... }" -> sin llaves, "while
//    (stack.nonEmpty) { ... }" -> "while stack.nonEmpty do ...", el bloque
//    "match { case ... }" -> "match" seguido de "case" alineados por
//    indentacion, sin llaves.
// 3. Se reemplazo "def main(args: Array[String]): Unit = { ... }" dentro
//    del object por "@main def runTests(): Unit = ..." a nivel superior.
// 4. La logica interna se mantuvo exactamente igual: misma Stack mutable
//    inicializada con el elemento raiz, mismo pattern matching
//    (list/Int/otros), mismo uso de "list.reverse.foreach(stack.push)"
//    para preservar el orden de aparicion al usar una pila LIFO, y el
//    mismo ListBuffer como acumulador convertido a List al final —
//    cumpliendo el requisito de comportamiento identico y respetando la
//    intencion original del codigo base (imperativo con Stack/ListBuffer
//    mutables).
// 5. El nombre de la funcion "flatten" y los tres casos de prueba con
//    "assert" se conservaron sin cambios logicos, ya que ya eran validos y
//    compatibles en Scala 3.6 tal como estaban escritos en 2.13.