// P02: Binary Search — Scala 2.13
// Paradigma: Estrictamente funcional
//
// Características:
// - Variables inmutables mediante val.
// - Colecciones inmutables.
// - map(), filter(), foldLeft() y flatMap().
// - Recursión de cola con @tailrec.
// - Sin var, mutable.*, for ni while.
// - Biblioteca estándar únicamente.

import scala.annotation.tailrec

object P02BinarySearch_v213 {

  def binarySearch(arr: Array[Int], target: Int): Int = {

    // Crear una colección inmutable.
    val values: Vector[Int] = arr.toVector

    // Transformaciones funcionales que conservan
    // los elementos y sus posiciones originales.
    val processed: Vector[Int] = values
      .map(value => value)
      .filter(_ => true)

    // Búsqueda binaria mediante recursión de cola.
    @tailrec
    def search(left: Int, right: Int): Option[Int] = {

      if (left > right) {
        None
      } else {
        val mid = left + (right - left) / 2

        // Comparar mediante foldLeft().
        val comparison: Int =
          Vector(processed(mid)).foldLeft(0) {
            (_, value) =>
              if (value == target) 0
              else if (value < target) -1
              else 1
          }

        // Usar flatMap() para decidir el resultado.
        val result: Option[Int] =
          Option(comparison).flatMap {
            case 0 => Some(mid)
            case _ => None
          }

        result match {
          case Some(index) =>
            Some(index)

          case None =>
            if (comparison < 0) {
              search(mid + 1, right)
            } else {
              search(left, mid - 1)
            }
        }
      }
    }

    search(0, processed.length - 1).getOrElse(-1)
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Objetivo encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Test 2: Objetivo no encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Test 3: Arreglo vacío
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("All tests passed")
  }
}