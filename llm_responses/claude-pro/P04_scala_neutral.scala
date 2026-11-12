import scala.annotation.tailrec

object BalancedParentheses {

  /** Mapeo cierre -> apertura. */
  private val Pairs: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')

  /** Retorna true si los paréntesis, corchetes y llaves de `s` están balanceados y se
    * cierran en el orden correcto; false en caso contrario.
    *
    * @throws IllegalArgumentException si `s` contiene un carácter que no es ( ) [ ] { }
    */
  def isBalanced(s: String): Boolean = {
    // Recorre la cadena por índice con una pila inmutable (List[Char]): apilar es
    // `c :: stack` y desapilar es tomar el resto, ambos en O(1). @tailrec garantiza que
    // la recursión es de cola, así que no consume pila aunque la cadena sea muy larga,
    // y permite terminar en cuanto se detecta un error.
    @tailrec
    def loop(i: Int, stack: List[Char]): Boolean =
      if (i == s.length) stack.isEmpty          // quedan aperturas sin cerrar -> no balanceada
      else s.charAt(i) match {
        case c @ ('(' | '[' | '{') => loop(i + 1, c :: stack)
        case c @ (')' | ']' | '}') =>
          stack match {
            case top :: rest if top == Pairs(c) => loop(i + 1, rest)   // cierre correcto
            case _                              => false               // no corresponde o pila vacía
          }
        case c => throw new IllegalArgumentException(s"Carácter no válido: '$c'")
      }

    loop(0, Nil)
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplos balanceados del enunciado y cadena vacía
    assert(isBalanced("()"))
    assert(isBalanced("{[]}"))
    assert(isBalanced(""))

    // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
    assert(!isBalanced("([)]"))

    // Caso 3: aperturas sin cerrar y cierres sin abrir
    assert(!isBalanced("(("))
    assert(!isBalanced("())"))

    println("Todas las pruebas pasaron.")
  }
}