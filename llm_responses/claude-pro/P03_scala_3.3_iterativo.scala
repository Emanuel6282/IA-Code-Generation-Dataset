import scala.collection.mutable

/** Elemento de la entrada: un entero o una sublista anidada.
  *
  * Tipo unión (Scala 3): el compilador rechaza en el nivel superior cualquier elemento
  * que no sea Int ni List, como List(1, "x"). En los niveles internos el tipo de los
  * elementos ya no se conoce (List[?]), por eso flatten también lo comprueba al ejecutar.
  */
type Element = Int | List[?]

/** Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.
  *
  * Usa una pila explícita de iteradores en lugar de recursión, así que funciona con
  * cualquier nivel de anidamiento sin desbordar la pila.
  *
  * @throws IllegalArgumentException si algún elemento anidado no es Int ni List
  */
def flatten(nested: List[Element]): List[Int] =
  val result = mutable.ListBuffer.empty[Int]
  val stack = mutable.ArrayDeque[Iterator[Any]](nested.iterator)
  while stack.nonEmpty do
    val current = stack.last
    if !current.hasNext then
      stack.removeLast()                              // el nivel actual se agotó: vuelve al anterior
    else
      current.next() match
        case sub: List[?] => stack.append(sub.iterator)  // baja un nivel; este se retoma después
        case n: Int       => result += n
        case other        => throw IllegalArgumentException(s"Elemento no válido: $other")
  result.toList

/** Caso de prueba como case class. */
case class TestCase(name: String, nested: List[Element], expected: List[Int])

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  // Anidamiento muy profundo (una versión recursiva desbordaría la pila).
  var deep: List[?] = List(7)
  var level = 0
  while level < 100000 do
    deep = List(deep)
    level += 1

  val testCases = mutable.ListBuffer.empty[TestCase]
  // Caso 1: ejemplo del enunciado
  testCases += TestCase("ejemplo del enunciado", List(1, List(2, 3), List(4, List(5, 6))), List(1, 2, 3, 4, 5, 6))
  // Caso 2: lista vacía y sublistas vacías anidadas
  testCases += TestCase("listas vacías", List(Nil, List(Nil), List(Nil, List(Nil))), Nil)
  // Caso 3: anidamiento muy profundo
  testCases += TestCase("anidamiento profundo", List(0, deep, 8), List(0, 7, 8))

  assert(flatten(Nil) == Nil, "lista vacía -> lista vacía")

  var i = 0
  while i < testCases.length do
    val TestCase(name, nested, expected) = testCases(i)
    val result = flatten(nested)
    assert(result == expected, s"Caso '$name': esperado $expected, obtenido $result")
    i += 1

  println("Todas las pruebas pasaron.")