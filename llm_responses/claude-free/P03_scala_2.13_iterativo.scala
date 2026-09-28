// P03: Flatten Nested List — Scala 2.13 (paradigma iterativo)
// Solo biblioteca estandar de Scala.

import scala.collection.mutable

object Flatten {

  /**
   * Dada una lista arbitrariamente anidada de enteros, retorna una lista
   * plana con todos los enteros en el mismo orden de aparicion.
   *
   * Implementacion estrictamente iterativa: usa una pila mutable
   * (mutable.Stack) para simular manualmente el descenso por los niveles
   * de anidamiento, en lugar de recursion, y un mutable.ListBuffer como
   * acumulador del resultado. No se usan map(), filter(), foldLeft() ni
   * llamadas recursivas.
   *
   * Se modela la entrada como List[Any], ya que Scala no tiene una forma
   * nativa de expresar "lista de enteros o listas anidadas" con tipado
   * estatico simple; cada elemento se inspecciona con pattern matching.
   *
   * @param nested Lista que puede contener Int y/o otras List[Any]
   *               anidadas a cualquier profundidad.
   * @return Una List[Int] plana con todos los enteros en el mismo orden de
   *         aparicion. Si la lista de entrada esta vacia, retorna una
   *         lista vacia.
   */
  def flatten(nested: List[Any]): List[Int] = {
    val result: mutable.ListBuffer[Int] = mutable.ListBuffer.empty[Int]
    val stack: mutable.Stack[Any] = mutable.Stack.empty[Any]

    // Se insertan los elementos en orden inverso para que, al usar la
    // pila (push/pop, LIFO), se procesen en el orden original.
    var i: Int = nested.length - 1
    while (i >= 0) {
      stack.push(nested(i))
      i -= 1
    }

    while (stack.nonEmpty) {
      val item = stack.pop()
      item match {
        case sublista: List[_] =>
          var j: Int = sublista.length - 1
          while (j >= 0) {
            stack.push(sublista(j))
            j -= 1
          }
        case entero: Int =>
          result += entero
        case _ =>
          // Cualquier otro tipo se ignora silenciosamente.
      }
    }

    result.toList
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
    val caso1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
    assert(flatten(caso1) == List(1, 2, 3, 4, 5, 6))

    // Caso 2: lista vacia -> lista vacia
    assert(flatten(List.empty[Any]) == List.empty[Int])

    // Caso 3: lista plana sin anidamiento
    val caso3: List[Any] = List(1, 2, 3)
    assert(flatten(caso3) == List(1, 2, 3))

    println("Todos los casos de prueba pasaron correctamente.")
  }
}