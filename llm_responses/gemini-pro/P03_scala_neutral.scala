object FlattenNestedList {

  /**
   * Toma una lista arbitrariamente anidada (representada como List[Any] en Scala)
   * y retorna una lista plana unidimensional de enteros.
   */
  def flatten(nestedList: List[Any]): List[Int] = {
    nestedList.flatMap {
      case i: Int     => List(i)             // Si es un entero, lo envuelve en una lista
      case l: List[_] => flatten(l)          // Si es una lista, se aplana recursivamente
      case _          => Nil                 // Ignora elementos nulos o de otros tipos
    }
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: múltiples niveles de anidamiento
    val nested1 = List(1, List(2, 3), List(4, List(5, 6)))
    val expected1 = List(1, 2, 3, 4, 5, 6)
    assert(flatten(nested1) == expected1, "Error: Falló el caso base del ejemplo")

    // 2. Caso límite: lista completamente vacía
    val nested2 = List()
    val expected2 = List()
    assert(flatten(nested2) == expected2, "Error: Una lista vacía debería retornar una lista vacía")

    // 3. Caso extremo: anidamiento profundo asimétrico con listas vacías en el medio
    val nested3 = List(1, List(), List(List(List(3))))
    val expected3 = List(1, 3)
    assert(flatten(nested3) == expected3, "Error: Falló al procesar listas vacías internas")

    println("Todos los casos de prueba en Scala pasaron correctamente.")
  }
}