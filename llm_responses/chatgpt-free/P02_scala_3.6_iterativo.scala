// P02: Binary Search — Scala 3.6
//
// Paradigma: Iterativo
//
// Características utilizadas:
// 1. Sintaxis de Scala 3:
//    Se utiliza la sintaxis moderna de Scala 3, compatible con Scala 3.6.
//
// 2. `var`:
//    Permite actualizar los límites de búsqueda durante cada iteración.
//
// 3. `while`:
//    Implementa el algoritmo de búsqueda binaria de forma iterativa.
//
// 4. `mutable.ListBuffer`:
//    Se utiliza para registrar los índices centrales visitados.
//
// 5. `mutable.Map`:
//    Se utiliza para almacenar información mutable sobre la búsqueda.
//
// Nota:
// Las características específicas introducidas en Scala 3.6 no aportan
// una mejora directa a este algoritmo. Por ello, no se fuerzan
// construcciones innecesarias.
//
// No se utilizan:
// - map()
// - filter()
// - foldLeft()
// - recursión
// - funciones de búsqueda binaria de la biblioteca estándar
// - librerías de terceros

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object P02BinarySearch_v36:

  def binarySearch(arr: Array[Int], target: Int): Int =
    var left = 0
    var right = arr.length - 1
    var result = -1
    var found = false

    val searchHistory = ListBuffer[Int]()
    val searchInfo = Map[String, Int]("iterations" -> 0)

    while left <= right && !found do
      val mid = left + (right - left) / 2

      searchHistory += mid
      searchInfo("iterations") = searchInfo("iterations") + 1

      if arr(mid) == target then
        result = mid
        found = true
      else if arr(mid) < target then
        left = mid + 1
      else
        right = mid - 1

    result

  // ============================================================
  // CASOS DE PRUEBA
  // ============================================================

  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("All tests passed")