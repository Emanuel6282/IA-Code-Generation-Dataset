//> using scala 3.6.4

// P02: Binary Search — Scala 3.6
// Paradigma: Estrictamente iterativo
//
// Características:
// - Sintaxis de indentación de Scala 3.
// - Variables mutables mediante var.
// - Bucle iterativo while ... do.
// - Condicionales if ... then ... else.
// - Sin recursión ni operaciones funcionales.
// - Biblioteca estándar únicamente.

object P02BinarySearch_v36:

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