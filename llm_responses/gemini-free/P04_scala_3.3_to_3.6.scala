// Características de Scala 3.6 utilizadas en este refactoring:
// 1. `mutable.ArrayDeque` en lugar de `mutable.Stack`: `mutable.Stack` está obsoleto desde Scala 2.13.
//    `ArrayDeque` es la colección recomendada e idiomática en Scala 3 para operaciones de pila LIFO
//    (`push`, `pop`, `headOption` / `isEmpty`) gracias a su mejor rendimiento en memoria y tiempo constante.
// 2. Sintaxis basada en sangría (Optional Braces - Quiet Syntax): Se eliminan las llaves de bloque ({})
//    en favor de sangría significativa con `then` y `do` para una estructura más limpia e idónea en Scala 3.6.
// 3. Declaraciones de primer nivel (Top-Level Definitions): Eliminación del envoltorio redundante `object P04...`
//    definiendo directamente `isBalanced` y la función ejecutable `@main`.
// 4. Anotación `@main` simplificada: Reemplaza el método imperativo tradicional `def main(args: Array[String])`.

import scala.collection.mutable

def isBalanced(s: String): Boolean =
  // Reemplazo de mutable.Stack (obsoleto) por ArrayDeque
  val stack = mutable.ArrayDeque[Char]()
  val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')
  var balanced = true

  var i = 0
  while i < s.length && balanced do
    val c = s(i)
    if "([{".contains(c) then
      stack.push(c)
    else if ")]}".contains(c) then
      if stack.isEmpty || stack.head != pairs(c) then
        balanced = false
      else
        stack.pop()
    i += 1

  balanced && stack.isEmpty


@main def runTests(): Unit =
  assert(isBalanced("()") == true)
  assert(isBalanced("([)]") == false)
  assert(isBalanced("") == true)
  assert(isBalanced("{[]}") == true)
  println("All tests passed")


// --- Explicación de los cambios ---
// 1. Sustitución de `mutable.Stack` por `mutable.ArrayDeque`:
//    En la biblioteca estándar de Scala 2.13/3.x, `scala.collection.mutable.Stack` fue marcado como deprecated
//    debido a que estaba implementado sobre `List` (poca localidad de referencia). En Scala 3.6 es práctica
//    estándar utilizar `ArrayDeque`, la cual soporta la misma interfaz LIFO (`push`, `pop`, `head`) con
//    buffers de arreglo contiguos de alto rendimiento.
// 2. Transición hacia Optional Braces (Sintaxis sin llaves de Scala 3):
//    Se removieron los bloques envueltos en `{}` en `isBalanced`, `while` e `if`, utilizando la sintaxis de
//    sangría idiomática con palabras clave de control de flujo como `then` y `do`.
// 3. Uso de definiciones de primer nivel (Top-Level Definitions) y `@main`:
//    Se eliminó el objeto `object P04BalancedParentheses_v213` y el método `main` explícito con arreglos de String.
//    En Scala 3.6, tanto las funciones utilitarias como el punto de entrada ejecutable `@main` pueden residir
//    directamente en el nivel superior del archivo.
// 4. Preservación del paradigma iterativo imperativo:
//    Se conservó la lógica exacta de control por condición en el bucle `while`, manteniendo las variables mutables
//    `var i` y `var balanced` para garantizar exactamente el mismo comportamiento y orden de evaluación.