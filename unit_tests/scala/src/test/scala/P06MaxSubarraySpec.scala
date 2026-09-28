import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P06: Maximum Subarray Sum (Kadane)
 * La IA debe implementar: def maxSubarraySum(arr: Array[Int]): Int
 */
class P06MaxSubarraySpec extends AnyFunSuite with Matchers {

  test("TC01 - Kadane clasico")       { Solution.maxSubarraySum(Array(-2,1,-3,4,-1,2,1,-5,4)) shouldBe 6  }
  test("TC02 - Un positivo")          { Solution.maxSubarraySum(Array(1))                     shouldBe 1  }
  test("TC03 - Un negativo")          { Solution.maxSubarraySum(Array(-1))                    shouldBe -1 }
  test("TC04 - Mayormente positivos") { Solution.maxSubarraySum(Array(5,4,-1,7,8))            shouldBe 23 }
  test("TC05 - Todos negativos")      { Solution.maxSubarraySum(Array(-2,-1))                 shouldBe -1 }
  test("TC06 - Todos positivos")      { Solution.maxSubarraySum(Array(1,2,3,4,5))             shouldBe 15 }
  test("TC07 - Subarray en el medio") { Solution.maxSubarraySum(Array(-3,2,-1,4,-2))          shouldBe 5  }
}
