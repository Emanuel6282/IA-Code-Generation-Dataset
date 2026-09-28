import scala.annotation.tailrec

object Solution {

  /**
   * Scala 2.13 - Paradigma Funcional Estricto
   * Implementación inmutable que combina foldLeft y recursión de cola (@tailrec)
   * para desanidar cualquier estructura sin efectos secundarios ni variables mutables (var).
   */
  def flatten(nestedList: List[Any]): List[Int] = {
    @tailrec
    def process(acc: List[Int], remaining: List[Any]): List[Int] = remaining match {
      case Nil => acc
      case (head: List[_]) :: tail => process(acc, head ::: tail)
      case (head: Int) :: tail     => process(acc :+ head, tail)
      case _ :: tail               => process(acc, tail)
    }

    process(Nil, nestedList)
  }

  def main(args: Array[String]): Unit = {
    // --- Casos de prueba usando assert (Scala 2.13 - Funcional) ---

    // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
    val test1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
    assert(flatten(test1) == List(1, 2, 3, 4, 5, 6), "Falló el Caso 1")

    // Caso 2: Lista vacía y estructuras con sublistas vacías intercaladas
    val test2a: List[Any] = Nil
    val test2b: List[Any] = List(List(), List(List()), List(1, List()), List(List(2)), List())
    assert(flatten(test2a) == Nil, "Falló el Caso 2a (Lista vacía)")
    assert(flatten(test2b) == List(1, 2), "Falló el Caso 2b (Sublistas vacías)")

    // Caso 3: Lista totalmente plana y lista profundamente anidada
    val test3a: List[Any] = List(10, 20, 30)
    val test3b: List[Any] = List(List(List(List(List(99)))))
    assert(flatten(test3a) == List(10, 20, 30), "Falló el Caso 3a (Lista ya plana)")
    assert(flatten(test3b) == List(99), "Falló el Caso 3b (Profundamente anidada)")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}