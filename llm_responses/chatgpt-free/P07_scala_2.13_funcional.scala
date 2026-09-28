/*

* 2D Flood Fill — Scala 2.13
* Paradigma: estrictamente funcional
*
* Características utilizadas:
* 1. val y colecciones inmutables para evitar estado mutable.
* 2. map() para transformar filas y celdas.
* 3. filter() para seleccionar vecinos válidos.
* 4. flatMap() para generar los vecinos de las posiciones.
* 5. foldLeft() para construir nuevos estados sin mutación.
* 6. @tailrec para realizar la expansión de la región sin
* utilizar bucles imperativos.
*
* No se utilizan:
* * var
* * mutable.*
* * for/while
* * foreach()
* * recursión no optimizada mediante @tailrec
    */

import scala.annotation.tailrec

object FloodFill {

type Position = (Int, Int)

private val directions: List[Position] = List(
(-1, 0), // arriba
(1, 0),  // abajo
(0, -1), // izquierda
(0, 1)   // derecha
)

private def validNeighbors(
position: Position,
grid: Vector[Vector[Int]],
originalColor: Int
): List[Position] = {

```
val rows = grid.length
val cols = grid.head.length

directions
  .map { case (rowOffset, colOffset) =>
    (
      position._1 + rowOffset,
      position._2 + colOffset
    )
  }
  .filter { case (row, col) =>
    row >= 0 &&
    row < rows &&
    col >= 0 &&
    col < cols &&
    grid(row)(col) == originalColor
  }

}

private def expandRegion(
grid: Vector[Vector[Int]],
originalColor: Int,
visited: Set[Position],
frontier: List[Position]
): Set[Position] = {

@tailrec
def expand(
    currentVisited: Set[Position],
    currentFrontier: List[Position]
): Set[Position] = {

  currentFrontier match {

    case Nil =>
      currentVisited

    case position :: remaining =>
      if (currentVisited.contains(position)) {
        expand(
          currentVisited,
          remaining
        )
      } else {

        val neighbors = validNeighbors(
          position,
          grid,
          originalColor
        )

        val newPositions = neighbors
          .filter(
            neighbor => !currentVisited.contains(neighbor)
          )

        val updatedVisited =
          currentVisited + position

        val updatedFrontier =
          remaining ++ newPositions

        expand(
          updatedVisited,
          updatedFrontier
        )
      }
  }
}

expand(visited, frontier)
}

def floodFill(
grid: Array[Array[Int]],
row: Int,
col: Int,
newColor: Int
): Array[Array[Int]] = {

val originalColor = grid(row)(col)

if (originalColor == newColor) {
  grid
} else {

  val immutableGrid: Vector[Vector[Int]] =
    grid.map(_.toVector).toVector

  val start: Position = (row, col)

  val filledPositions = expandRegion(
    immutableGrid,
    originalColor,
    Set.empty,
    List(start)
  )

  immutableGrid
    .map { currentRow =>
      currentRow
        .zipWithIndex
        .map { case (color, colIndex) =>
          val position = (
            row = immutableGrid.indexOf(currentRow),
            col = colIndex
          )

          if (filledPositions.contains(position)) {
            newColor
          } else {
            color
          }
        }
    }
    .toArray
    .map(_.toArray)
}

}

def main(args: Array[String]): Unit = {

// TC01: fill clásico
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

// TC02: fill completo
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

// TC03: mismo color, sin cambios
assert(
  floodFill(
    Array(
      Array(1, 1, 1),
      Array(1, 1, 0),
      Array(1, 0, 1)
    ),
    1,
    1,
    1
  ).deep == Array(
    Array(1, 1, 1),
    Array(1, 1, 0),
    Array(1, 0, 1)
  ).deep
)

println("All tests passed")
}
}
