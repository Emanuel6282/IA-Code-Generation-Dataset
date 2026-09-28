// P01: Word Frequency — Scala 3.6 (paradigma funcional)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.annotation.tailrec

def wordFrequency(words: List[String]): Map[String, Int] =

  @tailrec
  def loop(remaining: List[String], acc: Map[String, Int]): Map[String, Int] =
    remaining match
      case Nil =>
        acc
      case head :: tail =>
        val nuevaFrecuencia = acc.getOrElse(head, 0) + 1
        loop(tail, acc + (head -> nuevaFrecuencia))

  loop(words, Map.empty[String, Int])

@main def runTests(): Unit =
  // Caso 1: ejemplo del enunciado
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))

  // Caso 2: lista vacia -> mapa vacio
  assert(wordFrequency(List()) == Map.empty[String, Int])

  // Caso 3: todas las palabras distintas
  assert(wordFrequency(List("a", "b", "c")) == Map("a" -> 1, "b" -> 1, "c" -> 1))

  println("Todos los casos de prueba pasaron correctamente.")