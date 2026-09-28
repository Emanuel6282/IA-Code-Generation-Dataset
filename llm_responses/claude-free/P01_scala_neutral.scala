// P01: Word Frequency — Scala
// Solo biblioteca estandar de Scala.

object WordFrequency {

  /**
   * Dada una lista de palabras (strings), retorna un mapa que asocia
   * cada palabra unica con el numero de veces que aparece en la lista.
   *
   * @param words Lista de strings.
   * @return Un Map[String, Int] con la frecuencia de cada palabra.
   *         Si la lista de entrada esta vacia, retorna un mapa vacio.
   */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    words.groupBy(identity).view.mapValues(_.size).toMap
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado
    assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))

    // Caso 2: lista vacia -> mapa vacio
    assert(wordFrequency(List()) == Map.empty[String, Int])

    // Caso 3: todas las palabras distintas
    assert(wordFrequency(List("a", "b", "c")) == Map("a" -> 1, "b" -> 1, "c" -> 1))

    println("Todos los casos de prueba pasaron correctamente.")
  }
}