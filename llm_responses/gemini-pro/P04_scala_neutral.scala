import scala.annotation.tailrec

object BalancedParentheses {

  /**
   * Verifica si una cadena de paréntesis, corchetes y llaves 
   * está correctamente balanceada y cerrada en orden.
   * 
   * Implementación idiomática en Scala utilizando recursión de cola (Tail Recursion)
   * y Pattern Matching, sin estado mutable.
   */
  def isBalanced(s: String): Boolean = {
    
    @tailrec
    def loop(chars: List[Char], stack: List[Char]): Boolean = chars match {
      // Caso base: si ya no hay caracteres, la cadena está balanceada si la pila está vacía
      case Nil => stack.isEmpty
      
      // Si es un caracter de apertura, lo agregamos al tope de la pila
      case (c @ ('(' | '[' | '{')) :: tail => loop(tail, c :: stack)
      
      // Si es un caracter de cierre, verificamos el tope de la pila mediante pattern matching
      case ')' :: tail => stack match {
        case '(' :: st => loop(tail, st)
        case _         => false // Pila vacía o desajuste de pareja
      }
      case ']' :: tail => stack match {
        case '[' :: st => loop(tail, st)
        case _         => false
      }
      case '}' :: tail => stack match {
        case '{' :: st => loop(tail, st)
        case _         => false
      }
      
      // Ignoramos cualquier otro caracter que no sea paréntesis/corchete (por seguridad)
      case _ :: tail => loop(tail, stack)
    }

    // Iniciamos la recursión convirtiendo el String a una Lista de caracteres
    loop(s.toList, Nil)
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: un solo par balanceado
    assert(isBalanced("()") == true, "Error: '()' debería retornar true")

    // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
    assert(isBalanced("([)]") == false, "Error: '([)]' debería retornar false")

    // 3. Caso del ejemplo: anidamiento profundo balanceado
    assert(isBalanced("{[]}") == true, "Error: '{[]}' debería retornar true")

    // 4. Caso adicional contemplado en los requisitos: cadena vacía
    assert(isBalanced("") == true, "Error: Una cadena vacía debería retornar true")

    println("Todos los casos de prueba pasaron correctamente.")
  }
}