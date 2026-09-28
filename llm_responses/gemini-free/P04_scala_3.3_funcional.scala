// Scala 3.3 LTS - Paradigma Funcional Estricto
// Solución funcional pura con sintaxis basada en sangría (Optional Braces),
// recursión de cola con `@tailrec`, Pattern Matching y colecciones inmutables (`List`, `Map`).

import scala.annotation.tailrec

// Tabla inmutable de delimitadores de cierre a sus aperturas asociadas
private val Pairs: Map[Char, Char] = Map(
  ')' -> '(',
  ']' -> '[',
  '}' -> '{'
)

/**
 * Verifica si una cadena de delimitadores está balanceada usando una función
 * auxiliar con recursión de cola y coincidencia de patrones (Pattern Matching).
 */
def isBalanced(s: String): Boolean =
  // Filtrado inmutable previa evaluación funcional
  val validChars: List[Char] = s.toList.filter(c => "()[]{}".contains(c))

  @tailrec
  def loop(remaining: List[Char], stack: List[Char]): Boolean =
    (remaining, stack) match
      // Caso base: se consumió toda la secuencia; la cadena es válida solo si la pila está vacía
      case (Nil, currentStack) =>
        currentStack.isEmpty

      // Caracteres de apertura: se apilan en la cabeza de la lista inmutable
      case (head :: tail, currentStack) if head == '(' || head == '[' || head == '{' =>
        loop(tail, head :: currentStack)

      // Caracteres de cierre: verificar coincidencia con el tope de la pila
      case (head :: tail, top :: stackTail) if Pairs.get(head).contains(top) =>
        loop(tail, stackTail)

      // Carácter de cierre no coincidente o pila vacía prematura
      case _ =>
        false

  loop(validChars, Nil)


@main def runTests(): Unit =
  // Caso 1: Ejemplos base provistos en el problema
  assert(isBalanced("()") == true, "Falló ()")
  assert(isBalanced("([)]") == false, "Falló ([)]")
  assert(isBalanced("{[]}") == true, "Falló {[]}")

  // Caso 2: Cadena vacía y secuencias válidas compuestas
  assert(isBalanced("") == true, "Falló cadena vacía")
  assert(isBalanced("()[]{}") == true, "Falló ()[]{}")

  // Caso 3: Desbalance por aperturas/cierres huérfanos
  assert(isBalanced("((()") == false, "Falló ((()")
  assert(isBalanced("]") == false, "Falló ]")

  println("¡Todos los casos de prueba pasaron correctamente!")