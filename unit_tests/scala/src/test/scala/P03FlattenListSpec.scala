import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P03: Flatten Nested List
 * La IA debe implementar: def flatten(nested: Any): List[Int]
 * donde nested puede ser Int o List[Any] anidada
 */
class P03FlattenListSpec extends AnyFunSuite with Matchers {

  test("TC01 - Anidamiento basico") {
    val input: Any = List(1, List(2,3), List(4, List(5,6)))
    Solution.flatten(input) shouldBe List(1,2,3,4,5,6)
  }

  test("TC02 - Lista vacia") {
    Solution.flatten(List.empty) shouldBe List.empty
  }

  test("TC03 - Ya plana") {
    Solution.flatten(List(1,2,3)) shouldBe List(1,2,3)
  }

  test("TC04 - Profundamente anidada") {
    val input: Any = List(List(1, List(2, List(3, List(4)))))
    Solution.flatten(input) shouldBe List(1,2,3,4)
  }

  test("TC05 - Lista anidada vacia") {
    Solution.flatten(List(List.empty)) shouldBe List.empty
  }
}
