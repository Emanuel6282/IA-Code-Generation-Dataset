// P04: Balanced Parentheses — Scala 2.13
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. mutable.ListBuffer[Char]:
//    Implementa una pila mutable de símbolos de apertura.
//
// 2. mutable.Map[Char, Char]:
//    Almacena la correspondencia entre cierres y aperturas.
//
// 3. var:
//    Permite actualizar el índice del recorrido.
//
// 4. while:
//    Procesa la cadena de forma estrictamente iterativa.
//
// 5. Scala 2.13:
//    Se utiliza la API estándar de colecciones compatible
//    con Scala 2.13, sin dependencias externas.
//
// Nota:
// Las construcciones principales ya existían antes de
// Scala 2.13; no es necesario utilizar métodos funcionales
// nuevos de esa versión para resolver este problema.

import scala.collection.mutable

object P04BalancedParentheses_v213 {

  def isBalanced(s: String): Boolean = {

    // Pila mutable para almacenar aperturas.
    val stack = mutable.ListBuffer.empty[Char]

    // Mapa mutable de correspondencias.
    val pairs = mutable.Map[Char, Char]()

    pairs(')') = '('
    pairs(']') = '['
    pairs('}') = '{'

    // Índice mutable para el recorrido.
    var i = 0

    // Recorrido estrictamente iterativo.
    while (i < s.length) {

      val current = s.charAt(i)

      // Símbolo de apertura.
      if (current == '(' ||
          current == '[' ||
          current == '{') {

        stack += current

      } else if (pairs.contains(current)) {

        // No puede existir un cierre sin apertura.
        if (stack.isEmpty) {
          return false
        }

        // Consultar la última apertura.
        val lastOpen = stack.last

        // Comprobar que los símbolos coincidan.
        if (lastOpen != pairs(current)) {
          return false
        }

        // Retirar la última apertura.
        stack.remove(stack.size - 1)
      }

      i += 1
    }

    // Todos los símbolos deben estar cerrados.
    stack.isEmpty
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Paréntesis correctamente balanceados.
    assert(isBalanced("()") == true)

    // Test 2: Símbolos cerrados en orden incorrecto.
    assert(isBalanced("([)]") == false)

    // Test 3: Corchetes correctamente anidados.
    assert(isBalanced("{[]}") == true)

    println("All tests passed")
  }
}