/*
 * P07: 2D Flood Fill — Scala 3.6
 * Paradigma: estrictamente iterativo
 *
 * Características utilizadas:
 *
 * 1. Sintaxis de Scala 3.6:
 *    Se utiliza la sintaxis moderna de Scala 3.6 manteniendo una
 *    implementación imperativa e iterativa.
 *
 * 2. var:
 *    Se utiliza para mantener y actualizar los índices y el estado
 *    del recorrido.
 *
 * 3. mutable.ListBuffer:
 *    Se utiliza como cola para implementar el recorrido BFS.
 *
 * 4. mutable.Map:
 *    Se utiliza para almacenar las cuatro direcciones posibles.
 *
 * 5. while:
 *    Se utiliza para controlar todo el recorrido y evitar las
 *    abstracciones funcionales.
 *
 * Nota:
 * Scala 3.6 incorpora mejoras en diferentes áreas del lenguaje,
 * pero ninguna característica exclusiva de 3.6 mejora materialmente
 * este algoritmo Flood Fill bajo las restricciones del paradigma
 * iterativo. Por ello no se utilizan características nuevas de
 * forma artificial.
 *
 * No se utilizan:
 * - map()
 * - filter()
 * - foldLeft()
 * - foreach()
 * - recursion
 * - colecciones mutable distintas de las solicitadas
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

    // Si el color ya es el nuevo color, no se realiza ningún cambio.
    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    // Cola BFS implementada con ListBuffer.
    val queue = mutable.ListBuffer[(Int, Int)]()
    queue += ((row, col))

    // Marcamos la posición inicial inmediatamente.
    grid(row)(col) = newColor

    // Las cuatro direcciones permitidas.
    val directions = mutable.Map[Int, (Int, Int)](
      0 -> (-1, 0), // arriba
      1 -> (1, 0),  // abajo
      2 -> (0, -1), // izquierda
      3 -> (0, 1)   // derecha
    )

    var queueIndex = 0

    // Recorrido BFS completamente iterativo.
    while (queueIndex < queue.length) {

      val currentRow = queue(queueIndex)._1
      val currentCol = queue(queueIndex)._2

      queueIndex += 1

      var directionIndex = 0

      while (directionIndex < directions.size) {

        val direction = directions(directionIndex)

        val nextRow = currentRow + direction._1
        val nextCol = currentCol + direction._2

        // Verificamos que la posición esté dentro de la matriz
        // y que tenga el color original.
        if (
          nextRow >= 0 &&
          nextRow < rows &&
          nextCol >= 0 &&
          nextCol < cols &&
          grid(nextRow)(nextCol) == originalColor
        ) {

          // Marcamos inmediatamente la celda para evitar
          // agregarla varias veces a la cola.
          grid(nextRow)(nextCol) = newColor

          queue += ((nextRow, nextCol))
        }

        directionIndex += 1
      }
    }

    grid
  }

  // ============================================================
  // TESTS
  // ============================================================

  // TC01: ejemplo clásico
  val grid1 = Array(
    Array(1, 1, 1),
    Array(1, 1, 0),
    Array(1, 0, 1)
  )

  assert(
    floodFill(grid1, 1, 1, 2).sameElements(
      Array(
        Array(2, 2, 2),
        Array(2, 2, 0),
        Array(2, 0, 1)
      )
    )
  )

  // TC02: toda la matriz pertenece a la misma región
  val grid2 = Array(
    Array(0, 0),
    Array(0, 0)
  )

  assert(
    floodFill(grid2, 0, 0, 3).sameElements(
      Array(
        Array(3, 3),
        Array(3, 3)
      )
    )
  )

  // TC03: el color original ya es el nuevo color
  val grid3 = Array(
    Array(1, 1),
    Array(1, 0)
  )

  assert(
    floodFill(grid3, 0, 0, 1).sameElements(
      Array(
        Array(1, 1),
        Array(1, 0)
      )
    )
  )

  println("All tests passed")
}

/*
 * Cambios y decisiones:
 *
 * 1. Se utiliza mutable.ListBuffer como cola BFS para cumplir
 *    explícitamente el requisito del paradigma iterativo.
 *
 * 2. Se utiliza mutable.Map para representar las cuatro
 *    direcciones de movimiento.
 *
 * 3. Se utilizan variables var para los índices de la cola y
 *    de las direcciones.
 *
 * 4. El recorrido utiliza exclusivamente while; no se utilizan
 *    map(), filter(), foldLeft(), foreach() ni recursión.
 *
 * 5. La celda se marca con newColor cuando se agrega a la cola.
 *    Esto evita procesar una misma celda varias veces.
 *
 * 6. Se mantiene la matriz original como estructura mutable y
 *    se modifica in-place, tal como permite el paradigma iterativo.
 *
 * 7. La conectividad es exclusivamente de cuatro direcciones:
 *    arriba, abajo, izquierda y derecha.
 *
 * 8. Si originalColor == newColor, la matriz se retorna sin cambios.
 *
 * 9. Se mantienen tres casos de prueba con assert.
 *
 * Complejidad:
 * Tiempo: O(R * C)
 * Espacio adicional: O(R * C) en el peor caso por la cola BFS.
 */