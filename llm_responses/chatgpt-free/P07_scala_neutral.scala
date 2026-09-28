import scala.collection.mutable.Queue

object FloodFill {

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

val queue = Queue[(Int, Int)]()
queue.enqueue((row, col))

grid(row)(col) = newColor

val directions = Array(
  (-1, 0), // arriba
  (1, 0),  // abajo
  (0, -1), // izquierda
  (0, 1)   // derecha
)

while (queue.nonEmpty) {
  val (currentRow, currentCol) = queue.dequeue()

  directions.foreach { case (rowOffset, colOffset) =>
    val nextRow = currentRow + rowOffset
    val nextCol = currentCol + colOffset

    if (
      nextRow >= 0 &&
      nextRow < rows &&
      nextCol >= 0 &&
      nextCol < cols &&
      grid(nextRow)(nextCol) == originalColor
    ) {
      grid(nextRow)(nextCol) = newColor
      queue.enqueue((nextRow, nextCol))
    }
  }
}

grid

}

def main(args: Array[String]): Unit = {

// Test 1: ejemplo del enunciado
assert(
  floodFill(
    Array(
      Array(1, 1, 1),
      Array(1, 1, 0),
      Array(1, 0, 1)
    ),
    1,
    1,
    2
  ).deep == Array(
    Array(2, 2, 2),
    Array(2, 2, 0),
    Array(2, 0, 1)
  ).deep
)

// Test 2: toda la región tiene el mismo color
assert(
  floodFill(
    Array(
      Array(0, 0),
      Array(0, 0)
    ),
    0,
    0,
    3
  ).deep == Array(
    Array(3, 3),
    Array(3, 3)
  ).deep
)

// Test 3: el color original ya es el nuevo color
assert(
  floodFill(
    Array(
      Array(1, 1),
      Array(1, 0)
    ),
    0,
    0,
    1
  ).deep == Array(
    Array(1, 1),
    Array(1, 0)
  ).deep
)

println("All tests passed")

}
}
