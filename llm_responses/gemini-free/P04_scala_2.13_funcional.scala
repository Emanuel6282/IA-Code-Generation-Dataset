// Scala 2.13 - Paradigma Funcional Estricto
// Solución pura empleando recursión de cola (`@tailrec`), Pattern Matching, 
// estructuras inmutables (`ListMap`, `List`) y transformaciones funcionales (`filter`).

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

object Solution {

  // Mapa inmutable de pares de delimitadores (Cierre -> Apertura)
  private val Pairs: ListMap[Char, Char] = ListMap(
    ')' -> '(',
    ']' -> '[',
    '}' -> '{'
  )

  /**
   * Verifica si una cadena de delimitadores está balanceada mediante una función recursiva de cola.
   * Emplea una pila inmutable representada por `List[Char]` y Pattern Matching funcional.
   */
  def isBalanced(s: String): Boolean = {
    // Filtrado de la secuencia utilizando transformaciones funcionales
    val validChars: List[Char] = s.toList.filter(c => "()[]{}".contains(c))

    @tailrec
    def loop(remaining: List[Char], stack: List[Char]): Boolean = {
      (remaining, stack) match {
        // Caso base: no quedan caracteres por procesar; es válido si la pila final está vacía
        case (Nil, currentStack) =>
          currentStack.isEmpty

        // Delimitadores de apertura: se agregan al tope de la pila inmutable
        case (head :: tail, currentStack) if head == '(' || head == '[' || head == '{' =>
          loop(tail, head :: currentStack)

        // Delimitadores de cierre: verificar si coinciden con el tope de la pila
        case (head :: tail, top :: stackTail) if Pairs.get(head).contains(top) =>
          loop(tail, stackTail)

        // Desbalance detectado (cierre con pila vacía o no coincidencia de tipo)
        case _ =>
          false
      }
    }

    loop(validChars, Nil)
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: Ejemplos base provistos en el problema
    assert(isBalanced("()"), "Falló ()")
    assert(!isBalanced("([)]"), "Falló ([)]")
    assert(isBalanced("{[]}"), "Falló {[]}")

    // Caso 2: Cadena vacía y secuencias compuestas válidas
    assert(isBalanced(""), "Falló cadena vacía")
    assert(isBalanced("()[]{}"), "Falló ()[]{}")

    // Caso 3: Desbalance por aperturas/cierres huérfanos
    assert(!isBalanced("((()"), "Falló ((()")
    assert(!isBalanced("]"), "Falló ]")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}