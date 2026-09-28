// P02: Binary Search — Scala
//
// Implementación iterativa de búsqueda binaria.
// El arreglo debe estar ordenado ascendentemente.

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


// ============================================================
// CASOS DE PRUEBA
// ============================================================

assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
assert(binarySearch(Array.empty[Int], 1) == -1)

println("All tests passed")