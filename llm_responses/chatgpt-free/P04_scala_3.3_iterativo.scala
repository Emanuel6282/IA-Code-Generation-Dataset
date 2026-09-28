// P04: Balanced Parentheses — Scala 3.3
//
// Características utilizadas:
// - Sintaxis de Scala 3 para declaraciones y tipos más concisos.
// - mutable.ListBuffer como pila mutable.
// - mutable.Map para almacenar las correspondencias entre cierres
//   y aperturas.
// - while para realizar el procesamiento de forma iterativa.
//
// No se utilizan map(), filter(), foldLeft(), for-comprehensions
// ni recursión.
//
// Scala 3.3 no introduce una característica específica que mejore
// sustancialmente este algoritmo, por lo que se utiliza una implementación
// iterativa directa y compatible con Scala 3.3.

import scala.collection.mutable

object P04BalancedParentheses_v33 {

  def isBalanced(s: String): Boolean = {
    var stack = mutable.ListBuffer.empty[Char]

    var pairs = mutable.Map(
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

  /*
   * Cambios y decisiones:
   *
   * 1. mutable.ListBuffer
   *    Se utiliza como pila mutable para almacenar los caracteres de apertura.
   *
   * 2. mutable.Map
   *    Contiene la correspondencia entre cada carácter de cierre y su
   *    correspondiente carácter de apertura.
   *
   * 3. while
   *    El recorrido de la cadena es completamente iterativo.
   *
   * 4. var
   *    Se utiliza para mantener el índice, el estado de validez y la pila.
   *
   * 5. No se utilizan map(), filter(), foldLeft() ni recursión.
   *
   * 6. La función mantiene exactamente el nombre isBalanced.
   *
   * 7. La cadena vacía devuelve true porque la pila permanece vacía
   *    y valid conserva el valor true.
   *
   * 8. La lógica y los resultados son equivalentes a las versiones
   *    anteriores del ejercicio.
   */
}