// P04: Balanced Parentheses — Scala 2.13
//
// Características utilizadas:
// - val: todo el estado es inmutable.
// - colecciones inmutables: List y Map.
// - map(): transforma los caracteres de entrada.
// - filter(): conserva únicamente los caracteres válidos.
// - flatMap(): construye la secuencia final de caracteres.
// - foldLeft(): procesa funcionalmente toda la cadena manteniendo
//   un estado inmutable.
// - @tailrec: procesa recursivamente la pila de forma segura cuando
//   se necesita obtener su elemento superior.
//
// No se utilizan var, mutable.* ni bucles imperativos.

import scala.annotation.tailrec

object P04BalancedParentheses_v213 {

  private val openingChars = Set('(', '[', '{')

  private val pairs: Map[Char, Char] = Map(
    ')' -> '(',
    ']' -> '[',
    '}' -> '{'
  )

  private final case class State(
      stack: List[Char],
      valid: Boolean
  )

  @tailrec
  private def topOfStack(stack: List[Char]): Option[Char] = {
    stack match {
      case head :: _ => Some(head)
      case Nil       => None
    }
  }

  def isBalanced(s: String): Boolean = {

    val characters: List[Char] =
      s.toList
        .map(identity)
        .filter(c => openingChars.contains(c) || pairs.contains(c))
        .flatMap(c => List(c))

    val initialState = State(Nil, valid = true)

    val finalState = characters.foldLeft(initialState) {
      case (state, _) if !state.valid =>
        state

      case (state, c) if openingChars.contains(c) =>
        State(c :: state.stack, valid = true)

      case (state, c) =>
        val top = topOfStack(state.stack)

        val matches = top.exists(opening =>
          pairs.get(c).contains(opening)
        )

        if (matches) {
          State(state.stack.tail, valid = true)
        } else {
          State(state.stack, valid = false)
        }
    }

    finalState.valid && finalState.stack.isEmpty
  }

  def main(args: Array[String]): Unit = {
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)

    println("All tests passed")
  }
}