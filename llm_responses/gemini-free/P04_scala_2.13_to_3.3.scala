// Características de Scala 3.3 LTS utilizadas en este refactoring:
// 1. Sintaxis basada en sangría (Optional Braces - Quiet Syntax): Reemplaza las llaves ({}) 
//    por sangría limpia en definiciones, estructuras de control (`while ... do`, `if ... then ... else`).
// 2. Punto de entrada principal con anotación `@main`: Reemplaza la estructura estática del 
//    `object ... extends App` o `object { def main }` de Scala 2 por una función de primer nivel declarativa.
// 3. Importaciones avanzadas / Agrupadas de Scala 3: Uso de `import scala.collection.mutable.ArrayDeque` 
//    o `mutable` adaptado a la sintaxis limpia de Scala 3.
// 4. `mutable.ArrayDeque` / `mutable.Stack`: Uso eficiente de estructuras mutables LIFO alineadas a Scala 3.

import scala.collection.mutable

def isBalanced(s: String): Boolean =
  val stack = mutable.Stack[Char]()
  val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')
  var balanced = true

  var i = 0
  while i < s.length && balanced do
    val c = s(i)
    if "([{".contains(c) then
      stack.push(c)
    else if ")]}".contains(c) then
      if stack.isEmpty || stack.top != pairs(c) then
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
// 1. Eliminación de llaves ({}) mediante Optional Braces (Sintaxis basada en sangría):
//    En Scala 3.3 LTS, las llaves de bloque de `isBalanced`, `while`, e `if` se reemplazan por 
//    sangría significativa y palabras clave descriptivas como `do` y `then`, haciendo el código más legible.
// 2. Reemplazo del contenedor `object P04BalancedParentheses_v213` con método `main`:
//    Se adoptó la anotación `@main def runTests(): Unit`, que genera automáticamente la clase ejecutable 
//    y reduce el boilerplate de Scala 2.13.
// 3. Declaración de funciones de primer nivel (Top-Level Definitions):
//    Tanto `isBalanced` como `runTests` son ahora funciones de primer nivel, eliminando la necesidad 
//    de envolver obligatoriamente la lógica de negocio dentro de un `object` o `class`.
// 4. Mantenimiento del comportamiento imperativo iterativo idéntico:
//    Se preservó el uso del `while` condicional, los punteros mutables `var` y las operaciones `.push()` / `.pop()` 
//    sobre la pila mutable `mutable.Stack[Char]()` para garantizar rendimiento y semántica exactos.