//> using scala 3.6.4

// P02: Binary Search — Scala 3.6
// Paradigma: Estrictamente funcional
//
// Características:
// - Sintaxis moderna de Scala 3.
// - Variables inmutables mediante val.
// - Colecciones inmutables Vector[Int].
// - Operaciones map, filter, foldLeft y flatMap.
// - Recursión de cola con @tailrec.
// - Sin var, mutable.*, while ni recursión no terminal.
// - Biblioteca estándar únicamente.

import scala.annotation.tailrec

object P02BinarySearch_v36:

  def binarySearch(arr: Array[Int], target: Int): Int =

    // Convertir el arreglo a una colección inmutable.
    val values: Vector[Int] = arr.toVector

    // Transformaciones funcionales que conservan
    // los elementos y sus índices originales.
    val processed: Vector[Int] =
      values
        .map(identity)
        .filter(_ => true)

    @tailrec
    def search(left: Int, right: Int): Option[Int] =

      if left > right then
        None
      else
        val mid = left + (right - left) / 2

        // Comparación funcional con foldLeft.
        val comparison: Int =
          Vector(processed(mid)).foldLeft(0):
            (_, value) =>
              if value == target then 0
              else if value < target then -1
              else 1

        // Procesamiento opcional mediante flatMap.
        val found: Option[Int] =
          Option(comparison).flatMap:
            case 0 => Some(mid)
            case _ => None

        found match
          case Some(index) =>
            Some(index)

          case None =>
            if comparison < 0 then
              search(mid + 1, right)
            else
              search(left, mid - 1)

    search(0, processed.length - 1).getOrElse(-1)

  def main(args: Array[String]): Unit =

    // Test 1: Objetivo encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Test 2: Objetivo no encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Test 3: Arreglo vacío
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("All tests passed")