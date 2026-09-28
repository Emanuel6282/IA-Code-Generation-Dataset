import scala.collection.mutable

object FloodFill {

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    val originalColor = grid(row)(col)

    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length
    val directions = List((-1, 0), (1, 0), (0, -1), (0, 1))

    val queue = mutable.Queue((row, col))
    grid(row)(col) = newColor

    while (queue.nonEmpty) {
      val (r, c) = queue.dequeue()

      for ((dr, dc) <- directions) {
        val nr = r + dr
        val nc = c + dc

        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid(nr)(nc) == originalColor) {
          grid(nr)(nc) = newColor
          queue.enqueue((nr, nc))
        }
      }
    }

    grid
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado
    val grid1 = Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1))
    assert(floodFill(grid1, 1, 1, 2).map(_.toList).toList ==
      List(List(2, 2, 2), List(2, 2, 0), List(2, 0, 1)), "Fallo caso 1")

    // Caso 2: el color ya es el nuevo color -> sin cambios
    val grid2 = Array(Array(0, 0, 0), Array(0, 1, 1))
    assert(floodFill(grid2, 1, 1, 1).map(_.toList).toList ==
      List(List(0, 0, 0), List(0, 1, 1)), "Fallo caso 2")

    // Caso 3: matriz de un solo color, se rellena por completo
    val grid3 = Array(Array(3, 3), Array(3, 3))
    assert(floodFill(grid3, 0, 0, 7).map(_.toList).toList ==
      List(List(7, 7), List(7, 7)), "Fallo caso 3")

    println("Todos los tests pasaron correctamente.")
  }
}