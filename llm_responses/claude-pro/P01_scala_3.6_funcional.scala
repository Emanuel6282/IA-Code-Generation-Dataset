// Word Frequency — Scala 3.6, paradigma funcional.
//
// Sobre las novedades de Scala 3.6: las que son estables en esta versión
// (nueva sintaxis de given y de context bounds con `as`, SIP-64) sirven para
// abstracciones con type classes, que este problema no necesita. Las tuplas con
// nombre y `runtimeChecked` todavía son experimentales en 3.6 (requieren
// -experimental), así que no se usan. El código usa los idiomas estables de
// Scala 3: sintaxis sin llaves, definiciones de nivel superior y @main.

import scala.annotation.tailrec

/** Retorna un mapa inmutable que asocia cada palabra única con su número de apariciones.
  * Si la lista está vacía, retorna un mapa vacío.
  *
  * Definición de nivel superior (Scala 3): no necesita un `object` contenedor.
  */
def wordFrequency(words: List[String]): Map[String, Int] =
  // @tailrec: el compilador verifica que la recursión es de cola y la convierte en
  // un salto, así que no crece la pila aunque la lista sea muy larga.
  @tailrec
  def count(remaining: List[String], acc: Map[String, Int]): Map[String, Int] =
    remaining match
      case Nil          => acc
      // updatedWith devuelve un mapa nuevo con la entrada actualizada, sin mutar acc.
      case word :: rest => count(rest, acc.updatedWith(word)(current => Some(current.fold(1)(_ + 1))))

  count(words, Map.empty)

/** Caso de prueba: case class inmutable. */
case class TestCase(name: String, words: List[String], expected: Map[String, Int])

/** Implementaciones de referencia independientes para validar el resultado. */
def byFoldLeft(words: List[String]): Map[String, Int] =
  words.foldLeft(Map.empty[String, Int])((acc, word) => acc.updated(word, acc.getOrElse(word, 0) + 1))

def byGroupMapReduce(words: List[String]): Map[String, Int] =
  words.groupMapReduce(identity)(_ => 1)(_ + _)

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = List(
    // Caso 1: ejemplo del enunciado
    TestCase("ejemplo del enunciado", List("the", "fox", "the"), Map("the" -> 2, "fox" -> 1)),
    // Caso 2: lista vacía
    TestCase("lista vacía", List.empty, Map.empty),
    // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
    TestCase("mayúsculas y repeticiones", List("a", "A", "a", "b", "a"), Map("a" -> 3, "A" -> 1, "b" -> 1))
  )

  // flatMap + filter + map: por cada caso, Some(mensaje) si falla y None si pasa.
  val failures = testCases.flatMap { case TestCase(name, words, expected) =>
    Option(wordFrequency(words))
      .filter(result => result != expected || result != byFoldLeft(words) || result != byGroupMapReduce(words))
      .map(result => s"Caso '$name': entrada $words, esperado $expected, obtenido $result")
  }

  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")