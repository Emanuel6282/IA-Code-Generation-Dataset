import scala.collection.mutable

object FloodFill {

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    val originalColor = grid(row)(col)

    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    val directionsR = Array(-1, 1, 0, 0)
    val directionsC = Array(0, 0, -1, 1)

    val pending = new mutable.ListBuffer[(Int, Int)]
    val visited = mutable.Map[(Int, Int), Boolean]()

    pending.append((row, col))
    visited((row, col)) = true
    grid(row)(col) = newColor

    while (pending.nonEmpty) {
      val (r, c) = pending.remove(0)

      var i = 0
      while (i < 4) {
        val nr = r + directionsR(i)
        val nc = c + directionsC(i)

        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
            grid(nr)(nc) == originalColor && !visited.contains((nr, nc))) {
          grid(nr)(nc) = newColor
          visited((nr, nc)) = true
          pending.append((nr, nc))
        }

        i += 1
      }
    }

    grid
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado
    val grid1 = Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1))
    val expected1 = Array(Array(2, 2, 2), Array(2, 2, 0), Array(2, 0, 1))
    val result1 = floodFill(grid1, 1, 1, 2)
    var matches1 = true
    var r = 0
    while (r < result1.length) {
      var c = 0
      while (c < result1(r).length) {
        if (result1(r)(c) != expected1(r)(c)) matches1 = false
        c += 1
      }
      r += 1
    }
    assert(matches1, "Fallo caso 1")

    // Caso 2: el color ya es el nuevo color -> sin cambios
    val grid2 = Array(Array(0, 0, 0), Array(0, 1, 1))
    val expected2 = Array(Array(0, 0, 0), Array(0, 1, 1))
    val result2 = floodFill(grid2, 1, 1, 1)
    var matches2 = true
    r = 0
    while (r < result2.length) {
      var c = 0
      while (c < result2(r).length) {
        if (result2(r)(c) != expected2(r)(c)) matches2 = false
        c += 1
      }
      r += 1
    }
    assert(matches2, "Fallo caso 2")

    // Caso 3: matriz de un solo color, se rellena por completo
    val grid3 = Array(Array(3, 3), Array(3, 3))
    val expected3 = Array(Array(7, 7), Array(7, 7))
    val result3 = floodFill(grid3, 0, 0, 7)
    var matches3 = true
    r = 0
    while (r < result3.length) {
      var c = 0
      while (c < result3(r).length) {
        if (result3(r)(c) != expected3(r)(c)) matches3 = false
        c += 1
      }
      r += 1
    }
    assert(matches3, "Fallo caso 3")

    println("Todos los tests pasaron correctamente.")
  }
}