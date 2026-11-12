import scala.collection.mutable

object WordFrequency {

  /** Retorna un mapa que asocia cada palabra única con su número de apariciones.
    * Si la lista está vacía, retorna un mapa vacío.
    */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    val frequencies = mutable.Map.empty[String, Int]
    val it = words.iterator
    while (it.hasNext) {
      val word = it.next()
      // mutable.Map.updateWith (nuevo en Scala 2.13): lee y actualiza la entrada
      // en una sola operación, sin consultar el mapa dos veces.
      frequencies.updateWith(word) {
        case Some(count) => Some(count + 1)
        case None        => Some(1)
      }
    }
    frequencies.toMap // se expone un Map inmutable; el estado mutable queda encapsulado
  }

  def main(args: Array[String]): Unit = {
    val testCases = mutable.ListBuffer.empty[(String, List[String], Map[String, Int])]
    // Caso 1: ejemplo del enunciado
    testCases += (("ejemplo del enunciado", List("the", "fox", "the"), Map("the" -> 2, "fox" -> 1)))
    // Caso 2: lista vacía
    testCases += (("lista vacía", List.empty[String], Map.empty[String, Int]))
    // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
    testCases += (("mayúsculas y repeticiones", List("a", "A", "a", "b", "a"), Map("a" -> 3, "A" -> 1, "b" -> 1)))

    var i = 0
    while (i < testCases.length) {
      val (name, words, expected) = testCases(i)
      val result = wordFrequency(words)
      assert(result == expected, s"Caso '$name': entrada $words, esperado $expected, obtenido $result")
      i += 1
    }

    println("Todas las pruebas pasaron.")
  }
}