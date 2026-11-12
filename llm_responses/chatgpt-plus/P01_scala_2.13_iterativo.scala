import scala.collection.mutable

object WordFrequency {

  def wordFrequency(words: mutable.ListBuffer[String]): mutable.Map[String, Int] = {
    val frequency = mutable.Map[String, Int]()
    var i = 0

    while (i < words.length) {
      val word = words(i)

      if (frequency.contains(word)) {
        frequency(word) = frequency(word) + 1
      } else {
        frequency(word) = 1
      }

      i += 1
    }

    frequency
  }

  def main(args: Array[String]): Unit = {

    // Caso de prueba 1: Palabras repetidas
    val words1 = mutable.ListBuffer("the", "fox", "the")
    val expected1 = mutable.Map("the" -> 2, "fox" -> 1)

    assert(wordFrequency(words1) == expected1)

    // Caso de prueba 2: Lista vacía
    val words2 = mutable.ListBuffer.empty[String]

    assert(wordFrequency(words2).isEmpty)

    // Caso de prueba 3: Una sola palabra
    val words3 = mutable.ListBuffer("hello")
    val expected3 = mutable.Map("hello" -> 1)

    assert(wordFrequency(words3) == expected3)

    println("Todos los tests pasaron correctamente.")
  }
}