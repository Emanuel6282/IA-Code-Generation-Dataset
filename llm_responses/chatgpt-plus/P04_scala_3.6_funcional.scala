// P04: Balanced Parentheses — Scala 3.6
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Indentación significativa (Scala 3):
//    Permite definir bloques sin llaves.
//
// 2. if ... then y pattern matching (Scala 3):
//    Facilitan la expresión declarativa de condiciones.
//
// 3. val y colecciones inmutables:
//    Evitan modificar el estado durante el procesamiento.
//
// 4. map(), filter() y flatMap():
//    Realizan transformaciones funcionales.
//
// 5. foldLeft():
//    Acumula estados inmutables sin bucles imperativos.
//
// 6. @tailrec:
//    Verifica la optimización de la recursión de cola.
//
// 7. Option:
//    Representa un estado válido o inválido.
//
// COMPATIBILIDAD:
// Código compatible con Scala 3.6.
// Las características utilizadas son estables y no
// requieren opciones experimentales del compilador.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, for ni while.

import scala.annotation.tailrec

object P04BalancedParentheses_v36:

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

    // Estado inicial: pila vacía y secuencia válida.
    val initialState: Option[List[Char]] = Some(Nil)

    // Procesa un carácter sin modificar el estado.
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

    // Acumulación funcional de estados inmutables.
    val finalState: Option[List[Char]] =
      characters.foldLeft(initialState)(process)

    // Verificación de pila vacía mediante recursión de cola.
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

    // Resultado final.
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