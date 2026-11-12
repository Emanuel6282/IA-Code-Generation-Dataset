// P04: Balanced Parentheses — migrado a Scala 3.6
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. scala.util.boundary / break (Scala 3.3+): sale del bucle con `false` en
//    cuanto un cierre no corresponde. Sustituye a la variable bandera `balanced`, que
//    había que consultar en cada vuelta del while y combinar al final con la pila.
// 2. match con patrones alternativos (`'(' | '[' | '{'`): clasifica cada carácter
//    comparándolo con constantes, en lugar de buscarlo con `"([{".contains(c)`, y la
//    rama `case _` deja explícito que los demás caracteres se ignoran.
// 3. Sintaxis sin llaves e `if ... then`, `while ... do` (Scala 3): la estructura la da
//    la indentación.
// 4. Definiciones de nivel superior y @main (Scala 3): isBalanced no necesita un
//    `object` contenedor y el punto de entrada no necesita `def main(args: ...)`.
//
// Nota sobre las novedades de Scala 3.6 (verificado con el compilador 3.6.4):
// - Estable: context bounds con nombre y nueva sintaxis de given (SIP-64). Sirven para
//   abstracciones con type classes; esta función recibe un String concreto, sin ningún
//   tipo genérico que abstraer, así que no aportan nada.
// - Experimentales en 3.6 (requieren -experimental): tuplas con nombre y
//   runtimeChecked. No se usan.

import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

// Se define una sola vez, en lugar de en cada llamada.
private val Pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')

def isBalanced(s: String): Boolean =
  boundary:
    val stack = mutable.Stack[Char]()
    var i = 0
    while i < s.length do
      s(i) match
        case c @ ('(' | '[' | '{') => stack.push(c)
        case c @ (')' | ']' | '}') =>
          if stack.isEmpty || stack.pop() != Pairs(c) then break(false)
        case _ => ()   // otros caracteres se ignoran, igual que en el original
      i += 1
    stack.isEmpty

@main def P04BalancedParentheses_v36(): Unit =
  assert(isBalanced("()"))
  assert(!isBalanced("([)]"))
  assert(isBalanced(""))
  assert(isBalanced("{[]}"))
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6
// 1. `var balanced` + `while (i < s.length && balanced)` + `balanced && stack.isEmpty`
//    -> `boundary` con `break(false)`: el bucle termina en el mismo punto que antes (al
//    primer cierre que no corresponde), pero sin estado extra. `return` dentro del
//    bucle no sería idiomático en Scala 3, y boundary/break es la alternativa oficial.
// 2. `if ("([{".contains(c)) ... else if (")]}".contains(c))` -> match con patrones
//    alternativos y `case _ => ()`: mismas tres situaciones, comparadas contra
//    literales.
// 3. `stack.top != pairs(c)` + `stack.pop()` -> `stack.pop() != Pairs(c)`: lee y quita la
//    cima en una sola operación. Si no coincide, la función sale con false, así que
//    haber desapilado no afecta al resultado.
// 4. `pairs` creado en cada llamada -> constante privada de nivel superior `Pairs`.
// 5. Se conserva mutable.Stack: en Scala 3.6 sigue siendo la pila estándar de la
//    biblioteca (reimplementada sobre ArrayDeque desde 2.13).
// 6. Comportamiento idéntico al original, incluido el tratamiento de caracteres que no
//    son paréntesis (se ignoran), verificado con miles de cadenas aleatorias.
// 7. `object P04BalancedParentheses_v213 { ... }` -> definiciones de nivel superior, y
//    `def main(args: Array[String])` -> `@main def P04BalancedParentheses_v36()`.
// 8. Tests: `assert(x == true)` / `assert(x == false)` -> `assert(x)` / `assert(!x)`,
//    la forma idiomática de comprobar un Boolean. Se conservan los cuatro casos y el
//    texto "All tests passed".
// 9. Se actualizó la cabecera: el código base decía "Scala 2.13".