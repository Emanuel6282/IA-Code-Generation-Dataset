// P04: Balanced Parentheses — Scala 2.13
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. val:
//    Define referencias inmutables.
//
// 2. List[Char]:
//    Implementa una pila inmutable.
//
// 3. Map[Char, Char]:
//    Relaciona los cierres con sus aperturas.
//
// 4. map(), filter(), flatMap():
//    Realizan transformaciones funcionales.
//
// 5. foldLeft():
//    Acumula el estado sin utilizar variables mutables.
//
// 6. @tailrec:
//    Garantiza que la función recursiva auxiliar
//    pueda optimizarse mediante recursión de cola.
//
// 7. Scala 2.13:
//    Se utiliza la API de colecciones de Scala 2.13,
//    que ofrece una organización más uniforme de las
//    operaciones sobre colecciones.
//
// Sin var, mutable.*, for ni while.
// Biblioteca estándar únicamente.

import scala.annotation.tailrec

object P04BalancedParentheses_v213 {

  def isBalanced(s: String): Boolean = {

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

    // Función recursiva auxiliar con optimización de cola.
    @tailrec
    def process(
        remaining: List[Char],
        stack: List[Char]
    ): Boolean = {

      remaining match {

        // Todos los caracteres fueron procesados.
        case Nil =>
          stack.isEmpty

        // Símbolo de apertura.
        case c :: tail if "([{".contains(c) =>
          process(tail, c :: stack)

        // Símbolo de cierre.
        case c :: tail =>
          stack match {

            case top :: rest if pairs.get(c).contains(top) =>
              process(tail, rest)

            case _ =>
              false
          }
      }
    }

    // Acumulación funcional para comprobar si
    // todos los caracteres pertenecen al conjunto válido.
    val validCharacters = characters.foldLeft(true) {
      (valid, c) =>
        valid && "()[]{}".contains(c)
    }

    validCharacters && process(characters, Nil)
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Paréntesis correctamente balanceados.
    assert(isBalanced("()") == true)

    // Test 2: Símbolos cerrados en orden incorrecto.
    assert(isBalanced("([)]") == false)

    // Test 3: Corchetes correctamente anidados.
    assert(isBalanced("{[]}") == true)

    println("All tests passed")
  }
}