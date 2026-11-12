import scala.collection.mutable

object BalancedParentheses {

  /** Mapeo cierre -> apertura. Es privado y nunca se modifica después de crearlo. */
  private val Pairs: mutable.Map[Char, Char] = mutable.Map(')' -> '(', ']' -> '[', '}' -> '{')

  /** Retorna true si los paréntesis, corchetes y llaves de `s` están balanceados y se
    * cierran en el orden correcto; false en caso contrario.
    *
    * @throws IllegalArgumentException si `s` contiene un carácter que no es ( ) [ ] { }
    */
  def isBalanced(s: String): Boolean = {
    // mutable.ArrayDeque (nuevo en Scala 2.13): buffer circular con inserción y
    // eliminación O(1) en ambos extremos; aquí se usa como pila.
    val stack = mutable.ArrayDeque.empty[Char]
    var balanced = true
    var i = 0
    // El bucle termina al recorrer la cadena o en cuanto se detecta un error, sin
    // necesidad de `return` (poco idiomático en Scala).
    while (balanced && i < s.length) {
      val c = s.charAt(i)
      if (Pairs.contains(c)) {
        // removeLastOption (nuevo en Scala 2.13): desapila devolviendo un Option, que
        // está vacío si la pila lo estaba; así un cierre sin apertura también falla.
        balanced = stack.removeLastOption().contains(Pairs(c))
      } else if (c == '(' || c == '[' || c == '{') {
        stack.append(c)                         // apertura: se apila
      } else {
        throw new IllegalArgumentException(s"Carácter no válido: '$c'")
      }
      i += 1
    }
    balanced && stack.isEmpty                   // quedan aperturas sin cerrar -> no balanceada
  }

  def main(args: Array[String]): Unit = {
    val testCases = mutable.ListBuffer.empty[(String, String, Boolean)]
    // Caso 1: ejemplos balanceados del enunciado
    testCases += (("paréntesis simples", "()", true))
    testCases += (("anidamiento mixto", "{[]}", true))
    // Caso 2: cierre en orden incorrecto (ejemplo del enunciado)
    testCases += (("orden incorrecto", "([)]", false))
    // Caso 3: aperturas sin cerrar y cierres sin abrir
    testCases += (("apertura sin cerrar", "((", false))
    testCases += (("cierre sin abrir", "())", false))

    assert(isBalanced(""), "cadena vacía -> balanceada")

    var i = 0
    while (i < testCases.length) {
      val (name, input, expected) = testCases(i)
      val result = isBalanced(input)
      assert(result == expected, s"Caso '$name' (\"$input\"): esperado $expected, obtenido $result")
      i += 1
    }

    println("Todas las pruebas pasaron.")
  }
}