object BalancedParentheses {

  def isBalanced(s: String): Boolean = {
    val pairs = Map(
      ')' -> '(',
      ']' -> '[',
      '}' -> '{'
    )

    val result = s.foldLeft(Option(List.empty[Char])) {
      case (None, _) =>
        None

      case (Some(stack), char) if "([{".contains(char) =>
        Some(char :: stack)

      case (Some(head :: tail), char)
          if pairs.get(char).contains(head) =>
        Some(tail)

      case _ =>
        None
    }

    result.contains(Nil)
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Paréntesis correctamente balanceados.
    assert(isBalanced("()") == true)

    // Test 2: Símbolos cerrados en orden incorrecto.
    assert(isBalanced("([)]") == false)

    // Test 3: Corchetes correctamente anidados.
    assert(isBalanced("{[]}") == true)

    println("All tests passed")
  }
}