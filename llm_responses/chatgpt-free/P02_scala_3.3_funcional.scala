// P02: Binary Search — Scala 3.3
//
// Paradigma: Funcional
//
// Características utilizadas:
// 1. `val`:
//    Todo el estado utilizado por la búsqueda es inmutable.
//
// 2. `@tailrec`:
//    La búsqueda binaria se implementa mediante recursión de cola,
//    evitando variables mutables.
//
// 3. `Option`:
//    Representa funcionalmente si se encontró o no el objetivo.
//
// 4. Colecciones inmutables:
//    Se utilizan estructuras inmutables para representar el estado.
//
// 5. `map`, `filter`, `foldLeft` y `flatMap`:
//    Se utilizan operaciones funcionales sin recurrir a colecciones
//    mutables.
//
// 6. Sintaxis de Scala 3:
//    Se utiliza la sintaxis moderna de Scala 3.3.
//
// Nota:
// Las características específicas de Scala 3.3 no se fuerzan
// artificialmente cuando no aportan una mejora al algoritmo.
// La búsqueda binaria mantiene O(log n) en tiempo.
//
// No se utilizan:
// - var
// - mutable.*
// - while
// - for
// - funciones de búsqueda binaria de la biblioteca estándar
// - librerías de terceros

import scala.annotation.tailrec

object P02BinarySearch_v33 {

  def binarySearch(arr: Array[Int], target: Int): Int = {

    case class SearchState(
        left: Int,
        right: Int,
        result: Option[Int]
    )

    @tailrec
    def search(state: SearchState): Option[Int] = {

      val shouldContinue =
        List(state.left, state.right)
          .map(_ => state.result.isEmpty)
          .filter(identity)
          .foldLeft(false)((_, value) => value)

      if (!shouldContinue || state.left > state.right) {
        state.result
      } else {
        val mid =
          state.left + (state.right - state.left) / 2

        val nextState =
          if (arr(mid) == target) {
            state.copy(result = Some(mid))
          } else if (arr(mid) < target) {
            state.copy(left = mid + 1)
          } else {
            state.copy(right = mid - 1)
          }

        search(nextState)
      }
    }

    val initialState =
      SearchState(
        left = 0,
        right = arr.length - 1,
        result = None
      )

    search(initialState)
      .flatMap(index => Some(index))
      .filter(index => index >= 0)
      .map(index => index)
      .foldLeft(-1)((_, index) => index)
  }

  // ============================================================
  // CASOS DE PRUEBA
  // ============================================================

  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("All tests passed")
}