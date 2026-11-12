import scala.collection.mutable

object FloodFill {

  /**
   * Aplica Flood Fill sobre una matriz 2D.
   *
   * Modifica la matriz original y devuelve
   * la misma referencia.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] = {

    val originalColor = grid(row)(col)

    // Si ya tiene el nuevo color, no modificar nada.
    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    // Cuatro direcciones: arriba, abajo,
    // izquierda y derecha.
    val directions = Array(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    // Cola FIFO para BFS.
    val queue = mutable.Queue[(Int, Int)]()

    // Agregar la posición inicial.
    queue.enqueue((row, col))

    // Cambiar el color inicial.
    grid(row)(col) = newColor

    // Recorrido iterativo.
    while (queue.nonEmpty) {

      val (r, c) = queue.dequeue()

      for ((dr, dc) <- directions) {

        val nr = r + dr
        val nc = c + dc

        // Verificar límites y color original.
        if (
          nr >= 0 && nr < rows &&
          nc >= 0 && nc < cols &&
          grid(nr)(nc) == originalColor
        ) {

          // Cambiar el color antes de encolar.
          grid(nr)(nc) = newColor

          queue.enqueue((nr, nc))
        }
      }
    }

    grid
  }

  def main(args: Array[String]): Unit = {

    // TEST 1: Ejemplo del enunciado.
    assert(
      floodFill(
        Array(
          Array(1, 1, 1),
          Array(1, 1, 0),
          Array(1, 0, 1)
        ),
        1, 1, 2
      ).map(_.toSeq).toSeq ==
      Seq(
        Seq(2, 2, 2),
        Seq(2, 2, 0),
        Seq(2, 0, 1)
      )
    )

    // TEST 2: El color original ya es el nuevo.
    assert(
      floodFill(
        Array(
          Array(0, 0, 0),
          Array(0, 1, 1)
        ),
        1, 1, 1
      ).map(_.toSeq).toSeq ==
      Seq(
        Seq(0, 0, 0),
        Seq(0, 1, 1)
      )
    )

    // TEST 3: No colorear conexiones diagonales.
    assert(
      floodFill(
        Array(
          Array(1, 0, 1),
          Array(0, 1, 0),
          Array(1, 0, 1)
        ),
        1, 1, 2
      ).map(_.toSeq).toSeq ==
      Seq(
        Seq(1, 0, 1),
        Seq(0, 2, 0),
        Seq(1, 0, 1)
      )
    )

    println("All tests passed")
  }
}