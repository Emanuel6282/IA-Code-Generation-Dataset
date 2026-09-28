import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P04: Balanced Parentheses
 * La IA debe implementar: def isBalanced(s: String): Boolean
 */
class P04BalancedParenthesesSpec extends AnyFunSuite with Matchers {

  test("TC01 - Simple balanceado")           { Solution.isBalanced("()")     shouldBe true  }
  test("TC02 - Multiples tipos balanceados") { Solution.isBalanced("()[]{}") shouldBe true  }
  test("TC03 - Tipos mezclados mal cerrados"){ Solution.isBalanced("(]")     shouldBe false }
  test("TC04 - Intercalados")                { Solution.isBalanced("([)]")   shouldBe false }
  test("TC05 - Anidado balanceado")          { Solution.isBalanced("{[]}")   shouldBe true  }
  test("TC06 - Cadena vacia")                { Solution.isBalanced("")       shouldBe true  }
  test("TC07 - Sin cerrar")                  { Solution.isBalanced("(")      shouldBe false }
  test("TC08 - Profundamente anidado")       { Solution.isBalanced("((()))") shouldBe true  }
}
