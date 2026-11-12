// Balanced Parentheses — Scala 3.6, paradigma funcional.
//
// Sobre las novedades de Scala 3.6 (verificado con el compilador 3.6.4): la nueva
// sintaxis de given y de context bounds con `as` (SIP-64) es estable, pero sirve para
// abstracciones con type classes, y esta función trabaja con un String concreto, sin
// ningún tipo genérico que abstraer. Las tuplas con nombre y `runtimeChecked` siguen
// siendo experimentales en 3.6 (requieren -experimental), así que no se usan. El código
// usa los idiomas estables de Scala 3: sintaxis sin llaves, definiciones de nivel
// superior, @main y desempaquetado de tuplas en lambdas.

import scala.annotation.tailrec

/** Mapeo inmutable cierre -> apertura. */
private val Pairs: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')

/** Retorna true si los paréntesis, corchetes y llaves de `s` están balanceados y se
  * cierran en el orden correcto; false en caso contrario.
  *
  * Definición de nivel superior (Scala 3): no necesita un `object` contenedor.
  *
  * @throws IllegalArgumentException si `s` contiene un carácter que no es ( ) [ ] { }
  */
def isBalanced(s: String): Boolean =
  // @tailrec: el compilador garantiza que la recursión es de cola y la convierte en un
  // salto, así que no consume pila aunque la cadena sea muy larga. El estado (posición y
  // pila de aperturas) se pasa de una llamada a la siguiente; la pila es una List
  // inmutable y la recursión termina en cuanto hay un error.
  @tailrec
  def loop(i: Int, stack: List[Char]): Boolean =
    if i == s.length then stack.isEmpty                       // quedan aperturas -> false
    else s.charAt(i) match
      case c @ ('(' | '[' | '{') => loop(i + 1, c :: stack)
      case c @ (')' | ']' | '}') =>
        stack match
          case top :: below if top == Pairs(c) => loop(i + 1, below)  // cierre correcto
          case _                               => false               // no corresponde o pila vacía
      case c => throw IllegalArgumentException(s"Carácter no válido: '$c'")

  loop(0, Nil)

/** Implementación de referencia independiente con foldLeft + flatMap sobre Option:
  * Some(pila) mientras todo va bien, None en cuanto un cierre no corresponde.
  */
def byFoldLeft(s: String): Boolean =
  s.foldLeft(Option(List.empty[Char])) { (state, c) =>
    state.flatMap(stack =>
      Pairs.get(c) match
        case Some(opening) => Option.when(stack.headOption.contains(opening))(stack.tail)
        case None          => Some(c :: stack)
    )
  }.contains(Nil)

/** Caso de prueba: case class inmutable. */
case class TestCase(name: String, input: String, expected: Boolean)

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = List(
    // Caso 1: ejemplos balanceados del enunciado
    TestCase("paréntesis simples", "()", true),
    TestCase("anidamiento mixto", "{[]}", true),
    // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
    TestCase("orden incorrecto", "([)]", false),
    // Caso 3: aperturas sin cerrar y cierres sin abrir
    TestCase("apertura sin cerrar", "((", false),
    TestCase("cierre sin abrir", "())", false)
  )

  assert(isBalanced(""), "cadena vacía -> balanceada")

  // map + filter + map: calcula cada resultado, conserva los incorrectos (frente al
  // valor esperado o a la referencia) y los convierte en mensajes. Las lambdas reciben
  // la tupla desempaquetada (Scala 3).
  val failures = testCases
    .map(tc => (tc, isBalanced(tc.input)))
    .filter((tc, result) => result != tc.expected || result != byFoldLeft(tc.input))
    .map((tc, result) => s"Caso '${tc.name}' (\"${tc.input}\"): esperado ${tc.expected}, obtenido $result")

  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")