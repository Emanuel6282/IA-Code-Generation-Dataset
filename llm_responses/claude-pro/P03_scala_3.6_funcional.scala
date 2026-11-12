import scala.annotation.tailrec
import scala.reflect.ClassTag

/** Retorna una lista plana con todos los elementos de tipo `A` de `nested`, en el
  * mismo orden. Para el problema planteado, `A` es Int y se infiere solo:
  * `flatten(List(1, List(2, 3)))` devuelve `List[Int]`.
  *
  * Context bound con nombre `A: ClassTag as tag` (SIP-64, estable desde Scala 3.6):
  * da nombre a la instancia de ClassTag directamente en la firma, sin un `using`
  * aparte. `tag` permite comprobar en tiempo de ejecución que cada elemento anidado es
  * de tipo A (con `tag(value)`), algo que el borrado de tipos de la JVM no permite
  * hacer con un simple `value: A`. Así la función sirve para listas anidadas de
  * cualquier tipo de hoja (Int, String...) sin perder la comprobación.
  *
  * Tipo unión (Scala 3) en el parámetro: el compilador rechaza en el nivel superior
  * cualquier elemento que no sea A ni List.
  *
  * @throws IllegalArgumentException si algún elemento anidado no es A ni List
  */
def flatten[A: ClassTag as tag](nested: List[A | List[?]]): List[A] =
  // @tailrec: el compilador garantiza que la recursión es de cola y la convierte en
  // un salto, así que funciona con cualquier nivel de anidamiento sin desbordar la
  // pila. El estado (pendientes y acumulador) son listas inmutables que se pasan de
  // una llamada a la siguiente en lugar de modificarse.
  @tailrec
  def loop(pending: List[Any], acc: List[A]): List[A] =
    pending match
      case Nil                    => acc.reverse              // se acumuló al revés
      case (sub: List[?]) :: rest => loop(sub ::: rest, acc)  // sublista -> su contenido
      case tag(value) :: rest     => loop(rest, value :: acc) // elemento de tipo A -> a la salida
      case other :: _             => throw IllegalArgumentException(s"Elemento no válido: $other")

  loop(nested, Nil)

/** Caso de prueba: case class inmutable. */
case class TestCase(name: String, nested: List[Int | List[?]], expected: List[Int])

/** Punto de entrada con @main: sin object ni `def main(args: Array[String])`. */
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

  assert(flatten[Int](Nil) == Nil, "lista vacía -> lista vacía")

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

  // La misma función con otro tipo de hoja, gracias al context bound genérico.
  assert(flatten(List("a", List("b", List("c")))) == List("a", "b", "c"), "aplanado de String")

  println("Todas las pruebas pasaron.")