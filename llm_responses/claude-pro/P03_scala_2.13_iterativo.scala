import scala.collection.mutable

object Flatten {

  /** Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.
    * Cada elemento debe ser un Int o, a su vez, una List anidada.
    *
    * Usa una pila explícita de iteradores en lugar de recursión, así que funciona con
    * cualquier nivel de anidamiento sin desbordar la pila.
    *
    * @throws IllegalArgumentException si algún elemento no es Int ni List
    */
  def flatten(nested: List[Any]): List[Int] = {
    val result = mutable.ListBuffer.empty[Int]
    // mutable.ArrayDeque (nuevo en Scala 2.13): buffer circular con inserción y
    // eliminación O(1) en ambos extremos; aquí se usa como pila.
    val stack = mutable.ArrayDeque[Iterator[Any]](nested.iterator)
    while (stack.nonEmpty) {
      val current = stack.last
      if (!current.hasNext) {
        stack.removeLast()                        // el nivel actual se agotó: vuelve al anterior
      } else {
        current.next() match {
          case sub: List[_] => stack.append(sub.iterator)  // baja un nivel; este se retoma después
          case n: Int       => result += n
          case other        => throw new IllegalArgumentException(s"Elemento no válido: $other")
        }
      }
    }
    result.toList
  }

  def main(args: Array[String]): Unit = {
    // Anidamiento muy profundo (una versión recursiva desbordaría la pila).
    var deep: Any = List(7)
    var level = 0
    while (level < 100000) {
      deep = List(deep)
      level += 1
    }

    val testCases = mutable.ListBuffer.empty[(String, List[Any], List[Int])]
    // Caso 1: ejemplo del enunciado
    testCases += (("ejemplo del enunciado", List(1, List(2, 3), List(4, List(5, 6))), List(1, 2, 3, 4, 5, 6)))
    // Caso 2: lista vacía y sublistas vacías anidadas
    testCases += (("listas vacías", List(Nil, List(Nil), List(Nil, List(Nil))), List.empty[Int]))
    // Caso 3: anidamiento muy profundo
    testCases += (("anidamiento profundo", List(0, deep, 8), List(0, 7, 8)))

    assert(flatten(List.empty) == List.empty, "lista vacía -> lista vacía")

    var i = 0
    while (i < testCases.length) {
      val (name, nested, expected) = testCases(i)
      val result = flatten(nested)
      assert(result == expected, s"Caso '$name': esperado $expected, obtenido $result")
      i += 1
    }

    println("Todas las pruebas pasaron.")
  }
}