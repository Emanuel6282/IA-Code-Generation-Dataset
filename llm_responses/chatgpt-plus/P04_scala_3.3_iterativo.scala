// P04: Balanced Parentheses — Scala 3.3
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación significativa (Scala 3):
//    Permite definir bloques sin llaves.
//
// 2. while ... do (Scala 3):
//    Utiliza la sintaxis moderna para bucles iterativos.
//
// 3. if ... then (Scala 3):
//    Simplifica las estructuras condicionales.
//
// 4. mutable.ListBuffer[Char]:
//    Implementa una pila mutable de aperturas.
//
// 5. mutable.Map[Char, Char]:
//    Almacena las correspondencias entre símbolos.
//
// 6. var:
//    Permite actualizar el índice del recorrido.
//
// Compatible con Scala 3.3.
// Sin map(), filter(), foldLeft() ni recursión.
// Biblioteca estándar únicamente.

import scala.collection.mutable

object P04BalancedParentheses_v33:

  def isBalanced(s: String): Boolean =

    // Pila mutable de símbolos de apertura.
    val stack = mutable.ListBuffer.empty[Char]

    // Mapa mutable de correspondencias.
    val pairs = mutable.Map[Char, Char]()

    pairs(')') = '('
    pairs(']') = '['
    pairs('}') = '{'

    // Índice mutable para el recorrido.
    var i = 0

    // Bucle estrictamente iterativo.
    while i < s.length do

      val current = s.charAt(i)

      // Símbolo de apertura.
      if current == '(' ||
         current == '[' ||
         current == '{' then

        stack += current

      // Símbolo de cierre.
      else if pairs.contains(current) then

        // Verificar que exista una apertura previa.
        if stack.isEmpty then
          return false

        // Consultar la última apertura.
        val lastOpen = stack.last

        // Verificar correspondencia.
        if lastOpen != pairs(current) then
          return false

        // Eliminar la última apertura.
        stack.remove(stack.size - 1)

      // Avanzar al siguiente carácter.
      i += 1

    // La pila debe quedar vacía.
    stack.isEmpty

  def main(args: Array[String]): Unit =

    // Test 1: Paréntesis balanceados.
    assert(isBalanced("()") == true)

    // Test 2: Cierres en orden incorrecto.
    assert(isBalanced("([)]") == false)

    // Test 3: Corchetes correctamente anidados.
    assert(isBalanced("{[]}") == true)

    println("All tests passed")