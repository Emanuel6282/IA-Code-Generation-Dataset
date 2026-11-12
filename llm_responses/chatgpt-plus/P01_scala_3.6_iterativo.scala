import scala.collection.mutable

object WordFrequency:

  def wordFrequency(
      words: mutable.ListBuffer[String]
  ): mutable.Map[String, Int] =

    // Mapa mutable para almacenar las frecuencias
    val frequencies = mutable.Map.empty[String, Int]

    // Índice mutable para recorrer la lista
    var i = 0

    // Recorrido estrictamente iterativo
    while i < words.length do
      val word = words(i)

      // Incrementar la frecuencia o inicializarla en 1
      if frequencies.contains(word) then
        frequencies(word) = frequencies(word) + 1
      else
        frequencies(word) = 1

      i += 1

    frequencies

  def main(args: Array[String]): Unit =

    // Test 1: Palabras repetidas
    val words1 = mutable.ListBuffer("the", "fox", "the")
    val expected1 = mutable.Map("the" -> 2, "fox" -> 1)

    assert(wordFrequency(words1) == expected1)

    // Test 2: Lista vacía
    val words2 = mutable.ListBuffer.empty[String]

    assert(wordFrequency(words2).isEmpty)

    // Test 3: Una sola palabra
    val words3 = mutable.ListBuffer("hello")
    val expected3 = mutable.Map("hello" -> 1)

    assert(wordFrequency(words3) == expected3)

    println("Todos los tests pasaron correctamente.")