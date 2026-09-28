// P01: Word Frequency — Scala 3.6
//
// Paradigma: Iterativo
//
// Características utilizadas:
// 1. Sintaxis while ... do de Scala 3:
//    Permite expresar el bucle iterativo de forma concisa.
//
// 2. Inferencia de tipos:
//    Se utiliza val/var sin repetir tipos cuando son evidentes.
//
// 3. Colecciones mutables de la biblioteca estándar:
//    mutable.Map para almacenar las frecuencias y
//    mutable.ListBuffer para construir los casos de prueba.
//
// Nota:
// Scala 3.6 no introduce una característica específica que mejore
// significativamente este algoritmo de conteo. Se utiliza la sintaxis
// moderna de Scala 3 sin agregar complejidad innecesaria.

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