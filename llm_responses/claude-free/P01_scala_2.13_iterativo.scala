// P01: Word Frequency — Scala 2.13 (paradigma iterativo)
// Solo biblioteca estandar de Scala.

import scala.collection.mutable

object WordFrequency {

  /**
   * Dada una lista de palabras (strings), retorna un mapa que asocia
   * cada palabra unica con el numero de veces que aparece en la lista.
   *
   * Implementacion estrictamente iterativa: usa un mutable.Map como
   * acumulador y un bucle while con indice manual (var) para recorrer la
   * lista. No se usan map(), filter(), foldLeft() ni recursion.
   *
   * @param words Lista de strings.
   * @return Un Map[String, Int] inmutable con la frecuencia de cada palabra.
   *         Si la lista de entrada esta vacia, retorna un mapa vacio.
   */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    val result: mutable.Map[String, Int] = mutable.Map.empty[String, Int]
    var index: Int = 0

    while (index < words.length) {
      val word = words(index)
      if (result.contains(word)) {
        result(word) = result(word) + 1
      } else {
        result(word) = 1
      }
      index += 1
    }

    result.toMap
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