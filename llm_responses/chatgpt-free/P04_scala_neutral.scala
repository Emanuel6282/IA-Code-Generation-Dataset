object BalancedParentheses {

  def isBalanced(s: String): Boolean = {
    var stack = List.empty[Char]
    var valid = true
    var i = 0

    while (i < s.length && valid) {
      val c = s(i)

      if (c == '(' || c == '[' || c == '{') {
        stack = c :: stack
      } else {
        val expected = c match {
          case ')' => '('
          case ']' => '['
          case '}' => '{'
        }

        if (stack.isEmpty || stack.head != expected) {
          valid = false
        } else {
          stack = stack.tail
        }
      }

      i += 1
    }

    valid && stack.isEmpty
  }

  def main(args: Array[String]): Unit = {
    assert(isBalanced("()") == true)
    assert(isBalanced("([)]") == false)
    assert(isBalanced("{[]}") == true)

    println("All tests passed")
  }
}