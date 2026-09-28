// P03: Flatten Nested List — Scala 2.13 (codigo base para refactoring)
// Estilo: Stack explicita mutable (desafio iterativo), para forzar pattern matching recursivo en el refactor
import scala.collection.mutable

object P03FlattenList_v213 {

  def flatten(nested: Any): List[Int] = {
    val result = mutable.ListBuffer[Int]()
    val stack = mutable.Stack[Any](nested)

    while (stack.nonEmpty) {
      stack.pop() match {
        case list: List[_] =>
          // Agregar en orden inverso para preservar orden
          list.reverse.foreach(stack.push)
        case n: Int =>
          result += n
        case _ => // ignorar otros tipos
      }
    }
    result.toList
  }

  def main(args: Array[String]): Unit = {
    assert(flatten(List(1, List(2,3), List(4, List(5,6)))) == List(1,2,3,4,5,6))
    assert(flatten(List.empty) == List.empty)
    assert(flatten(List(1,2,3)) == List(1,2,3))
    println("All tests passed")
  }
}
