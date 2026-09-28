// P04: Balanced Parentheses — Scala 2.13
//
// Características utilizadas:
// - mutable.ListBuffer: utilizada como pila mutable para almacenar
//   los caracteres de apertura.
// - mutable.Map: utilizada para asociar cada carácter de cierre
//   con su correspondiente carácter de apertura.
// - while: utilizado para recorrer la cadena de forma iterativa.
// - var: utilizado para mantener y modificar el estado durante el recorrido.
//
// No se utilizan map(), filter(), foldLeft(), foreach() ni recursión.

import scala.collection.mutable

object P04BalancedParentheses_v213 {

  def isBalanced(s: String): Boolean = {
    var stack = mutable.ListBuffer[Char]()
    var pairs = mutable.Map[Char, Char](
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    var i = 0
    var valid = true

    while (i < s.length && valid) {
      val c = s.charAt(i)

      if (c == '(' || c == '[' || c == '{') {
        stack += c
      } else if (pairs.contains(c)) {
        if (stack.isEmpty || stack.last != pairs(c)) {
          valid = false
        } else {
          stack.remove(stack.length - 1)
        }
      }

      i += 1
    }

    valid && stack.isEmpty
  }

  def main(args: Array[String]): Unit = {
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)

    println("All tests passed")
  }
}