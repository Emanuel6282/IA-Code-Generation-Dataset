// P03: Flatten Nested List — Scala
// Solo biblioteca estandar de Scala.

object Flatten {

  /**
   * Dada una lista arbitrariamente anidada de enteros, retorna una lista
   * plana con todos los enteros en el mismo orden de aparicion.
   *
   * En Scala, al igual que en Java, no existe una forma estatica simple de
   * expresar "una lista que puede contener enteros o mas listas anidadas"
   * sin recurrir a Any; se modela como List[Any], donde cada elemento es,
   * en tiempo de ejecucion, un Int (una "hoja") o una List[Any] (otro nivel
   * de anidamiento). Se usa pattern matching para distinguir ambos casos.
   *
   * @param nested Lista que puede contener Int y/o otras List[Any]
   *               anidadas a cualquier profundidad.
   * @return Una List[Int] plana con todos los enteros en el mismo orden de
   *         aparicion. Si la lista de entrada esta vacia, retorna una
   *         lista vacia.
   */
  def flatten(nested: List[Any]): List[Int] = {
    nested.flatMap {
      case sublista: List[_] => flatten(sublista.asInstanceOf[List[Any]])
      case entero: Int => List(entero)
      case _ => List.empty[Int]
    }
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