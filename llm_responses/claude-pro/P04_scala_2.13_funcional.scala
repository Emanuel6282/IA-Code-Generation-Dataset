import scala.annotation.tailrec

object BalancedParentheses {

  /** Mapeo inmutable cierre -> apertura. */
  private val Pairs: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')

  /** Retorna true si los paréntesis, corchetes y llaves de `s` están balanceados y se
    * cierran en el orden correcto; false en caso contrario.
    *
    * @throws IllegalArgumentException si `s` contiene un carácter que no es ( ) [ ] { }
    */
  def isBalanced(s: String): Boolean = {
    // @tailrec: el compilador garantiza que la recursión es de cola y la convierte en
    // un salto, así que no consume pila aunque la cadena sea muy larga. El estado
    // (caracteres pendientes y pila de aperturas) son listas inmutables que se pasan
    // de una llamada a la siguiente; la recursión termina en cuanto hay un error.
    @tailrec
    def loop(pending: List[Char], stack: List[Char]): Boolean =
      pending match {
        case Nil => stack.isEmpty                                    // quedan aperturas -> false
        case (c @ ('(' | '[' | '{')) :: rest => loop(rest, c :: stack)
        case (c @ (')' | ']' | '}')) :: rest =>
          stack match {
            case top :: below if top == Pairs(c) => loop(rest, below)  // cierre correcto
            case _                               => false              // no corresponde o pila vacía
          }
        case c :: _ => throw new IllegalArgumentException(s"Carácter no válido: '$c'")
      }

    loop(s.toList, Nil)
  }

  def main(args: Array[String]): Unit = {
    val testCases: List[(String, String, Boolean)] = List(
      // Caso 1: ejemplos balanceados del enunciado
      ("paréntesis simples", "()", true),
      ("anidamiento mixto", "{[]}", true),
      // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
      ("orden incorrecto", "([)]", false),
      // Caso 3: aperturas sin cerrar y cierres sin abrir
      ("apertura sin cerrar", "((", false),
      ("cierre sin abrir", "())", false)
    )

    assert(isBalanced(""), "cadena vacía -> balanceada")

    // Implementación de referencia independiente con foldLeft + flatMap sobre Option:
    // Some(pila) mientras todo va bien, None en cuanto un cierre no corresponde.
    def byFoldLeft(s: String): Boolean =
      s.foldLeft(Option(List.empty[Char])) { (state, c) =>
        state.flatMap { stack =>
          Pairs.get(c) match {
            case Some(opening) => Option.when(stack.headOption.contains(opening))(stack.tail)
            case None          => Some(c :: stack)
          }
        }
      }.contains(Nil)

    // map + filter + map: calcula cada resultado, conserva los incorrectos (frente al
    // valor esperado o a la referencia) y los convierte en mensajes.
    val failures: List[String] = testCases
      .map { case (name, input, expected) => (name, input, expected, isBalanced(input)) }
      .filter { case (_, input, expected, result) => result != expected || result != byFoldLeft(input) }
      .map { case (name, input, expected, result) =>
        s"Caso '$name' (\"$input\"): esperado $expected, obtenido $result"
      }

    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}