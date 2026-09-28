// Scala 3.6 - Paradigma Funcional Estricto
// Solución pura que aprovecha la sintaxis basada en sangría de Scala 3 (Optional Braces),
// recursión de cola con `@tailrec`, Pattern Matching y estructuras inmutables (`List`, `Map`, `Option`).

import scala.annotation.tailrec

// Mapa inmutable de delimitadores de cierre a sus correspondientes aperturas
private val Pairs: Map[Char, Char] = Map(
  ')' -> '(',
  ']' -> '[',
  '}' -> '{'
)

/**
 * Verifica si una cadena de paréntesis está balanceada utilizando una función recursiva
 * de cola (`@tailrec`) con coincidencia de patrones (Pattern Matching) y apilamiento inmutable.
 */
def isBalanced(s: String): Boolean =
  // Transformación funcional inmutable previa
  val validChars: List[Char] = s.toList.filter(c => "()[]{}".contains(c))

  @tailrec
  def loop(remaining: List[Char], stack: List[Char]): Boolean =
    (remaining, stack) match
      // Caso base: se procesó toda la cadena. Es balanceado si la pila inmutable quedó vacía
      case (Nil, currentStack) =>
        currentStack.isEmpty

      // Caracteres de apertura: se agregan a la cabeza de la lista inmutable (push)
      case (head :: tail, currentStack) if head == '(' || head == '[' || head == '{' =>
        loop(tail, head :: currentStack)

      // Caracteres de cierre: desapilar si coincide con el tope de la pila (pop)
      case (head :: tail, top :: stackTail) if Pairs.get(head).contains(top) =>
        loop(tail, stackTail)

      // Carácter de cierre sin correspondencia o pila vacía antes de tiempo
      case _ =>
        false

  loop(validChars, Nil)


@main def runTests(): Unit =
  // Caso 1: Ejemplos base del enunciado
  assert(isBalanced("()") == true, "Falló ()")
  assert(isBalanced("([)]") == false, "Falló ([)]")
  assert(isBalanced("{[]}") == true, "Falló {[]}")

  // Caso 2: Cadena vacía y secuencias compuestas válidas
  assert(isBalanced("") == true, "Falló cadena vacía")
  assert(isBalanced("()[]{}") == true, "Falló ()[]{}")

  // Caso 3: Desbalance por aperturas/cierres huérfanos
  assert(isBalanced("((()") == false, "Falló ((()")
  assert(isBalanced("]") == false, "Falló ]")

  println("¡Todos los casos de prueba pasaron correctamente!")