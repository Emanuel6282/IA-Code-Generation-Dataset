// P04: Balanced Parentheses — Scala 3.3 (paradigma funcional)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

/**
 * Dada una cadena que contiene solo los caracteres "(", ")", "[", "]",
 * "{", "}", retorna true si todos los parentesis/corchetes estan
 * correctamente balanceados y cerrados en orden. Retorna false en caso
 * contrario.
 *
 * Implementacion estrictamente funcional: usa foldLeft para recorrer la
 * cadena acumulando el estado de forma inmutable. El acumulador es un
 * Option[List[Char]], donde None representa "ya se detecto un
 * desbalance" (cortocircuito funcional expresado con Option), y
 * Some(pila) representa el estado valido con la pila de aperturas
 * pendientes. La pila (List[Char]) es inmutable: cada paso construye una
 * NUEVA lista, nunca se muta. No se usan var ni mutable.*.
 *
 * @param s Cadena que contiene unicamente los caracteres de parentesis y
 *          corchetes indicados.
 * @return true si la cadena esta correctamente balanceada, false en caso
 *         contrario. Una cadena vacia se considera balanceada (true).
 */
def isBalanced(s: String): Boolean =
  val paresCierre: Map[Char, Char] = Map(')' -> '(', ']' -> '[', '}' -> '{')

  val estadoFinal: Option[List[Char]] = s.foldLeft(Option(List.empty[Char])) { (estadoOpt, caracter) =>
    estadoOpt.flatMap { pila =>
      if caracter == '(' || caracter == '[' || caracter == '{' then
        Some(caracter :: pila)
      else if paresCierre.contains(caracter) then
        pila match
          case cima :: resto if cima == paresCierre(caracter) => Some(resto)
          case _ => None
      else
        Some(pila)
    }
  }

  estadoFinal.exists(_.isEmpty)

@main def runTests(): Unit =
  // Caso 1: balanceado simple
  assert(isBalanced("()") == true)

  // Caso 2: orden incorrecto de cierre -> no balanceado
  assert(isBalanced("([)]") == false)

  // Caso 3: anidamiento balanceado de varios tipos
  assert(isBalanced("{[]}") == true)

  println("Todos los casos de prueba pasaron correctamente.")