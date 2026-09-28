// P04: Balanced Parentheses — Scala 3.3
//
// Características de Scala 3 utilizadas:
//
// 1. Sintaxis basada en indentación:
//    Elimina llaves innecesarias en bloques de métodos, if y while,
//    haciendo el código más legible.
//
// 2. if ... then ... else:
//    Sintaxis condicional moderna de Scala 3 que hace más explícita
//    la estructura de las condiciones.
//
// 3. mutable.ArrayDeque:
//    Se utiliza como pila mutable en lugar de mutable.Stack.
//    Permite operaciones eficientes de inserción y eliminación
//    en los extremos.
//
// 4. Inferencia de tipos:
//    Se aprovecha la inferencia de tipos de Scala para reducir
//    declaraciones redundantes sin perder tipado estático.
//
// Nota: Scala 3.3 no introduce una característica exclusiva que cambie
// fundamentalmente este algoritmo. Por ello, se aplican mejoras
// idiomáticas de Scala 3 sin modificar la lógica original.

import scala.collection.mutable

object P04BalancedParentheses_v33:

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
 * Cambios realizados y por qué son idiomáticos en Scala 3.3:
 *
 * 1. mutable.Stack -> mutable.ArrayDeque
 *    ArrayDeque es una estructura adecuada para representar una pila
 *    mediante operaciones sobre uno de sus extremos.
 *
 *    stack.push(c)       -> stack.append(c)
 *    stack.top           -> stack.last
 *    stack.pop()         -> stack.removeLast()
 *
 * 2. Sintaxis basada en indentación
 *    Scala 3 permite eliminar muchas llaves de los bloques estructurales.
 *    Esto reduce ruido visual y es una forma idiomática de escribir Scala 3.
 *
 * 3. if ... then
 *    Se utiliza la sintaxis moderna de Scala 3 para las expresiones
 *    condicionales.
 *
 * 4. while ... do
 *    Se utiliza la sintaxis moderna de Scala 3 para el bucle while,
 *    manteniendo el comportamiento estrictamente iterativo del código.
 *
 * 5. Map inmutable
 *    El mapa de pares no se modifica después de su creación, por lo que
 *    Map es suficiente y evita utilizar mutable.Map innecesariamente.
 *
 * 6. Se mantiene la lógica original
 *    Los caracteres de apertura se almacenan en la pila. Cada carácter
 *    de cierre se compara con el elemento superior y, si no coincide,
 *    el resultado pasa a false.
 *
 * 7. Se mantiene exactamente el nombre de la función: isBalanced.
 *
 * 8. Se conservan los mismos cuatro casos de prueba del código original.
 *
 * 9. El comportamiento y los resultados son idénticos al código base.
 */