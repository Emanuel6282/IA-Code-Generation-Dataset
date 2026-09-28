import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P07: 2D Flood Fill
 * La IA debe implementar: def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]]
 */
class P07FloodFillSpec extends AnyFunSuite with Matchers {

  test("TC01 - Fill clasico") {
    val grid = Array(Array(1,1,1),Array(1,1,0),Array(1,0,1))
    val result = Solution.floodFill(grid, 1, 1, 2)
    result shouldBe Array(Array(2,2,2),Array(2,2,0),Array(2,0,1))
  }

  test("TC02 - Llenar toda la grilla") {
    val grid = Array(Array(0,0,0),Array(0,0,0))
    val result = Solution.floodFill(grid, 0, 0, 1)
    result shouldBe Array(Array(1,1,1),Array(1,1,1))
  }

  test("TC03 - Mismo color, sin cambios") {
    val grid = Array(Array(1,1,1),Array(1,1,0),Array(1,0,1))
    val result = Solution.floodFill(grid, 1, 1, 1)
    result shouldBe Array(Array(1,1,1),Array(1,1,0),Array(1,0,1))
  }

  test("TC04 - Grilla 1x1") {
    Solution.floodFill(Array(Array(0)), 0, 0, 5) shouldBe Array(Array(5))
  }

  test("TC05 - Celda aislada") {
    val grid = Array(Array(0,1,0),Array(1,0,1),Array(0,1,0))
    val result = Solution.floodFill(grid, 0, 0, 3)
    result shouldBe Array(Array(3,1,0),Array(1,0,1),Array(0,1,0))
  }
}
