// P01: Word Frequency — Scala 2.13
//
// Paradigma: Iterativo
// Características utilizadas:
// - var: para mantener índices y referencias mutables.
// - mutable.ListBuffer: para representar listas modificables en los tests.
// - mutable.Map: para almacenar y actualizar las frecuencias.
// - while: para recorrer las palabras sin utilizar operaciones funcionales.
// - getOrElse: para obtener el contador actual o 0 si la palabra no existe.
//
// Biblioteca: únicamente la biblioteca estándar de Scala 2.13.

import scala.collection.mutable

def wordFrequency(words: List[String]): mutable.Map[String, Int] = {
  val result = mutable.Map[String, Int]()

  var i = 0

  while (i < words.length) {
    val word = words(i)
    result.update(word, result.getOrElse(word, 0) + 1)
    i += 1
  }

  result
}

// ============================================================
// Casos de prueba
// ============================================================

// Test 1: palabras repetidas
val test1 = mutable.ListBuffer[String]()
test1 += "the"
test1 += "fox"
test1 += "the"

assert(
  wordFrequency(test1.toList) ==
    mutable.Map("the" -> 2, "fox" -> 1)
)

// Test 2: lista vacía
val test2 = mutable.ListBuffer[String]()

assert(
  wordFrequency(test2.toList).isEmpty
)

// Test 3: una sola palabra
val test3 = mutable.ListBuffer[String]()
test3 += "hello"

assert(
  wordFrequency(test3.toList) ==
    mutable.Map("hello" -> 1)
)

println("All tests passed")