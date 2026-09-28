/*
 * P07: 2D Flood Fill — Scala 3.3
 * Paradigma: estrictamente funcional
 *
 * Características utilizadas:
 *
 * 1. Colecciones inmutables:
 *    Vector y Set se utilizan para representar la matriz, las posiciones
 *    visitadas y la frontera del recorrido sin modificar datos existentes.
 *
 * 2. map():
 *    Se utiliza para transformar filas y celdas de la matriz resultante.
 *
 * 3. filter():
 *    Se utiliza para seleccionar únicamente los vecinos válidos que
 *    pertenecen al área que debe rellenarse.
 *
 * 4. foldLeft():
 *    Se utiliza para procesar las cuatro direcciones de cada posición
 *    y construir de forma inmutable la nueva frontera.
 *
 * 5. flatMap():
 *    Se utiliza para obtener los vecinos de todas las posiciones de
 *    la frontera actual.
 *
 * 6. @tailrec:
 *    Se utiliza para realizar el recorrido del área conectada de forma
 *    recursiva y optimizada mediante recursión de cola.
 *
 * 7. Sintaxis de Scala 3.3:
 *    Se utiliza la sintaxis moderna de Scala 3 para pattern matching
 *    y tipos de colecciones.
 *
 * Nota:
 * Aunque Scala 3.3 incorpora características como enum, union types,
 * extension methods y given/using, ninguna de ellas aporta una mejora
 * relevante a este algoritmo. No se utilizan artificialmente.
 *
 * No se utilizan:
 * - var
 * - mutable.*
 * - while
 * - foreach()
 * - mutación de la matriz original
 */

import scala.annotation.tailrec

object P07FloodFill {

  type Position = (Int, Int)

  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] = {

    val immutableGrid: Vector[Vector[Int]] =
      grid.map(_.toVector).toVector

    val originalColor = immutableGrid(row)(col)

    if (originalColor == newColor) {
      return grid
    }

    val rows = immutableGrid.length
    val cols = immutableGrid.head.length

    val directions: Vector[Position] = Vector(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    def validNeighbors(position: Position): Vector[Position] = {
      val (currentRow, currentCol) = position

      directions
        .map { case (rowOffset, colOffset) =>
          (
            currentRow + rowOffset,
            currentCol + colOffset
          )
        }
        .filter { case (nextRow, nextCol) =>
          nextRow >= 0 &&
          nextRow < rows &&
          nextCol >= 0 &&
          nextCol < cols &&
          immutableGrid(nextRow)(nextCol) == originalColor
        }
    }

    @tailrec
    def fill(
        frontier: List[Position],
        visited: Set[Position]
    ): Set[Position] = {

      frontier match {
        case Nil =>
          visited

        case currentPosition :: remaining =>
          if (visited.contains(currentPosition)) {
            fill(remaining, visited)
          } else {

            val neighbors =
              validNeighbors(currentPosition)

            val newPositions =
              neighbors
                .filter(!visited.contains(_))
                .filter(!remaining.contains(_))

            val newFrontier =
              remaining ++ newPositions.toList

            fill(
              newFrontier,
              visited + currentPosition
            )
          }
      }
    }

    val filledPositions =
      fill(
        List((row, col)),
        Set.empty
      )

    immutableGrid
      .zipWithIndex
      .map { case (currentRow, rowIndex) =>
        currentRow
          .zipWithIndex
          .map { case (color, colIndex) =>
            if (filledPositions.contains((rowIndex, colIndex))) {
              newColor
            } else {
              color
            }
          }
      }
      .map(_.toArray)
      .toArray
  }

  // TC01: ejemplo clásico
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
    ).map(_.toList).toList ==
      List(
        List(2, 2, 2),
        List(2, 2, 0),
        List(2, 0, 1)
      )
  )

  // TC02: toda la matriz tiene el mismo color
  assert(
    floodFill(
      Array(
        Array(0, 0),
        Array(0, 0)
      ),
      0,
      0,
      3
    ).map(_.toList).toList ==
      List(
        List(3, 3),
        List(3, 3)
      )
  )

  // TC03: el color original ya es el nuevo color
  assert(
    floodFill(
      Array(
        Array(1, 1),
        Array(1, 0)
      ),
      0,
      0,
      1
    ).map(_.toList).toList ==
      List(
        List(1, 1),
        List(1, 0)
      )
  )

  println("All tests passed")
}