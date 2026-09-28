// P04: Balanced Parentheses — Scala 3.6
//
// Características utilizadas:
// - val: todo el estado del algoritmo es inmutable.
// - List, Set y Map: colecciones inmutables.
// - map(): transforma los caracteres de la cadena.
// - filter(): conserva únicamente los caracteres válidos.
// - flatMap(): construye la secuencia final a procesar.
// - foldLeft(): procesa la cadena manteniendo un estado inmutable.
// - @tailrec: realiza la comprobación final de la pila mediante
//   recursión de cola.
//
// No se utilizan var, mutable.*, while, for ni recursión no optimizada.
//
// Scala 3.6 no aporta una característica específica que mejore
// materialmente este algoritmo, por lo que se mantiene una solución
// funcional directa y legible.

import scala.annotation.tailrec

object P04BalancedParentheses_v36 {

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
      case Nil       => true
      case _ :: tail => stackIsEmpty(tail)

  def isBalanced(s: String): Boolean = {

    val characters: List[Char] =
      s.toList
        .map(identity)
        .filter(c => openingChars.contains(c) || pairs.contains(c))
        .flatMap(c => List(c))

    val initialState =
      State(
        stack = Nil,
        valid = true
      )

    val finalState =
      characters.foldLeft(initialState) {
        case (state, _) if !state.valid =>
          state

        case (state, c) if openingChars.contains(c) =>
          State(
            stack = c :: state.stack,
            valid = true
          )

        case (state, c) =>
          state.stack match
            case opening :: remaining =>
              val matches =
                pairs.get(c).contains(opening)

              if matches then
                State(
                  stack = remaining,
                  valid = true
                )
              else
                State(
                  stack = state.stack,
                  valid = false
                )

            case Nil =>
              State(
                stack = Nil,
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