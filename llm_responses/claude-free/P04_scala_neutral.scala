// P04: Balanced Parentheses — Scala
// Solo biblioteca estandar de Scala.

import scala.annotation.tailrec

object BalancedParentheses {

  /**
   * Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
   * "{", "}", retorna true si todos los parentesis/corchetes estan
   * correctamente balanceados y cerrados en orden. Retorna false en caso
   * contrario.
   *
   * Implementacion mediante recursion de cola (@tailrec) sobre una pila
   * inmutable (List[Char]): cada caracter de apertura se antepone a la
   * pila; cada caracter de cierre debe coincidir con la cabeza de la pila
   * (el caracter de apertura mas reciente y aun no cerrado), o la cadena
   * no esta balanceada.
   *
   * @param s Cadena que contiene unicamente los caracteres de parentesis
   *          y corchetes indicados.
   * @return true si la cadena esta correctamente balanceada, false en
   *         caso contrario. Una cadena vacia se considera balanceada
   *         (true).
   */
  def isBalanced(s: String): Boolean = {
    val paresCierre: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')

    @tailrec
    def loop(caracteres: List[Char], pila: List[Char]): Boolean =
      caracteres match {
        case Nil =>
          pila.isEmpty
        case c :: resto if c == '(' || c == '[' || c == '{' =>
          loop(resto, c :: pila)
        case c :: resto if paresCierre.contains(c) =>
          pila match {
            case cima :: restoPila if cima == paresCierre(c) =>
              loop(resto, restoPila)
            case _ =>
              false
          }
        case _ :: resto =>
          loop(resto, pila)
      }

    loop(s.toList, List.empty[Char])
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: balanceado simple
    assert(isBalanced("()") == true)

    // Caso 2: orden incorrecto de cierre -> no balanceado
    assert(isBalanced("([)]") == false)

    // Caso 3: anidamiento balanceado de varios tipos
    assert(isBalanced("{[]}") == true)

    println("Todos los casos de prueba pasaron correctamente.")
  }
}