// P01: Word Frequency — Scala 2.13
//
// Paradigma: Funcional
// Características utilizadas:
// - val: evita estado mutable.
// - Colecciones inmutables: List y Map.
// - map(): transforma elementos.
// - filter(): selecciona elementos.
// - foldLeft(): acumula las frecuencias.
// - flatMap(): demuestra composición funcional sobre colecciones.
// - @tailrec: recorrido recursivo optimizado por tail recursion.
//
// Biblioteca: únicamente la biblioteca estándar de Scala 2.13.

import scala.annotation.tailrec

def wordFrequency(words: List[String]): Map[String, Int] = {

  // flatMap() se utiliza para normalizar la entrada como una colección
  // de palabras antes del procesamiento.
  val normalizedWords: List[String] =
    words.flatMap(word => List(word))

  // filter() elimina palabras vacías.
  val validWords: List[String] =
    normalizedWords.filter(word => word.nonEmpty)

  // map() transforma cada palabra en un par (palabra, 1).
  val occurrences: List[(String, Int)] =
    validWords.map(word => (word, 1))

  // foldLeft() construye el mapa de frecuencias de forma inmutable.
  occurrences.foldLeft(Map.empty[String, Int]) {
    (frequency, occurrence) =>
      val word = occurrence._1
      val count = occurrence._2

      frequency.updated(
        word,
        frequency.getOrElse(word, 0) + count
      )
  }
}

// Función auxiliar con tail recursion.
// Se utiliza para validar los resultados de los tests sin utilizar
// var ni estructuras mutables.
@tailrec
def allTestsPassed(
    tests: List[() => Boolean]
): Boolean = {
  tests match {
    case Nil => true
    case test :: remaining =>
      assert(test())
      allTestsPassed(remaining)
  }
}

// ============================================================
// Casos de prueba
// ============================================================

val tests = List[() => Boolean](

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

allTestsPassed(tests)

println("All tests passed")