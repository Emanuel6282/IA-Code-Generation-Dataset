object BinarySearch {

  def binarySearch(arr: Array[Int], target: Int): Int = {
    var left = 0
    var right = arr.length - 1

    while (left <= right) {
      val mid = left + (right - left) / 2

      if (arr(mid) == target) {
        return mid
      } else if (arr(mid) < target) {
        left = mid + 1
      } else {
        right = mid - 1
      }
    }

    -1
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Objetivo encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Test 2: Objetivo no encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Test 3: Arreglo vacío
    assert(binarySearch(Array.empty[Int], 5) == -1)

    println("All tests passed")
  }
}