// P04: Balanced Parentheses — Scala 3.6
//
// Características utilizadas:
// - Sintaxis moderna de Scala 3 para declaraciones y expresiones.
// - mutable.ListBuffer como pila mutable.
// - mutable.Map para almacenar las correspondencias entre paréntesis.
// - while para realizar todo el procesamiento de forma iterativa.
// - var para mantener el estado mutable durante el recorrido.
//
// No se utilizan map(), filter(), foldLeft(), for ni recursión.
//
// Scala 3.6 no introduce una característica específica que mejore
// sustancialmente este algoritmo, por lo que se mantiene una solución
// iterativa directa y eficiente.

import scala.collection.mutable

object P04BalancedParentheses_v36 {

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
   *    Se utiliza como pila mutable para almacenar los caracteres
   *    de apertura.
   *
   * 2. mutable.Map
   *    Almacena la correspondencia entre cada carácter de cierre
   *    y su correspondiente carácter de apertura.
   *
   * 3. while
   *    El recorrido de la cadena es completamente iterativo.
   *
   * 4. var
   *    Se utiliza para mantener el índice, el estado de validez
   *    y las estructuras mutables requeridas por el paradigma.
   *
   * 5. No se utilizan map(), filter(), foldLeft() ni recursión.
   *
   * 6. La función conserva exactamente el nombre isBalanced.
   *
   * 7. Una cadena vacía devuelve true porque la pila permanece vacía
   *    y valid comienza y permanece en true.
   *
   * 8. Se utilizan únicamente elementos de la biblioteca estándar.
   *
   * 9. No se fuerza ninguna característica exclusiva de Scala 3.6:
   *    ninguna aportaría una mejora significativa a este algoritmo
   *    manteniendo estrictamente el paradigma iterativo solicitado.
   */
}