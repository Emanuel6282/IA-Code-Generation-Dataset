import scala.annotation.tailrec

object Flatten {

  /** Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.
    * Cada elemento debe ser un Int o, a su vez, una List anidada.
    *
    * @throws IllegalArgumentException si algún elemento no es Int ni List
    */
  def flatten(nested: List[Any]): List[Int] = {
    // @tailrec: el compilador garantiza que la recursión es de cola y la convierte en
    // un salto, así que funciona con cualquier nivel de anidamiento sin desbordar la
    // pila. El estado (pendientes y acumulador) son listas inmutables que se pasan de
    // una llamada a la siguiente en lugar de modificarse.
    @tailrec
    def loop(pending: List[Any], acc: List[Int]): List[Int] =
      pending match {
        case Nil                    => acc.reverse                 // se acumuló al revés
        case (sub: List[_]) :: rest => loop(sub ::: rest, acc)     // sublista -> su contenido
        case (n: Int) :: rest       => loop(rest, n :: acc)        // entero -> a la salida
        case other :: _             => throw new IllegalArgumentException(s"Elemento no válido: $other")
      }

    loop(nested, Nil)
  }

  def main(args: Array[String]): Unit = {
    // Anidamiento muy profundo (una recursión que no sea de cola desbordaría la pila),
    // construido con foldLeft en lugar de un bucle.
    val deep: Any = (1 to 100000).foldLeft[Any](List(7))((acc, _) => List(acc))

    val testCases: List[(String, List[Any], List[Int])] = List(
      // Caso 1: ejemplo del enunciado
      ("ejemplo del enunciado", List(1, List(2, 3), List(4, List(5, 6))), List(1, 2, 3, 4, 5, 6)),
      // Caso 2: lista vacía y sublistas vacías anidadas
      ("listas vacías", List(Nil, List(Nil), List(Nil, List(Nil))), List.empty),
      // Caso 3: anidamiento muy profundo
      ("anidamiento profundo", List(0, deep, 8), List(0, 7, 8))
    )

    assert(flatten(List.empty) == List.empty, "lista vacía -> lista vacía")

    // map + filter + map: calcula cada resultado, conserva los incorrectos y los
    // convierte en mensajes.
    val wrongResults: List[String] = testCases
      .map { case (name, nested, expected) => (name, expected, flatten(nested)) }
      .filter { case (_, expected, result) => result != expected }
      .map { case (name, expected, result) => s"Caso '$name': esperado $expected, obtenido $result" }

    // flatMap + Option.when (nuevo en Scala 2.13) + foldLeft: comprobación adicional de
    // que la suma de los enteros aplanados coincide con la suma esperada, calculada por
    // otro camino.
    val wrongSums: List[String] = testCases.flatMap { case (name, nested, expected) =>
      val sum = flatten(nested).foldLeft(0)(_ + _)
      val expectedSum = expected.foldLeft(0)(_ + _)
      Option.when(sum != expectedSum)(s"Caso '$name': suma $sum, esperada $expectedSum")
    }

    val failures = wrongResults ++ wrongSums
    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}