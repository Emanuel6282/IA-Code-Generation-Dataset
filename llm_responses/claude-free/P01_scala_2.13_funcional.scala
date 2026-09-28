// P01: Word Frequency — Scala 2.13 (paradigma funcional)
// Solo biblioteca estandar de Scala.

import scala.annotation.tailrec

object WordFrequency {

  /**
   * Dada una lista de palabras (strings), retorna un mapa que asocia
   * cada palabra unica con el numero de veces que aparece en la lista.
   *
   * Implementacion estrictamente funcional: recursion de cola anotada con
   * @tailrec, sin var ni colecciones mutables. El acumulador es un
   * Map[String, Int] inmutable que se reconstruye (nunca se muta) en cada
   * llamada recursiva mediante el operador "+".
   *
   * @param words Lista de strings.
   * @return Un Map[String, Int] inmutable con la frecuencia de cada palabra.
   *         Si la lista de entrada esta vacia, retorna un mapa vacio.
   */
  def wordFrequency(words: List[String]): Map[String, Int] = {

    @tailrec
    def loop(remaining: List[String], acc: Map[String, Int]): Map[String, Int] =
      remaining match {
        case Nil =>
          acc
        case head :: tail =>
          val nuevaFrecuencia = acc.getOrElse(head, 0) + 1
          loop(tail, acc + (head -> nuevaFrecuencia))
      }

    loop(words, Map.empty[String, Int])
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