// P04: Balanced Parentheses — Scala 2.13 (paradigma iterativo)
// Solo biblioteca estandar de Scala.

import scala.collection.mutable

object BalancedParentheses {

  /**
   * Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
   * "{", "}", retorna true si todos los parentesis/corchetes estan
   * correctamente balanceados y cerrados en orden. Retorna false en caso
   * contrario.
   *
   * Implementacion estrictamente iterativa: usa una pila mutable
   * (mutable.Stack) y un bucle while con indice manual (var), sin map(),
   * filter(), foldLeft() ni recursion.
   *
   * @param s Cadena que contiene unicamente los caracteres de parentesis
   *          y corchetes indicados.
   * @return true si la cadena esta correctamente balanceada, false en
   *         caso contrario. Una cadena vacia se considera balanceada
   *         (true).
   */
  def isBalanced(s: String): Boolean = {
    val paresCierre: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')
    val pila: mutable.Stack[Char] = mutable.Stack.empty[Char]

    var indice: Int = 0
    var balanceado: Boolean = true

    while (indice < s.length && balanceado) {
      val caracter = s(indice)

      if (caracter == '(' || caracter == '[' || caracter == '{') {
        pila.push(caracter)
      } else if (paresCierre.contains(caracter)) {
        if (pila.isEmpty) {
          balanceado = false
        } else if (pila.top != paresCierre(caracter)) {
          balanceado = false
        } else {
          pila.pop()
        }
      }

      indice += 1
    }

    if (pila.nonEmpty) {
      balanceado = false
    }

    balanceado
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