// Scala 2.13 - Paradigma Iterativo Estricto
// Implementación imperativa utilizando `mutable.ListBuffer`, `mutable.Map`, 
// variables reasignables `var` y bucles `while` de control de flujo.

import scala.collection.mutable

object Solution {

  def isBalanced(s: String): Boolean = {
    // Mapa mutable para la correspondencia de delimitadores (Cierre -> Apertura)
    val pairs: mutable.Map[Char, Char] = mutable.Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    // Estructura de pila mutable basada en ListBuffer
    val stack: mutable.ListBuffer[Char] = mutable.ListBuffer.empty[Char]

    var i: Int = 0
    var balanced: Boolean = true

    while (i < s.length && balanced) {
      val ch: Char = s.charAt(i)

      if (ch == '(' || ch == '[' || ch == '{') {
        // Apilar al inicio del ListBuffer
        stack.prepend(ch)
      } else if (pairs.contains(ch)) {
        if (stack.isEmpty) {
          balanced = false
        } else {
          val top: Char = stack.remove(0)
          if (top != pairs(ch)) {
            balanced = false
          }
        }
      } else {
        // Carácter fuera del conjunto esperado
        balanced = false
      }

      i += 1
    }

    balanced && stack.isEmpty
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: Ejemplos base del enunciado
    assert(isBalanced("()"), "Falló ()")
    assert(!isBalanced("([)]"), "Falló ([)]")
    assert(isBalanced("{[]}"), "Falló {[]}")

    // Caso 2: Cadena vacía y estructuras compuestas válidas
    assert(isBalanced(""), "Falló cadena vacía")
    assert(isBalanced("()[]{}"), "Falló ()[]{}")

    // Caso 3: Desbalance por aperturas/cierres huérfanos
    assert(!isBalanced("((()"), "Falló ((()")
    assert(!isBalanced("]"), "Falló ]")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}