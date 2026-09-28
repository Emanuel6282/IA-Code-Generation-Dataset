// P04: Balanced Parentheses — Scala 2.13 (codigo base para refactoring)
// Estilo: Stack mutable con operaciones push/pop explicitas
import scala.collection.mutable

object P04BalancedParentheses_v213 {

  def isBalanced(s: String): Boolean = {
    val stack = mutable.Stack[Char]()
    val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')
    var balanced = true

    var i = 0
    while (i < s.length && balanced) {
      val c = s(i)
      if ("([{".contains(c)) {
        stack.push(c)
      } else if (")]}".contains(c)) {
        if (stack.isEmpty || stack.top != pairs(c)) {
          balanced = false
        } else {
          stack.pop()
        }
      }
      i += 1
    }
    balanced && stack.isEmpty
  }

  def main(args: Array[String]): Unit = {
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("") == true)
    assert(isBalanced("{[]}") == true)
    println("All tests passed")
  }
}
