import scala.collection.mutable

/** Retorna un mapa que asocia cada palabra única con su número de apariciones.
  * Si la lista está vacía, retorna un mapa vacío.
  *
  * Definición de nivel superior (Scala 3): no necesita un `object` contenedor.
  */
def wordFrequency(words: List[String]): Map[String, Int] =
  val frequencies = mutable.Map.empty[String, Int]
  val it = words.iterator
  while it.hasNext do                       // sintaxis de control de Scala 3: while ... do
    val word = it.next()
    frequencies(word) = frequencies.getOrElse(word, 0) + 1
  frequencies.toMap                         // se expone un Map inmutable

/** Caso de prueba: case class de Scala 3 con sintaxis sin llaves. */
case class TestCase(name: String, words: List[String], expected: Map[String, Int])

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
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