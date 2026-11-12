// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. scala.util.boundary y break (Nativo en 3.6+): Permite eliminar la necesidad de variables de estado ("flags" como `var balanced = true`). En Scala 3.6, usar `boundary` es la forma segura a nivel de tipos y de alto rendimiento para escapar tempranamente de un bloque imperativo (early return), haciendo el flujo más directo.
// 2. Sintaxis de Indentación Significativa (Braceless Syntax): Se eliminan completamente las llaves `{}`. Uso estricto de `while ... do` y `if ... then`, lo cual es el estándar idiomático y visualmente más limpio en Scala 3.6.
// 3. Reemplazo de mutable.Stack por mutable.ArrayDeque: En el ecosistema moderno de Scala, `mutable.Stack` está obsoleto por sus problemas de herencia. `ArrayDeque` es la estructura LIFO/FIFO estándar actual por su eficiencia.
// 4. Punto de entrada @main: Elimina el boilerplate del método estático main heredado de Java, encapsulando la ejecución directamente a nivel superior.

import scala.collection.mutable.ArrayDeque
import scala.util.boundary
import scala.util.boundary.break

object P04BalancedParentheses_v36:

  def isBalanced(s: String): Boolean =
    val stack = ArrayDeque.empty[Char]
    val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')

    var i = 0
    // boundary define un límite del que podemos escapar prematuramente retornando un Boolean
    boundary:
      while i < s.length do
        val c = s(i)
        
        if "([{".contains(c) then
          stack.append(c) // Reemplaza a push()
        else if ")]}".contains(c) then
          // Verificación y escape temprano (early return) usando break
          if stack.isEmpty || stack.last != pairs(c) then
            break(false)
          else
            stack.removeLast() // Reemplaza a pop()
            
        i += 1
      
      // Si el bucle termina sin escapes, evaluamos si la pila quedó vacía
      stack.isEmpty

// @main genera automáticamente el código de arranque (entry point)
@main def runRefactoredTests36(): Unit =
  assert(P04BalancedParentheses_v36.isBalanced("()") == true)
  assert(P04BalancedParentheses_v36.isBalanced("([)]") == false)
  assert(P04BalancedParentheses_v36.isBalanced("") == true)
  assert(P04BalancedParentheses_v36.isBalanced("{[]}") == true)
  println("All tests passed")

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.6:

1. Implementación de `boundary` y `break`:
   En el código de Scala 2.13, se dependía de `var balanced = true` para salir de la condición del `while` en la siguiente iteración. Scala 3 introdujo `scala.util.boundary`, que en la versión 3.6 está totalmente consolidado para reemplazar estas variables bandera. Permite abortar el bloque y retornar `false` inmediatamente sin lanzar excepciones en tiempo de ejecución, mejorando tanto la claridad del flujo como la eficiencia.

2. Limpieza Visual (Optional Braces):
   Se migró el bloque monolítico lleno de llaves y paréntesis a una estructura basada puramente en la indentación. El uso de `then` y `do` guía la lectura de arriba hacia abajo de forma mucho más natural.

3. Actualización de Estructuras de Datos:
   El uso de `mutable.Stack` fue reemplazado por `ArrayDeque`. `ArrayDeque` utiliza un arreglo circular subyacente que no sufre de la sincronización bloqueante de la clase heredada antigua, haciendo que las operaciones `append` (push) y `removeLast` (pop) sean operaciones estrictamente de tiempo constante O(1).
*/