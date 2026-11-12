import scala.annotation.tailrec

object WordFrequency {

  /** Retorna un mapa inmutable que asocia cada palabra única con su número de apariciones.
    * Si la lista está vacía, retorna un mapa vacío.
    */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    // @tailrec: el compilador verifica que la recursión es de cola y la convierte en
    // un salto, así que no crece la pila aunque la lista sea muy larga.
    @tailrec
    def count(remaining: List[String], acc: Map[String, Int]): Map[String, Int] =
      remaining match {
        case Nil => acc
        // Map.updatedWith (nuevo en Scala 2.13): devuelve un mapa nuevo con la entrada
        // actualizada, a partir de su valor actual (Option), sin mutar el acumulador.
        case word :: rest => count(rest, acc.updatedWith(word)(current => Some(current.fold(1)(_ + 1))))
      }

    count(words, Map.empty)
  }

  def main(args: Array[String]): Unit = {
    val testCases: List[(String, List[String], Map[String, Int])] = List(
      // Caso 1: ejemplo del enunciado
      ("ejemplo del enunciado", List("the", "fox", "the"), Map("the" -> 2, "fox" -> 1)),
      // Caso 2: lista vacía
      ("lista vacía", List.empty, Map.empty),
      // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
      ("mayúsculas y repeticiones", List("a", "A", "a", "b", "a"), Map("a" -> 3, "A" -> 1, "b" -> 1))
    )

    // Implementaciones de referencia independientes para validar el resultado:
    // una con foldLeft y otra con groupMapReduce (nuevo en Scala 2.13).
    def byFoldLeft(words: List[String]): Map[String, Int] =
      words.foldLeft(Map.empty[String, Int])((acc, word) => acc.updated(word, acc.getOrElse(word, 0) + 1))

    def byGroupMapReduce(words: List[String]): Map[String, Int] =
      words.groupMapReduce(identity)(_ => 1)(_ + _)

    // flatMap + filter + map: por cada caso, Some(mensaje) si falla y None si pasa.
    val failures: List[String] = testCases.flatMap { case (name, words, expected) =>
      Option(wordFrequency(words))
        .filter(result => result != expected || result != byFoldLeft(words) || result != byGroupMapReduce(words))
        .map(result => s"Caso '$name': entrada $words, esperado $expected, obtenido $result")
    }

    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}