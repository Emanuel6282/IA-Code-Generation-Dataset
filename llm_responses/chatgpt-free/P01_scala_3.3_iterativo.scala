// P01: Word Frequency — Scala 3.3
//
// Paradigma: Iterativo
//
// Características utilizadas de Scala 3.3:
// 1. Sintaxis de Scala 3 para la definición de métodos.
// 2. Inferencia de tipos mediante val/var.
// 3. Colecciones mutables de la biblioteca estándar.
// 4. while para realizar el procesamiento de forma iterativa.
//
// No se utilizan map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

def wordFrequency(words: List[String]): mutable.Map[String, Int] =
  val result = mutable.Map[String, Int]()

  var i = 0

  while i < words.length do
    val word = words(i)
    val currentCount = result.getOrElse(word, 0)

    result.update(word, currentCount + 1)

    i += 1

  result


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