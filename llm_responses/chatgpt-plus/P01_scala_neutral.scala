object WordFrequency {

  def wordFrequency(words: List[String]): Map[String, Int] = {
    words.groupBy(identity).map {
      case (word, occurrences) => word -> occurrences.size
    }
  }

  def main(args: Array[String]): Unit = {

    // Caso de prueba 1: Palabras repetidas
    assert(
      wordFrequency(List("the", "fox", "the")) ==
      Map("the" -> 2, "fox" -> 1)
    )

    // Caso de prueba 2: Lista vacía
    assert(
      wordFrequency(List.empty[String]) == Map.empty[String, Int]
    )

    // Caso de prueba 3: Una sola palabra
    assert(
      wordFrequency(List("hello")) == Map("hello" -> 1)
    )

    println("Todos los tests pasaron correctamente.")
  }
}