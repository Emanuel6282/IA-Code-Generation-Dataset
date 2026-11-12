// P04: Balanced Parentheses — Scala 3.3
// Migración desde Scala 2.13
// Paradigma: Iterativo con pila mutable
//
// CARACTERÍSTICAS MODERNAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite definir bloques sin llaves, mejorando la legibilidad.
//
// 2. Sintaxis while ... do (Scala 3):
//    Expresa los bucles iterativos de forma más clara.
//
// 3. Sintaxis if ... then (Scala 3):
//    Simplifica las condiciones sin utilizar paréntesis.
//
// 4. Sintaxis object Nombre: (Scala 3):
//    Permite definir objetos mediante indentación.
//
// 5. Colecciones compatibles con Scala 3.3:
//    Se conserva mutable.Stack[Char] y sus operaciones
//    push(), top y pop() para mantener el algoritmo original.
//
// NOTA:
// Estas características sintácticas pertenecen a Scala 3
// y están disponibles en Scala 3.3.
// No son novedades exclusivas de la versión 3.3.

import scala.collection.mutable

object P04BalancedParentheses_v33:

  def isBalanced(s: String): Boolean =

    // Pila mutable original.
    val stack = mutable.Stack[Char]()

    // Correspondencias originales.
    val pairs = Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    // Estado mutable de validación.
    var balanced = true

    // Índice mutable del recorrido.
    var i = 0

    // Bucle iterativo con sintaxis Scala 3.
    while i < s.length && balanced do

      val c = s(i)

      // Símbolo de apertura.
      if "([{".contains(c) then
        stack.push(c)

      // Símbolo de cierre.
      else if ")]}".contains(c) then

        // Comprobar correspondencia.
        if stack.isEmpty || stack.top != pairs(c) then
          balanced = false
        else
          stack.pop()

      // Avanzar al siguiente carácter.
      i += 1

    // Misma condición final que el código original.
    balanced && stack.isEmpty

  def main(args: Array[String]): Unit =

    // Se conservan exactamente los cuatro tests originales.
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)
    assert(isBalanced("{[]}") == true)

    println("All tests passed")

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN SCALA 2.13 -> SCALA 3.3
 *
 * 1. Nombre del objeto:
 *
 *    Antes:
 *      object P04BalancedParentheses_v213 {
 *
 *    Ahora:
 *      object P04BalancedParentheses_v33:
 *
 *    Motivo:
 *    Identificar la versión de destino y utilizar
 *    la sintaxis moderna de Scala 3.
 *
 * 2. Eliminación de llaves de bloques:
 *
 *    Antes:
 *      def isBalanced(s: String): Boolean = {
 *        ...
 *      }
 *
 *    Ahora:
 *      def isBalanced(s: String): Boolean =
 *        ...
 *
 *    Motivo:
 *    Scala 3 admite indentación significativa.
 *
 * 3. Actualización del bucle while:
 *
 *    Antes:
 *      while (i < s.length && balanced) {
 *        ...
 *      }
 *
 *    Ahora:
 *      while i < s.length && balanced do
 *        ...
 *
 *    Motivo:
 *    Utilizar la sintaxis idiomática de Scala 3.
 *
 * 4. Actualización de condiciones:
 *
 *    Antes:
 *      if ("([{".contains(c)) {
 *        ...
 *      }
 *
 *    Ahora:
 *      if "([{".contains(c) then
 *        ...
 *
 *    Motivo:
 *    Simplificar las estructuras condicionales.
 *
 * 5. Conservación de mutable.Stack[Char]:
 *
 *    Se mantienen:
 *      stack.push(c)
 *      stack.top
 *      stack.pop()
 *
 *    Motivo:
 *    Preservar la estructura de datos y la lógica
 *    de pila mutable del algoritmo original.
 *
 * 6. Conservación del estado mutable:
 *
 *    Se mantienen:
 *      var balanced = true
 *      var i = 0
 *
 *    Motivo:
 *    Mantener el comportamiento iterativo y la
 *    terminación anticipada del recorrido.
 *
 * 7. Conservación de las correspondencias:
 *
 *    Se mantiene el Map inmutable con los pares:
 *      ')' -> '('
 *      ']' -> '['
 *      '}' -> '{'
 *
 *    Motivo:
 *    Mantener exactamente las mismas reglas.
 *
 * 8. Conservación de los cuatro tests:
 *
 *    assert(isBalanced("()") == true)
 *    assert(isBalanced("([)]") == false)
 *    assert(isBalanced("") == true)
 *    assert(isBalanced("{[]}") == true)
 *
 *    Motivo:
 *    Verificar los mismos casos del código original.
 *
 * 9. Conservación de la salida:
 *
 *    println("All tests passed")
 *
 *    Motivo:
 *    Mantener el comportamiento observable del programa.
 *
 * COMPLEJIDAD:
 *    Tiempo: O(n).
 *    Espacio auxiliar: O(n).
 *
 * RESULTADO:
 *    Misma lógica y resultados que Scala 2.13,
 *    con sintaxis moderna compatible con Scala 3.3.
 */