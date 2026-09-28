object Solution {

  /**
   * Dada una lista arbitrariamente anidada de enteros, retorne una lista plana
   * con todos los enteros en el mismo orden de aparición utilizando pattern matching.
   */
  def flatten(nestedList: List[Any]): List[Int] = nestedList match {
    case Nil => Nil
    case (head: List[_]) :: tail => flatten(head) ::: flatten(tail)
    case (head: Int) :: tail     => head :: flatten(tail)
    case _ :: tail               => flatten(tail)
  }

  def main(args: Array[args.type]): Unit = {
    // --- Casos de prueba usando assert ---

    // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
    val test1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
    assert(flatten(test1) == List(1, 2, 3, 4, 5, 6), "Falló el Caso 1")

    // Caso 2: Lista vacía y sublistas vacías intercaladas
    val test2a: List[Any] = List()
    val test2b: List[Any] = List(List(), List(List()), List(1, List()), List(List(2)), List())
    assert(flatten(test2a) == List(), "Falló el Caso 2a (Lista vacía)")
    assert(flatten(test2b) == List(1, 2), "Falló el Caso 2b (Sublistas vacías)")

    // Caso 3: Lista totalmente plana y lista profundamente anidada
    val test3a: List[Any] = List(10, 20, 30)
    val test3b: List[Any] = List(List(List(List(List(99)))))
    assert(flatten(test3a) == List(10, 20, 30), "Falló el Caso 3a")
    assert(flatten(test3b) == List(99), "Falló el Caso 3b")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}