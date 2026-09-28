// P04: Balanced Parentheses — Scala 3.6
//
// Características utilizadas:
//
// 1. Sintaxis basada en indentación:
//    Reduce llaves innecesarias y mejora la legibilidad del código.
//
// 2. if ... then y while ... do:
//    Sintaxis moderna de Scala 3 para expresiones condicionales
//    y bucles.
//
// 3. mutable.ArrayDeque:
//    Se utiliza como pila mutable. Es una estructura apropiada para
//    operaciones eficientes sobre los extremos y reemplaza el uso
//    de mutable.Stack.
//
// 4. Inferencia de tipos:
//    Reduce declaraciones redundantes manteniendo el tipado estático.
//
// Nota: Scala 3.6 no introduce una característica específica que
// mejore materialmente este algoritmo. Por ello, se utilizan las
// características idiomáticas de Scala 3 compatibles con 3.6 sin
// introducir complejidad innecesaria.

import scala.collection.mutable

object P04BalancedParentheses_v36:

  def isBalanced(s: String): Boolean =
    val stack = mutable.ArrayDeque.empty[Char]

    val pairs = Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    var balanced = true
    var i = 0

    while i < s.length && balanced do
      val c = s(i)

      if "([{".contains(c) then
        stack.append(c)
      else if ")]}".contains(c) then
        if stack.isEmpty || stack.last != pairs(c) then
          balanced = false
        else
          stack.removeLast()

      i += 1

    balanced && stack.isEmpty

  def main(args: Array[String]): Unit =
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)
    assert(isBalanced("{[]}") == true)

    println("All tests passed")

/*
 * Cambios realizados y por qué son idiomáticos en Scala 3.6:
 *
 * 1. mutable.Stack -> mutable.ArrayDeque
 *    ArrayDeque es una estructura adecuada para implementar una pila
 *    utilizando operaciones sobre uno de sus extremos.
 *
 *    stack.push(c) -> stack.append(c)
 *    stack.top     -> stack.last
 *    stack.pop()   -> stack.removeLast()
 *
 * 2. Sintaxis basada en indentación
 *    Se eliminan las llaves innecesarias de object, métodos y bloques
 *    de control. Esta es una forma idiomática de escribir Scala 3.
 *
 * 3. if ... then
 *    Se utiliza la sintaxis condicional moderna de Scala 3.
 *
 * 4. while ... do
 *    Se utiliza la sintaxis moderna de Scala 3 manteniendo el algoritmo
 *    estrictamente iterativo.
 *
 * 5. Map inmutable
 *    El mapa pairs nunca se modifica, por lo que se utiliza Map en lugar
 *    de mutable.Map.
 *
 * 6. Se mantiene la misma lógica:
 *    - Los caracteres de apertura se agregan a la pila.
 *    - Cada carácter de cierre se compara con el elemento superior.
 *    - Una discrepancia produce false.
 *    - Al finalizar, la pila debe estar vacía.
 *
 * 7. Se mantiene exactamente el nombre de la función: isBalanced.
 *
 * 8. Se conservan exactamente los cuatro casos de prueba del código base.
 *
 * 9. El comportamiento y los resultados son idénticos al código original.
 *
 * 10. No se utilizan características funcionales como map(), filter()
 *     o foldLeft(), porque esta migración conserva el paradigma
 *     iterativo del código base.
 */