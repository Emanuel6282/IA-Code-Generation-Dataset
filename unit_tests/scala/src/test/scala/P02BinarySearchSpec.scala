import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P02: Binary Search
 * La IA debe implementar: def binarySearch(arr: Array[Int], target: Int): Int
 */
class P02BinarySearchSpec extends AnyFunSuite with Matchers {

  test("TC01 - Target en el medio")          { Solution.binarySearch(Array(1,3,5,7,9), 5)      shouldBe 2  }
  test("TC02 - Target al inicio")            { Solution.binarySearch(Array(1,3,5,7,9), 1)      shouldBe 0  }
  test("TC03 - Target al final")             { Solution.binarySearch(Array(1,3,5,7,9), 9)      shouldBe 4  }
  test("TC04 - Target no encontrado")        { Solution.binarySearch(Array(1,3,5,7,9), 4)      shouldBe -1 }
  test("TC05 - Array vacio")                 { Solution.binarySearch(Array.empty[Int], 1)      shouldBe -1 }
  test("TC06 - Un solo elemento encontrado") { Solution.binarySearch(Array(1), 1)              shouldBe 0  }
  test("TC07 - Array de longitud par")       { Solution.binarySearch(Array(2,4,6,8,10,12), 10) shouldBe 4  }
}
