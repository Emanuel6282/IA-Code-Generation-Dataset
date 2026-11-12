// P02: Binary Search — Scala 3.3
// Paradigma: Estrictamente iterativo
//
// Características:
// - Sintaxis moderna de Scala 3.
// - Uso de var para variables mutables.
// - Uso de while ... do.
// - Uso de if ... then ... else.
// - Sin map(), filter(), foldLeft() ni recursión.
// - Biblioteca estándar únicamente.

object P02BinarySearch_v33:

  def binarySearch(arr: Array[Int], target: Int): Int =
    var left = 0
    var right = arr.length - 1

    while left <= right do
      val mid = left + (right - left) / 2

      if arr(mid) == target then
        return mid
      else if arr(mid) < target then
        left = mid + 1
      else
        right = mid - 1

    -1

  def main(args: Array[String]): Unit =

    // Test 1: Objetivo encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Test 2: Objetivo no encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Test 3: Arreglo vacío
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("All tests passed")