import scala.collection.mutable

object Solution {

  /**
   * Scala 2.13 - Paradigma Iterativo Estricto
   * Utiliza colecciones mutables (ListBuffer, ArrayDeque), variables `var` y bucles `while`
   * sin recursión ni combinadores funcionales (map, filter, fold, etc.).
   */
  def flatten(nestedList: List[Any]): List[Int] = {
    val result = mutable.ListBuffer[Int]()
    
    // Pila de trabajo explícita basada en la colección mutable de Scala 2.13
    val stack = mutable.ArrayDeque[Any]()
    
    // Carga inicial
    stack.append(nestedList)

    while (stack.nonEmpty) {
      val current = stack.removeLast()

      current match {
        case list: List[_] =>
          // Iteración usando while e índice mutable para evitar recursión y map/fold
          var i = list.length - 1
          while (i >= 0) {
            stack.append(list(i))
            i -= 1
          }
        case elem: Int =>
          result.append(elem)
        case _ => // Ignorar nulos o tipos no soportados
      }
    }

    result.toList
  }

  def main(args: Array[String]): Unit = {
    // --- Casos de prueba usando assert (Scala 2.13 - Iterativo) ---

    // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
    val test1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
    assert(flatten(test1) == List(1, 2, 3, 4, 5, 6), "Falló el Caso 1")

    // Caso 2: Lista vacía y sublistas vacías intercaladas
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