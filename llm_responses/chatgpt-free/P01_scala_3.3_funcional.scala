// P01: Word Frequency — Scala 3.3
//
// Paradigma: Funcional
//
// Características utilizadas:
// 1. val:
//    Mantiene todas las referencias inmutables.
//
// 2. Colecciones inmutables:
//    Se utilizan List y Map.
//
// 3. map():
//    Transforma cada palabra en un par (palabra, 1).
//
// 4. filter():
//    Elimina palabras vacías.
//
// 5. foldLeft():
//    Acumula las frecuencias en un Map inmutable.
//
// 6. flatMap():
//    Se utiliza para procesar la lista sin estado mutable.
//
// 7. @tailrec:
//    Se utiliza para ejecutar los tests mediante recursión de cola.
//
// Biblioteca estándar únicamente.

import scala.annotation.tailrec

def wordFrequency(words: List[String]): Map[String, Int] =
  val normalizedWords =
    words.flatMap(word => List(word))

  val validWords =
    normalizedWords.filter(_.nonEmpty)

  val occurrences =
    validWords.map(word => word -> 1)

  occurrences.foldLeft(Map.empty[String, Int]) {
    case (frequency, (word, count)) =>
      frequency.updated(
        word,
        frequency.getOrElse(word, 0) + count
      )
  }


// ============================================================
// Tests
// ============================================================

val tests: List[() => Boolean] = List(

  // Test 1: palabras repetidas
  () =>
    wordFrequency(List("the", "fox", "the")) ==
      Map("the" -> 2, "fox" -> 1),

  // Test 2: lista vacía
  () =>
    wordFrequency(List.empty[String]) ==
      Map.empty[String, Int],

  // Test 3: una sola palabra
  () =>
    wordFrequency(List("hello")) ==
      Map("hello" -> 1)
)

@tailrec
def runTests(remaining: List[() => Boolean]): Unit =
  remaining match
    case Nil =>
      println("All tests passed")

    case test :: rest =>
      assert(test())
      runTests(rest)

runTests(tests)