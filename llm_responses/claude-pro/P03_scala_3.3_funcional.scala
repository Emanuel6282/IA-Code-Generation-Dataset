import scala.annotation.tailrec

/** Elemento de la entrada: un entero o una sublista anidada.
  *
  * Tipo unión (Scala 3): el compilador rechaza en el nivel superior cualquier elemento
  * que no sea Int ni List, como List(1, "x"). En los niveles internos el tipo de los
  * elementos ya no se conoce (List[?]), por eso flatten también lo comprueba al ejecutar.
  */
type Element = Int | List[?]

/** Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.
  *
  * @throws IllegalArgumentException si algún elemento anidado no es Int ni List
  */
def flatten(nested: List[Element]): List[Int] =
  // @tailrec: el compilador garantiza que la recursión es de cola y la convierte en
  // un salto, así que funciona con cualquier nivel de anidamiento sin desbordar la
  // pila. El estado (pendientes y acumulador) son listas inmutables que se pasan de
  // una llamada a la siguiente en lugar de modificarse.
  @tailrec
  def loop(pending: List[Any], acc: List[Int]): List[Int] =
    pending match
      case Nil                    => acc.reverse              // se acumuló al revés
      case (sub: List[?]) :: rest => loop(sub ::: rest, acc)  // sublista -> su contenido
      case (n: Int) :: rest       => loop(rest, n :: acc)     // entero -> a la salida
      case other :: _             => throw IllegalArgumentException(s"Elemento no válido: $other")

  loop(nested, Nil)

/** Caso de prueba: case class inmutable. */
case class TestCase(name: String, nested: List[Element], expected: List[Int])

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  // Anidamiento muy profundo (una recursión que no sea de cola desbordaría la pila),
  // construido con foldLeft en lugar de un bucle.
  val deep: List[?] = (1 to 100000).foldLeft[List[?]](List(7))((acc, _) => List(acc))

  val testCases = List(
    // Caso 1: ejemplo del enunciado
    TestCase("ejemplo del enunciado", List(1, List(2, 3), List(4, List(5, 6))), List(1, 2, 3, 4, 5, 6)),
    // Caso 2: lista vacía y sublistas vacías anidadas
    TestCase("listas vacías", List(Nil, List(Nil), List(Nil, List(Nil))), Nil),
    // Caso 3: anidamiento muy profundo
    TestCase("anidamiento profundo", List(0, deep, 8), List(0, 7, 8))
  )

  assert(flatten(Nil) == Nil, "lista vacía -> lista vacía")

  // map + filter + map: calcula cada resultado, conserva los incorrectos y los
  // convierte en mensajes. Las lambdas reciben la tupla desempaquetada (Scala 3).
  val wrongResults = testCases
    .map(tc => (tc, flatten(tc.nested)))
    .filter((tc, result) => result != tc.expected)
    .map((tc, result) => s"Caso '${tc.name}': esperado ${tc.expected}, obtenido $result")

  // flatMap + Option.when + foldLeft: comprobación adicional de que la suma de los
  // enteros aplanados coincide con la suma esperada, calculada por otro camino.
  val wrongSums = testCases.flatMap(tc =>
    val sum = flatten(tc.nested).foldLeft(0)(_ + _)
    val expectedSum = tc.expected.foldLeft(0)(_ + _)
    Option.when(sum != expectedSum)(s"Caso '${tc.name}': suma $sum, esperada $expectedSum")
  )

  val failures = wrongResults ++ wrongSums
  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")