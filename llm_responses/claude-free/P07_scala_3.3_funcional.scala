import scala.annotation.tailrec

def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
  val originalColor = grid(row)(col)

  if originalColor == newColor then
    grid
  else
    val rows = grid.length
    val cols = grid(0).length
    val immutableGrid: Vector[Vector[Int]] = grid.map(_.toVector).toVector

    def neighbors(pos: (Int, Int)): List[(Int, Int)] =
      val (r, c) = pos
      List((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))

    def inBoundsAndSameColor(pos: (Int, Int)): Boolean =
      val (r, c) = pos
      r >= 0 && r < rows && c >= 0 && c < cols && immutableGrid(r)(c) == originalColor

    @tailrec
    def expand(visited: Set[(Int, Int)], frontier: Set[(Int, Int)]): Set[(Int, Int)] =
      if frontier.isEmpty then
        visited
      else
        val newVisited = visited ++ frontier
        val nextFrontier = frontier
          .flatMap(neighbors)
          .filter(inBoundsAndSameColor)
          .filterNot(newVisited.contains)
        expand(newVisited, nextFrontier)

    val filledPositions = expand(Set.empty[(Int, Int)], Set((row, col)))

    val newGrid: Vector[Vector[Int]] = immutableGrid.zipWithIndex.map { case (rowVec, r) =>
      rowVec.zipWithIndex.map { case (value, c) =>
        if filledPositions.contains((r, c)) then newColor else value
      }
    }

    newGrid.map(_.toArray).toArray

@main def runTests(): Unit =
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