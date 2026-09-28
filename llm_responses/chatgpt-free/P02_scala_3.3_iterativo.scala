// P02: Binary Search — Scala 3.3
//
// Paradigma: Iterativo
//
// Características utilizadas:
// 1. Sintaxis de Scala 3:
//    Se utiliza la sintaxis moderna de Scala 3, compatible con Scala 3.3.
//
// 2. Tipos explícitos:
//    Se mantienen tipos claros para las variables del algoritmo,
//    facilitando la lectura de la búsqueda binaria.
//
// 3. var y while:
//    Se utilizan para implementar estrictamente el paradigma iterativo.
//
// Nota:
// Scala 3.3 incorpora características como extension methods,
// enum, union types, given/using y sintaxis de indentación.
// Ninguna de ellas proporciona una mejora real para este algoritmo,
// por lo que no se introducen artificialmente.
//
// No se utilizan map(), filter(), foldLeft() ni recursión.
// No se utilizan funciones de búsqueda binaria de la biblioteca estándar.

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object P02BinarySearch_v33 {

  def binarySearch(arr: Array[Int], target: Int): Int = {
    var left = 0
    var right = arr.length - 1
    var result = -1
    var found = false

    // Estructuras mutables solicitadas para el paradigma iterativo.
    val searchHistory = ListBuffer[Int]()
    val searchInfo = Map[String, Int](
      "iterations" -> 0
    )

    while (left <= right && !found) {
      val mid = left + (right - left) / 2

      searchHistory += mid
      searchInfo("iterations") = searchInfo("iterations") + 1

      if (arr(mid) == target) {
        result = mid
        found = true
      } else if (arr(mid) < target) {
        left = mid + 1
      } else {
        right = mid - 1
      }
    }

    result
  }

  // ============================================================
  // CASOS DE PRUEBA
  // ============================================================

  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("All tests passed")
}

// ============================================================
// EXPLICACIÓN DE LOS CAMBIOS
// ============================================================
//
// 1. La función conserva exactamente el nombre:
//
//       binarySearch
//
// 2. Se utiliza `var` para representar el estado mutable de la
//    búsqueda:
//
//       left
//       right
//       result
//       found
//
// 3. Se utiliza `while` para realizar las iteraciones de la
//    búsqueda binaria.
//
// 4. Se utilizan `mutable.ListBuffer` y `mutable.Map`, de acuerdo
//    con los requisitos del paradigma iterativo solicitado.
//
//    `searchHistory` almacena los índices `mid` visitados durante
//    la búsqueda y `searchInfo` mantiene información mutable sobre
//    las iteraciones.
//
//    Estas estructuras no son necesarias para el algoritmo en sí,
//    pero se incluyen para cumplir explícitamente el requisito.
//
// 5. No se utilizan:
//
//       map()
//       filter()
//       foldLeft()
//       recursión
//
//    porque pertenecen a un enfoque funcional y no al paradigma
//    iterativo solicitado.
//
// 6. Se conserva el cálculo:
//
//       left + (right - left) / 2
//
//    en lugar de:
//
//       (left + right) / 2
//
//    para evitar un posible overflow en la suma de los límites.
//
// 7. Los tres casos de prueba originales se conservan:
//
//       [1, 3, 5, 7, 9], 5 -> 2
//       [1, 3, 5, 7, 9], 4 -> -1
//       [], 1             -> -1
//
// 8. No se utiliza ninguna función de búsqueda binaria de la
//    biblioteca estándar.
//
// 9. Las características específicas de Scala 3.3 no se fuerzan
//    artificialmente. La sintaxis utilizada es válida en Scala 3.3
//    y mantiene el código sencillo.
//
// 10. Complejidad:
//
//       Tiempo:  O(log n)
//       Espacio: O(log n) por el historial de índices.
//
//    El algoritmo de búsqueda propiamente dicho utiliza O(1)
//    espacio; el historial agregado únicamente sirve para satisfacer
//    el uso solicitado de colecciones mutables.