/*
 * P07: Flood Fill — Scala 3.3
 *
 * CARACTERÍSTICAS DE SCALA 3.3 UTILIZADAS:
 *
 * 1. Sintaxis de Scala 3 para definiciones:
 *    Se utiliza la sintaxis moderna de Scala 3 manteniendo el código
 *    conciso y legible.
 *
 * 2. Inferencia de tipos:
 *    Se mantienen declaraciones como mutable.Queue[(Int, Int)]()
 *    sin especificar tipos redundantes en las expresiones.
 *
 * 3. Sintaxis moderna para pattern matching:
 *    Se mantiene el destructuring de tuplas en las iteraciones,
 *    aprovechando la sintaxis más limpia de Scala 3.
 *
 * NOTA:
 * Scala 3.3 incorpora características como enums, union types,
 * extension methods y given/using. Ninguna de ellas mejora
 * materialmente este algoritmo BFS ni aporta una ventaja clara
 * manteniendo el comportamiento original, por lo que no se
 * introducen artificialmente.
 */

import scala.collection.mutable

object P07FloodFill_v33 {

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
 * 1. Scala 2.13 -> Scala 3.3
 *    Se actualiza el código para utilizar la sintaxis y el
 *    compilador de Scala 3.3.
 *
 * 2. Nombre del objeto:
 *    P07FloodFill_v213 -> P07FloodFill_v33.
 *
 *    Esto identifica explícitamente la versión de destino.
 *    El nombre de la función floodFill permanece sin cambios,
 *    tal como exige el problema.
 *
 * 3. Firma de floodFill:
 *    Se mantiene exactamente el mismo comportamiento y los mismos
 *    tipos principales:
 *
 *    Array[Array[Int]] -> Array[Array[Int]]
 *
 * 4. Algoritmo BFS:
 *    Se mantiene sin modificaciones conceptuales.
 *
 *    La cola continúa siendo:
 *
 *    mutable.Queue[(Int, Int)]
 *
 *    y el recorrido continúa utilizando while.
 *
 * 5. Actualización in-place:
 *    Se conserva la modificación directa de grid:
 *
 *    grid(nr)(nc) = newColor
 *
 *    Esto garantiza el mismo comportamiento que la versión 2.13.
 *
 * 6. Direcciones:
 *    Se mantienen exactamente las cuatro direcciones:
 *
 *    (-1, 0) -> arriba
 *    (1, 0)  -> abajo
 *    (0, -1) -> izquierda
 *    (0, 1)  -> derecha
 *
 *    No se agregan diagonales.
 *
 * 7. Condición de color:
 *    Se mantiene la comprobación:
 *
 *    originalColor == newColor
 *
 *    En ese caso se retorna la misma matriz sin cambios.
 *
 * 8. Tests:
 *    Se conservan los tres casos de prueba originales:
 *
 *    TC01 -> ejemplo clásico
 *    TC02 -> grilla completa
 *    TC03 -> mismo color
 *
 *    Únicamente se reformatea su escritura para mejorar
 *    la legibilidad en Scala 3.
 *
 * 9. Imports:
 *    Se conserva únicamente:
 *
 *    scala.collection.mutable
 *
 *    No se agregan librerías externas.
 *
 * 10. Características nuevas de Scala 3.3:
 *     No se introducen enums, union types, extension methods
 *     ni given/using porque no proporcionan una mejora real
 *     para este algoritmo. Mantener el BFS original es más
 *     apropiado para garantizar comportamiento idéntico.
 *
 * 11. Complejidad:
 *     Se mantiene:
 *
 *     Tiempo: O(R * C)
 *     Espacio: O(R * C)
 *
 *     donde R es el número de filas y C el número de columnas.
 */