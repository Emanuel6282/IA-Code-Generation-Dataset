// P04: Balanced Parentheses — Scala 3.6
// Migración del código base proporcionado
// Paradigma: Iterativo con pila mutable
//
// CARACTERÍSTICAS MODERNAS UTILIZADAS:
//
// 1. Indentación significativa:
//    Permite definir bloques sin llaves y mejora la legibilidad.
//
// 2. Sintaxis while ... do:
//    Expresa el recorrido iterativo con la sintaxis de Scala 3.
//
// 3. Sintaxis if ... then:
//    Simplifica las condiciones y reduce los delimitadores.
//
// 4. Definición de object mediante dos puntos:
//    Utiliza la sintaxis idiomática de Scala 3.
//
// 5. Inferencia de tipos con val:
//    Mantiene declaraciones concisas y seguras.
//
// NOTA SOBRE LA VERSIÓN:
// Estas características pertenecen a Scala 3 y están
// disponibles en Scala 3.6. No son exclusivas de 3.6.
// No se requieren características experimentales.
//
// Se conservan las operaciones de la pila original
// para mantener el mismo algoritmo y comportamiento.

import scala.collection.mutable

object P04BalancedParentheses_v36:

  def isBalanced(s: String): Boolean =

    // Pila mutable original.
    val stack = mutable.Stack[Char]()

    // Mapa inmutable de correspondencias.
    val pairs = Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    // Estado de validación.
    var balanced = true

    // Índice del recorrido.
    var i = 0

    // Recorrido iterativo.
    while i < s.length && balanced do

      val c = s(i)

      // Símbolo de apertura.
      if "([{".contains(c) then
        stack.push(c)

      // Símbolo de cierre.
      else if ")]}".contains(c) then

        // Verificar la correspondencia.
        if stack.isEmpty || stack.top != pairs(c) then
          balanced = false
        else
          stack.pop()

      // Avanzar al siguiente carácter.
      i += 1

    // Resultado original.
    balanced && stack.isEmpty

  def main(args: Array[String]): Unit =

    // Se conservan los cuatro casos de prueba originales.
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)
    assert(isBalanced("{[]}") == true)

    println("All tests passed")

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN A SCALA 3.6
 *
 * 1. Actualización del nombre del objeto:
 *
 *    Antes:
 *      object P04BalancedParentheses_v213 {
 *
 *    Ahora:
 *      object P04BalancedParentheses_v36:
 *
 *    Motivo:
 *    Identificar la versión de destino y utilizar
 *    la sintaxis moderna de Scala 3.
 *
 * 2. Uso de indentación significativa:
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
 *    Reducir delimitadores y mejorar la legibilidad.
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
 * 4. Actualización de las condiciones:
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
 *    Aprovechar la sintaxis moderna if ... then.
 *
 * 5. Conservación de mutable.Stack:
 *
 *    Se mantienen:
 *      stack.push(c)
 *      stack.top
 *      stack.pop()
 *
 *    Motivo:
 *    Preservar la lógica LIFO del algoritmo original.
 *
 * 6. Conservación de las variables mutables:
 *
 *    Se mantienen:
 *      var balanced = true
 *      var i = 0
 *
 *    Motivo:
 *    Mantener el recorrido iterativo y su condición
 *    de terminación anticipada.
 *
 * 7. Conservación del mapa:
 *
 *    Se mantienen las correspondencias:
 *      ')' -> '('
 *      ']' -> '['
 *      '}' -> '{'
 *
 *    Motivo:
 *    Conservar exactamente las reglas de balanceo.
 *
 * 8. Conservación de los cuatro casos de prueba:
 *
 *    assert(isBalanced("()") == true)
 *    assert(isBalanced("([)]") == false)
 *    assert(isBalanced("") == true)
 *    assert(isBalanced("{[]}") == true)
 *
 *    Motivo:
 *    Verificar los mismos casos del código base.
 *
 * 9. Conservación de la salida:
 *
 *    println("All tests passed")
 *
 *    Motivo:
 *    Mantener el comportamiento observable.
 *
 * 10. Compatibilidad con Scala 3.6:
 *
 *     Se utilizan características estables de Scala 3.
 *     No se introducen dependencias externas ni cambios
 *     algorítmicos innecesarios.
 *
 * COMPLEJIDAD:
 *     Tiempo: O(n).
 *     Espacio auxiliar: O(n).
 *
 * RESULTADO:
 *     Misma lógica y comportamiento del código original,
 *     con sintaxis moderna compatible con Scala 3.6.
 */