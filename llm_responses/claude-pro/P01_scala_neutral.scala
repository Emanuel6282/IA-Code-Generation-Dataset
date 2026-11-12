object WordFrequency {

  /** Retorna un mapa que asocia cada palabra única con su número de apariciones.
    * Si la lista está vacía, retorna un mapa vacío.
    */
  def wordFrequency(words: List[String]): Map[String, Int] =
    words.groupMapReduce(identity)(_ => 1)(_ + _)

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado
    assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))

    // Caso 2: lista vacía
    assert(wordFrequency(List.empty) == Map.empty)

    // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
    assert(wordFrequency(List("a", "A", "a", "b", "a")) == Map("a" -> 3, "A" -> 1, "b" -> 1))

    println("Todas las pruebas pasaron.")
  }
}