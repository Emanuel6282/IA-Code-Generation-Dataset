// Balanced Parentheses — Scala 3.6, paradigma iterativo.
//
// Sobre las novedades de Scala 3.6 (verificado con el compilador 3.6.4): la nueva
// sintaxis de given y de context bounds con `as` (SIP-64) es estable, pero sirve para
// abstracciones con type classes, y esta función trabaja con un String concreto, sin
// ningún tipo genérico que abstraer. Las tuplas con nombre y `runtimeChecked` siguen
// siendo experimentales en 3.6 (requieren -experimental), así que no se usan. El código
// usa los idiomas estables de Scala 3, incluido boundary/break (Scala 3.3+), que encaja
// de forma natural para salir del bucle en cuanto se detecta un error.

import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

/** Mapeo cierre -> apertura. Es privado y nunca se modifica después de crearlo. */
private val Pairs: mutable.Map[Char, Char] = mutable.Map(')' -> '(', ']' -> '[', '}' -> '{')

/** Retorna true si los paréntesis, corchetes y llaves de `s` están balanceados y se
  * cierran en el orden correcto; false en caso contrario.
  *
  * boundary / break permite salir del bucle con `false` en cuanto un cierre no
  * corresponde, sin `return` (desaconsejado en Scala 3) ni una variable bandera.
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

/** Punto de entrada con @main: sin object ni `def main(args: Array[String])`. */
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