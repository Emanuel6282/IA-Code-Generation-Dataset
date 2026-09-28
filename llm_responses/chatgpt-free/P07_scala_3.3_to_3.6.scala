/*
 * P07: Flood Fill — Scala 3.6
 *
 * CARACTERÍSTICAS DE SCALA 3.6 UTILIZADAS:
 *
 * 1. Sintaxis de Scala 3:
 *    Se utiliza la sintaxis moderna de Scala 3 para mantener las
 *    declaraciones y expresiones concisas y legibles.
 *
 * 2. Inferencia de tipos:
 *    Se aprovecha la inferencia de tipos para evitar anotaciones
 *    redundantes en valores locales y colecciones.
 *
 * 3. Pattern matching sobre tuplas:
 *    Se mantiene el destructuring de las posiciones y direcciones,
 *    proporcionando una forma clara de trabajar con coordenadas.
 *
 * NOTA:
 * Scala 3.6 no introduce una característica específica que mejore
 * materialmente el algoritmo BFS de Flood Fill. Características como
 * enums, union types, extension methods o given/using no aportan una
 * ventaja real en este caso, por lo que no se utilizan artificialmente.
 *
 * El algoritmo continúa siendo iterativo y utiliza mutable.Queue,
 * while y actualización in-place para preservar exactamente el
 * comportamiento de la versión original.
 */

import scala.collection.mutable

object P07FloodFill_v36 {

  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] = {

    val originalColor = grid(row)(col)

    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    val queue = mutable.Queue[(Int, Int)]()
    queue.enqueue((row, col))

    grid(row)(col) = newColor

    val directions = List(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    while (queue.nonEmpty) {

      val (r, c) = queue.dequeue()

      for ((dr, dc) <- directions) {

        val nr = r + dr
        val nc = c + dc

        if (
          nr >= 0 &&
          nr < rows &&
          nc >= 0 &&
          nc < cols &&
          grid(nr)(nc) == originalColor
        ) {
          grid(nr)(nc) = newColor
          queue.enqueue((nr, nc))
        }
      }
    }

    grid
  }

  def main(args: Array[String]): Unit = {

    // TC01: fill clasico
    val r1 = floodFill(
      Array(
        Array(1, 1, 1),
        Array(1, 1, 0),
        Array(1, 0, 1)
      ),
      1,
      1,
      2
    )

    assert(
      r1.deep ==
        Array(
          Array(2, 2, 2),
          Array(2, 2, 0),
          Array(2, 0, 1)
        ).deep
    )

    // TC02: grilla completa
    val r2 = floodFill(
      Array(
        Array(0, 0, 0),
        Array(0, 0, 0)
      ),
      0,
      0,
      1
    )

    assert(
      r2.deep ==
        Array(
          Array(1, 1, 1),
          Array(1, 1, 1)
        ).deep
    )

    // TC03: mismo color
    val r3 = floodFill(
      Array(
        Array(1, 1, 1),
        Array(1, 1, 0),
        Array(1, 0, 1)
      ),
      1,
      1,
      1
    )

    assert(
      r3.deep ==
        Array(
          Array(1, 1, 1),
          Array(1, 1, 0),
          Array(1, 0, 1)
        ).deep
    )

    println("All tests passed")
  }
}

/*
 * ============================================================
 * CAMBIOS REALIZADOS
 * ============================================================
 *
 * 1. Scala 2.13 -> Scala 3.6
 *    El código se actualiza para ser compilado y ejecutado con
 *    Scala 3.6.
 *
 * 2. Nombre del objeto:
 *    P07FloodFill_v213 -> P07FloodFill_v36.
 *
 *    El nombre de la función se mantiene exactamente como
 *    floodFill, cumpliendo el requisito.
 *
 * 3. BFS:
 *    El algoritmo BFS se conserva sin cambios conceptuales.
 *    Se continúa utilizando mutable.Queue y while.
 *
 * 4. Actualización in-place:
 *    Se mantiene:
 *
 *      grid(nr)(nc) = newColor
 *
 *    Esto garantiza que el comportamiento sea idéntico al código
 *    original.
 *
 * 5. Direcciones:
 *    Se conservan exactamente las cuatro direcciones originales:
 *
 *      (-1, 0) -> arriba
 *      (1, 0)  -> abajo
 *      (0, -1) -> izquierda
 *      (0, 1)  -> derecha
 *
 *    No se agregan movimientos diagonales.
 *
 * 6. Condición de color:
 *    Se mantiene la comprobación de que el color original sea
 *    diferente del nuevo color antes de iniciar el recorrido.
 *
 * 7. Pattern matching:
 *    Se conserva el destructuring:
 *
 *      val (r, c) = queue.dequeue()
 *      for ((dr, dc) <- directions)
 *
 *    Esta forma resulta idiomática y legible en Scala 3.
 *
 * 8. Tests:
 *    Se conservan exactamente los tres escenarios originales:
 *
 *      TC01 -> fill clásico
 *      TC02 -> grilla completa
 *      TC03 -> mismo color
 *
 *    Solamente se reformatea su escritura para facilitar la lectura.
 *
 * 9. Imports:
 *    Se mantiene únicamente scala.collection.mutable.
 *    No se utilizan librerías externas.
 *
 * 10. Características específicas de Scala 3.6:
 *     No se agregan características que no tengan una utilidad
 *     real para este algoritmo. Esto evita introducir complejidad
 *     artificial solamente para demostrar una característica del
 *     lenguaje.
 *
 * 11. Complejidad:
 *     El algoritmo mantiene:
 *
 *       Tiempo:  O(R * C)
 *       Espacio: O(R * C)
 *
 *     donde R representa el número de filas y C el número de
 *     columnas de la matriz.
 *
 * 12. Comportamiento:
 *     Los resultados y la lógica del programa son equivalentes
 *     a la implementación original en Scala 2.13.
 */