// Scala 3 - Solución con Pattern Matching y Recursión de Cola (@tailrec)

import scala.annotation.tailrec

def isBalanced(s: String): Boolean =
  val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')

  @tailrec
  def loop(chars: List[Char], stack: List[Char]): Boolean =
    (chars, stack) match
      // Si procesamos todos los caracteres, la cadena es válida solo si la pila está vacía
      case (Nil, _) => 
        stack.isEmpty

      // Caracteres de apertura: se agregan a la cabeza de la pila inmutable
      case (c :: tail, _) if c == '(' || c == '[' || c == '{' => 
        loop(tail, c :: stack)

      // Caracteres de cierre: verificar coincidencia con el tope de la pila
      case (c :: tail, top :: stackTail) if pairs.get(c).contains(top) => 
        loop(tail, stackTail)

      // Carácter de cierre sin correspondencia o pila vacía prematura
      case (c :: _, _) if pairs.contains(c) => 
        false

      // Cualquier otro carácter no esperado
      case _ => 
        false

  loop(s.toList, Nil)


@main def runTests(): Unit =
  // Caso 1: Ejemplos base del enunciado
  assert(isBalanced("()") == true, "Falló ()")
  assert(isBalanced("([)]") == false, "Falló ([)]")
  assert(isBalanced("{[]}") == true, "Falló {[]}")

  // Caso 2: Cadena vacía y secuencias válidas compuestas
  assert(isBalanced("") == true, "Falló cadena vacía")
  assert(isBalanced("()[]{}") == true, "Falló ()[]{}")

  // Caso 3: Desbalance por cierres o aperturas sobrantes
  assert(isBalanced("((()") == false, "Falló ((()")
  assert(isBalanced("]") == false, "Falló ]")

  println("¡Todos los casos de prueba pasaron correctamente!")