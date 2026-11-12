// P04: Balanced Parentheses — Scala 3.3
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación significativa (Scala 3):
//    Define bloques sin necesidad de llaves.
//
// 2. if ... then (Scala 3):
//    Permite escribir condiciones de forma más legible.
//
// 3. val y colecciones inmutables:
//    Evitan modificar el estado durante el procesamiento.
//
// 4. map(), filter() y flatMap():
//    Transforman los caracteres funcionalmente.
//
// 5. foldLeft():
//    Acumula estados inmutables durante el recorrido.
//
// 6. @tailrec:
//    Garantiza la optimización de la recursión de cola.
//
// 7. Option y pattern matching:
//    Representan y procesan estados válidos o inválidos.
//
// Compatible con Scala 3.3.
// Sin var, mutable.*, for ni while.
// Biblioteca estándar únicamente.

import scala.annotation.tailrec

object P04BalancedParentheses_v33:

  def isBalanced(s: String): Boolean =

    // Correspondencias inmutables.
    val pairs: Map[Char, Char] = Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    // Transformaciones funcionales.
    val characters: List[Char] =
      s.toList
        .map(identity)
        .filter(c => "()[]{}".contains(c))
        .flatMap(c => List(c))

    // Estado inmutable: pila de aperturas pendientes.
    val initialState: Option[List[Char]] = Some(Nil)

    // Procesar un carácter sin modificar el estado anterior.
    def process(
        state: Option[List[Char]],
        current: Char
    ): Option[List[Char]] =

      state.flatMap: stack =>
        if "([{".contains(current) then
          Some(current :: stack)
        else
          stack match
            case top :: rest if pairs.get(current).contains(top) =>
              Some(rest)

            case _ =>
              None

    // Acumulación funcional mediante foldLeft.
    val finalState: Option[List[Char]] =
      characters.foldLeft(initialState)(process)

    // Verificación final mediante recursión de cola.
    @tailrec
    def isEmptyStack(stack: List[Char]): Boolean =
      stack match
        case Nil =>
          true

        case _ :: tail =>
          if tail.isEmpty then
            false
          else
            isEmptyStack(tail)

    // El estado debe ser válido y la pila debe estar vacía.
    finalState match
      case Some(stack) =>
        isEmptyStack(stack)

      case None =>
        false

  def main(args: Array[String]): Unit =

    // Test 1: Paréntesis correctamente balanceados.
    assert(isBalanced("()") == true)

    // Test 2: Símbolos cerrados en orden incorrecto.
    assert(isBalanced("([)]") == false)

    // Test 3: Corchetes correctamente anidados.
    assert(isBalanced("{[]}") == true)

    println("All tests passed")