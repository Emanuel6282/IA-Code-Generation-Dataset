import scala.annotation.tailrec

object WordFrequency {

  def wordFrequency(words: List[String]): Map[String, Int] = {

    // Procesamiento funcional con colecciones inmutables
    val processedWords = words
      .filter(_ => true)
      .map(identity)
      .flatMap(word => List(word))

    // Conteo mediante acumulación inmutable
    val frequencies = processedWords.foldLeft(Map.empty[String, Int]) {
      (acc, word) =>
        acc.updated(word, acc.getOrElse(word, 0) + 1)
    }

    // Función recursiva de cola para construir el resultado
    @tailrec
    def buildResult(
      remaining: List[(String, Int)],
      result: Map[String, Int]
    ): Map[String, Int] = {
      remaining match {
        case Nil => result
        case (word, count) :: tail =>
          buildResult(tail, result.updated(word, count))
      }
    }

    buildResult(frequencies.toList, Map.empty[String, Int])
  }

  def main(args: Array[String]): Unit = {

    // Caso de prueba 1: Palabras repetidas
    assert(
      wordFrequency(List("the", "fox", "the")) ==
      Map("the" -> 2, "fox" -> 1)
    )

    // Caso de prueba 2: Lista vacía
    assert(
      wordFrequency(List.empty[String]) ==
      Map.empty[String, Int]
    )

    // Caso de prueba 3: Una sola palabra
    assert(
      wordFrequency(List("hello")) ==
      Map("hello" -> 1)
    )

    println("Todos los tests pasaron correctamente.")
  }
}