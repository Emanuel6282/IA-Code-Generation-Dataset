// P02: Binary Search — Scala 2.13
//
// Paradigma: Iterativo
//
// Características utilizadas:
// 1. var:
//    Permite mantener y actualizar los límites de búsqueda.
// 2. while:
//    Se utiliza para repetir las iteraciones de la búsqueda binaria
//    sin recurrencia ni operaciones funcionales.
//
// No se utilizan map(), filter(), foldLeft() ni recursión.
// Tampoco se utiliza ninguna función de búsqueda binaria de la
// biblioteca estándar.

object P02BinarySearch_v213 {

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
}