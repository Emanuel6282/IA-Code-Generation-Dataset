// P03: Flatten Nested List — Scala 3.6 (paradigma iterativo)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.collection.mutable

def flatten(nested: List[Any]): List[Int] =
  val result: mutable.ListBuffer[Int] = mutable.ListBuffer.empty[Int]
  val stack: mutable.Stack[Any] = mutable.Stack.empty[Any]

  var i: Int = nested.length - 1
  while i >= 0 do
    stack.push(nested(i))
    i -= 1

  while stack.nonEmpty do
    val item = stack.pop()
    item match
      case sublista: List[_] =>
        var j: Int = sublista.length - 1
        while j >= 0 do
          stack.push(sublista(j))
          j -= 1
      case entero: Int =>
        result += entero
      case _ =>
        // Cualquier otro tipo se ignora silenciosamente.

  result.toList

@main def runTests(): Unit =
  // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
  val caso1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
  assert(flatten(caso1) == List(1, 2, 3, 4, 5, 6))

  // Caso 2: lista vacia -> lista vacia
  assert(flatten(List.empty[Any]) == List.empty[Int])

  // Caso 3: lista plana sin anidamiento
  val caso3: List[Any] = List(1, 2, 3)
  assert(flatten(caso3) == List(1, 2, 3))

  println("Todos los casos de prueba pasaron correctamente.")