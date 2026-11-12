// Word Frequency — Scala 3.6, paradigma iterativo.
//
// Sobre las novedades de Scala 3.6: las que son estables en esta versión
// (nueva sintaxis de given y de context bounds con `as`, SIP-64) sirven para
// abstracciones con type classes, que este problema no necesita. Las tuplas con
// nombre y `runtimeChecked` todavía son experimentales en 3.6 (requieren
// -experimental), así que no se usan. El código usa los idiomas estables de
// Scala 3: sintaxis sin llaves, definiciones de nivel superior y @main.

import scala.collection.mutable

/** Retorna un mapa que asocia cada palabra única con su número de apariciones.
  * Si la lista está vacía, retorna un mapa vacío.
  */
def wordFrequency(words: List[String]): Map[String, Int] =
  val frequencies = mutable.Map.empty[String, Int]
  val it = words.iterator
  while it.hasNext do
    val word = it.next()
    frequencies(word) = frequencies.getOrElse(word, 0) + 1
  frequencies.toMap // se expone un Map inmutable; el estado mutable queda encapsulado

/** Caso de prueba como case class. */
case class TestCase(name: String, words: List[String], expected: Map[String, Int])

/** Punto de entrada con @main: sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = mutable.ListBuffer.empty[TestCase]
  // Caso 1: ejemplo del enunciado
  testCases += TestCase("ejemplo del enunciado", List("the", "fox", "the"), Map("the" -> 2, "fox" -> 1))
  // Caso 2: lista vacía
  testCases += TestCase("lista vacía", List.empty, Map.empty)
  // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
  testCases += TestCase("mayúsculas y repeticiones", List("a", "A", "a", "b", "a"), Map("a" -> 3, "A" -> 1, "b" -> 1))

  var i = 0
  while i < testCases.length do
    val TestCase(name, words, expected) = testCases(i)
    val result = wordFrequency(words)
    assert(result == expected, s"Caso '$name': entrada $words, esperado $expected, obtenido $result")
    i += 1

  println("Todas las pruebas pasaron.")