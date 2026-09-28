// Scala 3.6 - Paradigma Iterativo Estricto
// Solución imperativa usando la sintaxis basada en sangría de Scala 3,
// colecciones mutables (`mutable.ListBuffer`, `mutable.Map`), variables `var`
// y un bucle `while` tradicional sin recursión ni combinadores funcionales.

import scala.collection.mutable

def isBalanced(s: String): Boolean =
  // Mapa mutable de correspondencia (Cierre -> Apertura)
  val pairs: mutable.Map[Char, Char] = mutable.Map(
    ')' -> '(',
    ']' -> '[',
    '}' -> '{'
  )

  // Estructura de pila mutable basada en ListBuffer
  val stack: mutable.ListBuffer[Char] = mutable.ListBuffer.empty[Char]

  var i: Int = 0
  var balanced: Boolean = true

  while i < s.length && balanced do
    val ch: Char = s.charAt(i)

    if ch == '(' || ch == '[' || ch == '{' then
      // Apilar al frente del ListBuffer
      stack.prepend(ch)
    else if pairs.contains(ch) then
      if stack.isEmpty then
        balanced = false
      else
        val top: Char = stack.remove(0)
        if top != pairs(ch) then
          balanced = false
    else
      // Carácter no perteneciente al conjunto de delimitadores válidos
      balanced = false

    i += 1

  balanced && stack.isEmpty


@main def runTests(): Unit =
  // Caso 1: Ejemplos base del enunciado
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