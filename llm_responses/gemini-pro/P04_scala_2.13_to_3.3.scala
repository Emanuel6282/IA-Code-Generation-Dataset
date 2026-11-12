// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis de Indentación Significativa (Braceless Syntax): Se eliminaron por completo las llaves `{}` de las estructuras de control, métodos y objetos. En Scala 3, esto mejora enormemente la legibilidad, haciendo que el código sea más limpio y similar a Python.
// 2. Nuevas palabras clave `then` y `do`: Reemplazan la sintaxis basada en paréntesis/llaves para las sentencias `if` y bucles `while`, respectivamente, siendo el estándar idiomático actual.
// 3. Punto de entrada `@main`: Elimina el código repetitivo (boilerplate) del antiguo bloque `def main(args: Array[String]): Unit` dentro de un objeto singular, permitiendo definir el punto de ejecución directamente como una función de nivel superior.
// 4. `mutable.ArrayDeque`: Aunque se introdujo al final del ciclo 2.13, en el ecosistema de Scala 3 `mutable.Stack` se considera obsoleto y se recomienda estrictamente el uso de `ArrayDeque` por su mayor rendimiento y seguridad (operaciones nativas O(1) sin sincronización heredada).

import scala.collection.mutable

object P04BalancedParentheses_v33:

  def isBalanced(s: String): Boolean =
    // ArrayDeque es el reemplazo moderno y eficiente para Stack
    val stack = mutable.ArrayDeque.empty[Char]
    val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')
    var balanced = true

    var i = 0
    // Scala 3: Bucle while usando 'do' sin llaves
    while i < s.length && balanced do
      val c = s(i)
      
      // Scala 3: Condicional if usando 'then' sin llaves
      if "([{".contains(c) then
        stack.append(c) // push moderno
      else if ")]}".contains(c) then
        if stack.isEmpty || stack.last != pairs(c) then
          balanced = false
        else
          stack.removeLast() // pop moderno
      i += 1
      
    balanced && stack.isEmpty

// Scala 3: Definición directa del punto de entrada
@main def runRefactoredTests(): Unit =
  assert(P04BalancedParentheses_v33.isBalanced("()") == true)
  assert(P04BalancedParentheses_v33.isBalanced("([)]") == false)
  assert(P04BalancedParentheses_v33.isBalanced("") == true)
  assert(P04BalancedParentheses_v33.isBalanced("{[]}") == true)
  println("All tests passed")

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.3:

1. Transición a Optional Braces:
   El código original de Scala 2.13 dependía fuertemente de bloques delimitados por `{}` para el `while`, el `if`, y las clases. La migración a la sintaxis basada en indentación de Scala 3 (`if ... then`, `while ... do`) recorta el ruido visual, alineando a Scala con lenguajes de sintaxis limpia y previniendo errores comunes de anidamiento de llaves.

2. @main vs Objeto App/Main:
   El código base definía un método estático `def main(args: Array[String]): Unit`. En Scala 3, usar la anotación `@main` instruye al compilador a generar todo ese envoltorio por nosotros. El método simplemente se ubica en el nivel superior (fuera de la clase/objeto principal).

3. Refactorización de la Pila (Stack):
   La clase `mutable.Stack` (presente en el código base) es un artefacto antiguo en Scala que acarrea problemas de diseño (como heredar características indeseadas). Migrar la misma lógica exacta a `mutable.ArrayDeque` usando `append()` (para simular push) y `removeLast()` / `last` (para simular pop y peek) mantiene el rendimiento iterativo intacto pero utilizando la estructura de datos que exige el estado del arte en Scala 3.
*/