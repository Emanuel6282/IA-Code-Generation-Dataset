// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Rediseño de Colecciones (Scala 2.13): Se utilizan los métodos explícitos de mutación `addOne` para el Map y el ListBuffer. Este método fue introducido en Scala 2.13 para estandarizar la adición de un solo elemento en colecciones mutables (reemplazando la sobrecarga semántica del operador `+=`).
// 2. Colecciones Mutables Explícitas: Se emplea estrictamente `mutable.ListBuffer` (simulando una pila mediante inserción/borrado al final) y `mutable.Map` según los requisitos.
// 3. Paradigma Iterativo Puro: Se utiliza un bucle `while` tradicional manejado por un contador `var i`, así como una bandera `var isValid` para interrumpir tempranamente el ciclo. Se omite completamente el uso de pattern matching recursivo, closures o Streams iteradores funcionales.

import scala.collection.mutable

object P04BalancedParentheses_v213_Iterative {

  def isBalanced(s: String): Boolean = {
    // Inicialización usando las factorías estándar recomendadas en 2.13
    val stack = mutable.ListBuffer.empty[Char]
    val pairs = mutable.Map.empty[Char, Char]
    
    // addOne es el método idiomático de mutación en Scala 2.13+
    pairs.addOne(')' -> '(')
    pairs.addOne(']' -> '[')
    pairs.addOne('}' -> '{')

    var i = 0
    var isValid = true

    while (i < s.length && isValid) {
      val c = s.charAt(i)

      if (c == '(' || c == '[' || c == '{') {
        // Apilamos el elemento al final del ListBuffer
        stack.addOne(c)
      } else if (pairs.contains(c)) {
        if (stack.isEmpty) {
          isValid = false
        } else {
          // Extraemos el tope de la pila (último elemento insertado)
          val top = stack.last
          
          // Eliminamos el último elemento (O(1) en implementaciones indexadas recientes o ListBuffer optimizado)
          stack.remove(stack.length - 1)
          
          if (top != pairs(c)) {
            isValid = false
          }
        }
      }
      i += 1
    }

    // Para ser válido, no debe haber fallado tempranamente y la pila debe quedar vacía
    isValid && stack.isEmpty
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: un solo par balanceado
    assert(isBalanced("()") == true, "Error: '()' deberia retornar true")

    // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
    assert(isBalanced("([)]") == false, "Error: '([)]' deberia retornar false")

    // 3. Caso del ejemplo: anidamiento profundo balanceado
    assert(isBalanced("{[]}") == true, "Error: '{[]}' deberia retornar true")

    // 4. Caso adicional explícito en los requisitos
    assert(isBalanced("") == true, "Error: Una cadena vacia deberia retornar true")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}