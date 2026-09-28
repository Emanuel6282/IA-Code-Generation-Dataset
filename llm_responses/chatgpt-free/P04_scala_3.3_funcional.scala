// P04: Balanced Parentheses — Scala 3.3
//
// Características utilizadas:
// - val: todo el estado es inmutable.
// - colecciones inmutables: List y Map.
// - map(): transforma la representación de los caracteres.
// - filter(): conserva únicamente los caracteres válidos.
// - flatMap(): construye la colección de caracteres a procesar.
// - foldLeft(): procesa la cadena manteniendo un estado inmutable.
// - @tailrec: verifica de forma recursiva y optimizada que la pila
//   final no contenga elementos sin cerrar.
//
// No se utilizan var, mutable.*, while, for ni bucles imperativos.

import scala.annotation.tailrec

object P04BalancedParentheses_v33 {

  private val openingChars: Set[Char] =
    Set('(', '[', '{')

  private val pairs: Map[Char, Char] =
    Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

  private final case class State(
      stack: List[Char],
      valid: Boolean
  )

  @tailrec
  private def stackIsEmpty(stack: List[Char]): Boolean =
    stack match
      case Nil          => true
      case _ :: tail    => stackIsEmpty(tail)

  def isBalanced(s: String): Boolean = {

    val characters: List[Char] =
      s.toList
        .map(identity)
        .filter(c => openingChars.contains(c) || pairs.contains(c))
        .flatMap(c => List(c))

    val initialState =
      State(Nil, valid = true)

    val finalState =
      characters.foldLeft(initialState) {
        case (state, _) if !state.valid =>
          state

        case (state, c) if openingChars.contains(c) =>
          State(
            c :: state.stack,
            valid = true
          )

        case (state, c) =>
          state.stack match
            case opening :: remaining =>
              val matches =
                pairs.get(c).contains(opening)

              if matches then
                State(
                  remaining,
                  valid = true
                )
              else
                State(
                  state.stack,
                  valid = false
                )

            case Nil =>
              State(
                Nil,
                valid = false
              )
      }

    finalState.valid && stackIsEmpty(finalState.stack)
  }

  def main(args: Array[String]): Unit = {
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)

    println("All tests passed")
  }
}