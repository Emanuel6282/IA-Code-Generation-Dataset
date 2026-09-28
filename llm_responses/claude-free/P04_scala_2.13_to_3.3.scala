// P04: Balanced Parentheses — Scala 3.3 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, while ... do, if ... then. Estilo idiomatico recomendado
//    desde Scala 3.0 y consolidado en la version LTS 3.3.
// 2. Definiciones de nivel superior (top-level definitions): isBalanced ya
//    no necesita estar envuelta en un "object" explicito.
// 3. "@main": reemplaza "object { def main(args: Array[String]): Unit =
//    ... }" por una anotacion sobre una funcion de nivel superior.
// 4. Nota de honestidad sobre el paradigma: el codigo base es
//    deliberadamente imperativo (Stack mutable + var balanced + while),
//    pero este ejercicio pide una migracion de version con comportamiento
//    identico, no un cambio de paradigma. Se conserva intencionalmente el
//    estilo imperativo, incluyendo la comprobacion "\"([{\".contains(c)" /
//    "\")]}\"​.contains(c)" tal cual (en lugar de, por ejemplo, un Set o un
//    Map de aperturas), ya que alterar esa logica excederia el alcance de
//    una simple migracion sintactica.

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

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se elimino el "object P04BalancedParentheses_v213 { ... }" que
//    envolvia todo el codigo, aprovechando las definiciones de nivel
//    superior de Scala 3. Reduce anidamiento e indentacion innecesaria sin
//    alterar el comportamiento.
// 2. Se convirtio la sintaxis con llaves a sintaxis por indentacion
//    significativa: "def isBalanced(...) = { ... }" -> sin llaves, "while
//    (...) { ... }" -> "while ... do ...", "if (...) { ... } else { ...
//    }" -> "if ... then ... else ...". Estilo idiomatico vigente en
//    Scala 3.3.
// 3. Se reemplazo "def main(args: Array[String]): Unit = { ... }" dentro
//    del object por "@main def runTests(): Unit = ..." a nivel superior.
// 4. La logica interna se mantuvo exactamente igual: mismo Stack mutable,
//    mismo Map de pares, misma variable "balanced", mismo indice "i" con
//    while, y las mismas comprobaciones de pertenencia con
//    "\"([{\".contains(c)"/"\")]}\"​.contains(c)" — cumpliendo el requisito
//    de comportamiento identico y respetando la intencion original del
//    codigo base (imperativo).
// 5. El nombre de la funcion "isBalanced" y los cuatro casos de prueba con
//    "assert" se conservaron sin cambios logicos, ya que ya eran validos y
//    compatibles en Scala 3.3 tal como estaban escritos en 2.13.