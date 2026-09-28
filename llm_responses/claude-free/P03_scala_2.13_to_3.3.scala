// P03: Flatten Nested List — Scala 3.3 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, while ... do, match sin llaves con case alineado por
//    indentacion. Estilo idiomatico recomendado desde Scala 3.0 y
//    consolidado en la version LTS 3.3.
// 2. Definiciones de nivel superior (top-level definitions): flatten ya no
//    necesita estar envuelta en un "object" explicito.
// 3. "@main": reemplaza "object { def main(args: Array[String]): Unit =
//    ... }" por una anotacion sobre una funcion de nivel superior.
// 4. Nota de honestidad sobre el tipo de entrada: Scala 3 introduce union
//    types recursivos (p. ej. "type Nested = Int | List[Nested]"), que
//    hubieran permitido reemplazar el parametro "Any" por un tipo mas
//    preciso. Sin embargo, este ejercicio pide una migracion de version
//    con comportamiento identico, no un cambio de firma o de modelado de
//    tipos; cambiar "Any" por un union type alteraria la firma publica de
//    "flatten" (de "Any" a "List[Nested]" o similar) y excederia el
//    alcance de una simple migracion sintactica. Por eso se conserva
//    "Any" tal cual, aplicando unicamente las mejoras de sintaxis de
//    superficie (indentacion, @main, top-level defs) que no alteran la
//    firma ni el comportamiento.
// 5. Nota de honestidad sobre el paradigma: el codigo base es
//    deliberadamente imperativo con Stack mutable explicita ("para forzar
//    pattern matching recursivo en el refactor"), pero el pattern
//    matching ya esta presente en el codigo base (para distinguir
//    List[_]/Int/otros); se conserva intacto junto con la logica mutable
//    de Stack/ListBuffer, respetando el requisito de comportamiento
//    identico.

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
//    compatibles en Scala 3.3 tal como estaban escritos en 2.13.