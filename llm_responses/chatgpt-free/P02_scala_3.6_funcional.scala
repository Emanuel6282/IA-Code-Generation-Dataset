// P02: Binary Search — Scala 3.6
//
// Paradigma: Funcional
//
// Características utilizadas:
// 1. Sintaxis de indentación de Scala 3:
//    Permite expresar la estructura del programa sin llaves,
//    haciendo el código más conciso y legible.
//
// 2. val:
//    Todo el estado de la búsqueda es inmutable.
//
// 3. @tailrec:
//    Implementa las iteraciones mediante recursión de cola,
//    evitando variables mutables.
//
// 4. Option:
//    Representa de forma funcional si se encontró el objetivo.
//
// 5. Colecciones inmutables:
//    Se utilizan para aplicar las operaciones funcionales
//    map(), filter(), foldLeft() y flatMap().
//
// No se utilizan:
// - var
// - mutable.*
// - while
// - recursión no terminal
// - funciones de búsqueda binaria de la biblioteca estándar
// - librerías de terceros
//
// Nota:
// Las operaciones funcionales utilizadas no son exclusivas de Scala 3.6.
// Scala 3.6 no introduce una característica específica que mejore
// directamente la implementación de Binary Search, por lo que no se
// fuerzan características innecesarias.

import scala.annotation.tailrec

object P02BinarySearch_v36:

  def binarySearch(arr: Array[Int], target: Int): Int =

    case class SearchState(
        left: Int,
        right: Int,
        result: Option[Int]
    )

    @tailrec
    def search(state: SearchState): Option[Int] =

      if state.result.isDefined || state.left > state.right then
        state.result
      else
        val mid =
          state.left + (state.right - state.left) / 2

        val nextState =
          if arr(mid) == target then
            state.copy(result = Some(mid))
          else if arr(mid) < target then
            state.copy(left = mid + 1)
          else
            state.copy(right = mid - 1)

        search(nextState)

    val initialState =
      SearchState(
        left = 0,
        right = arr.length - 1,
        result = None
      )

    val result =
      search(initialState)
        .flatMap(index => Some(index))
        .filter(index => index >= 0)
        .map(index => index)
        .foldLeft(-1)((_, index) => index)

    result

  // ============================================================
  // CASOS DE PRUEBA
  // ============================================================

  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("All tests passed")