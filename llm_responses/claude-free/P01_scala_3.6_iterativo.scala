// P01: Word Frequency — Scala 3.6 (paradigma iterativo)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.collection.mutable

def wordFrequency(words: List[String]): Map[String, Int] =
  val result: mutable.Map[String, Int] = mutable.Map.empty[String, Int]
  var index: Int = 0

  while index < words.length do
    val word = words(index)
    if result.contains(word) then
      result(word) = result(word) + 1
    else
      result(word) = 1
    index += 1

  result.toMap

@main def runTests(): Unit =
  // Caso 1: ejemplo del enunciado
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))

  // Caso 2: lista vacia -> mapa vacio
  assert(wordFrequency(List()) == Map.empty[String, Int])

  // Caso 3: todas las palabras distintas
  assert(wordFrequency(List("a", "b", "c")) == Map("a" -> 1, "b" -> 1, "c" -> 1))

  println("Todos los casos de prueba pasaron correctamente.")