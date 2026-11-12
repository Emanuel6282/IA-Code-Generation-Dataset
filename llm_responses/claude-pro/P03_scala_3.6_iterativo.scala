import scala.collection.mutable
import scala.reflect.ClassTag

/** Retorna una lista plana con todos los elementos de tipo `A` de `nested`, en el
  * mismo orden. Para el problema planteado, `A` es Int y se infiere solo:
  * `flatten(List(1, List(2, 3)))` devuelve `List[Int]`.
  *
  * Context bound con nombre `A: ClassTag as tag` (SIP-64, estable desde Scala 3.6):
  * da nombre a la instancia de ClassTag directamente en la firma, sin un `using`
  * aparte. `tag` permite comprobar en tiempo de ejecución que cada elemento anidado es
  * de tipo A (con `case tag(a)`), algo que el borrado de tipos de la JVM no permite
  * hacer con un simple `case a: A`. Así la función sirve para listas anidadas de
  * cualquier tipo de hoja (Int, String...) sin perder la comprobación.
  *
  * Tipo unión (Scala 3) en el parámetro: el compilador rechaza en el nivel superior
  * cualquier elemento que no sea A ni List.
  *
  * Usa una pila explícita de iteradores en lugar de recursión, así que funciona con
  * cualquier nivel de anidamiento sin desbordar la pila.
  *
  * @throws IllegalArgumentException si algún elemento anidado no es A ni List
  */
def flatten[A: ClassTag as tag](nested: List[A | List[?]]): List[A] =
  val result = mutable.ListBuffer.empty[A]
  val stack = mutable.ArrayDeque[Iterator[Any]](nested.iterator)
  while stack.nonEmpty do
    val current = stack.last
    if !current.hasNext then
      stack.removeLast()                              // el nivel actual se agotó: vuelve al anterior
    else
      current.next() match
        case sub: List[?] => stack.append(sub.iterator)  // baja un nivel; este se retoma después
        case tag(value)   => result += value             // elemento de tipo A -> a la salida
        case other        => throw IllegalArgumentException(s"Elemento no válido: $other")
  result.toList

/** Caso de prueba como case class. */
case class TestCase(name: String, nested: List[Int | List[?]], expected: List[Int])

/** Punto de entrada con @main: sin object ni `def main(args: Array[String])`. */
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

  assert(flatten[Int](Nil) == Nil, "lista vacía -> lista vacía")

  var i = 0
  while i < testCases.length do
    val TestCase(name, nested, expected) = testCases(i)
    val result = flatten(nested)
    assert(result == expected, s"Caso '$name': esperado $expected, obtenido $result")
    i += 1

  // La misma función con otro tipo de hoja, gracias al context bound genérico.
  assert(flatten(List("a", List("b", List("c")))) == List("a", "b", "c"), "aplanado de String")

  println("Todas las pruebas pasaron.")