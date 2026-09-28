// P04: Balanced Parentheses — Scala 3.6 (paradigma iterativo)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.collection.mutable

def isBalanced(s: String): Boolean =
  val paresCierre: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')
  val pila: mutable.Stack[Char] = mutable.Stack.empty[Char]

  var indice: Int = 0
  var balanceado: Boolean = true

  while indice < s.length && balanceado do
    val caracter = s(indice)

    if caracter == '(' || caracter == '[' || caracter == '{' then
      pila.push(caracter)
    else if paresCierre.contains(caracter) then
      if pila.isEmpty then
        balanceado = false
      else if pila.top != paresCierre(caracter) then
        balanceado = false
      else
        pila.pop()

    indice += 1

  if pila.nonEmpty then
    balanceado = false

  balanceado

@main def runTests(): Unit =
  // Caso 1: balanceado simple
  assert(isBalanced("()") == true)

  // Caso 2: orden incorrecto de cierre -> no balanceado
  assert(isBalanced("([)]") == false)

  // Caso 3: anidamiento balanceado de varios tipos
  assert(isBalanced("{[]}") == true)

  println("Todos los casos de prueba pasaron correctamente.")