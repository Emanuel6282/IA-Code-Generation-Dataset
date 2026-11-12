import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

/** Mapeo cierre -> apertura. Es privado y nunca se modifica después de crearlo. */
private val Pairs: mutable.Map[Char, Char] = mutable.Map(')' -> '(', ']' -> '[', '}' -> '{')

/** Retorna true si los paréntesis, corchetes y llaves de `s` están balanceados y se
  * cierran en el orden correcto; false en caso contrario.
  *
  * Usa scala.util.boundary / break (nuevo en Scala 3.3): permite salir del bucle con
  * `false` en cuanto un cierre no corresponde, sin `return` (desaconsejado en Scala 3)
  * ni una variable bandera que haya que consultar en cada vuelta.
  *
  * @throws IllegalArgumentException si `s` contiene un carácter que no es ( ) [ ] { }
  */
def isBalanced(s: String): Boolean =
  boundary:
    val stack = mutable.ArrayDeque.empty[Char]
    var i = 0
    while i < s.length do
      s.charAt(i) match
        case c @ ('(' | '[' | '{') => stack.append(c)          // apertura: se apila
        case c @ (')' | ']' | '}') =>
          // Un cierre debe corresponder a la última apertura pendiente; si no (o si la
          // pila está vacía), se sale del boundary devolviendo false.
          if !stack.removeLastOption().contains(Pairs(c)) then break(false)
        case c => throw IllegalArgumentException(s"Carácter no válido: '$c'")
      i += 1
    stack.isEmpty                                               // quedan aperturas -> false

/** Caso de prueba como case class. */
case class TestCase(name: String, input: String, expected: Boolean)

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = mutable.ListBuffer.empty[TestCase]
  // Caso 1: ejemplos balanceados del enunciado
  testCases += TestCase("paréntesis simples", "()", true)
  testCases += TestCase("anidamiento mixto", "{[]}", true)
  // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
  testCases += TestCase("orden incorrecto", "([)]", false)
  // Caso 3: aperturas sin cerrar y cierres sin abrir
  testCases += TestCase("apertura sin cerrar", "((", false)
  testCases += TestCase("cierre sin abrir", "())", false)

  assert(isBalanced(""), "cadena vacía -> balanceada")

  var i = 0
  while i < testCases.length do
    val TestCase(name, input, expected) = testCases(i)
    val result = isBalanced(input)
    assert(result == expected, s"Caso '$name' (\"$input\"): esperado $expected, obtenido $result")
    i += 1

  println("Todas las pruebas pasaron.")