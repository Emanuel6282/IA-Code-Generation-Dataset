// P02: Binary Search — Scala 2.13
//
// Paradigma: Funcional
//
// Características utilizadas:
// 1. val:
//    Todas las variables son inmutables.
//
// 2. Colecciones inmutables:
//    El estado de la búsqueda se representa mediante una estructura
//    inmutable.
//
// 3. @tailrec:
//    La búsqueda se implementa mediante recursión de cola, evitando
//    el uso de var y manteniendo un consumo constante de stack.
//
// 4. map(), filter(), foldLeft() y flatMap():
//    Se utilizan operaciones funcionales sobre colecciones.
//
// No se utilizan var ni colecciones mutable.*.
// No se utiliza la función de búsqueda binaria de la biblioteca estándar.

import scala.annotation.tailrec

object P02BinarySearch_v213 {

  def binarySearch(arr: Array[Int], target: Int): Int = {

    case class SearchState(
      left: Int,
      right: Int,
      result: Option[Int]
    )

    @tailrec
    def search(state: SearchState): Option[Int] = {
      if (state.result.isDefined) {
        state.result
      } else if (state.left > state.right) {
        None
      } else {
        val mid = state.left + (state.right - state.left) / 2

        val comparison =
          if (arr(mid) == target) {
            Some(mid)
          } else if (arr(mid) < target) {
            None
          } else {
            None
          }

        comparison match {
          case Some(index) =>
            search(state.copy(result = Some(index)))

          case None if arr(mid) < target =>
            search(
              state.copy(left = mid + 1)
            )

          case None =>
            search(
              state.copy(right = mid - 1)
            )
        }
      }
    }

    val initialState =
      SearchState(
        left = 0,
        right = arr.length - 1,
        result = None
      )

    val result =
      search(initialState)
        .map(identity)
        .filter(index => index >= 0)
        .flatMap(index => Some(index))

    result.getOrElse(-1)
  }

  // ============================================================
  // CASOS DE PRUEBA
  // ============================================================

  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("All tests passed")
}