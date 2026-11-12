// P07: 2D Flood Fill — Scala 2.13
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Scala 2.13 — API de colecciones:
//    Uso de Vector y Set inmutables.
//
// 2. val:
//    Todas las variables son inmutables.
//
// 3. map():
//    Construye la matriz resultante.
//
// 4. filter():
//    Selecciona los vecinos válidos.
//
// 5. flatMap():
//    Reúne los vecinos de las posiciones actuales.
//
// 6. foldLeft():
//    Acumula las posiciones de cada frontera.
//
// 7. @tailrec:
//    Garantiza que la recursión sea optimizada
//    como un recorrido iterativo por el compilador.
//
// 8. Array[Array[Int]]:
//    Se conserva como tipo de entrada y salida,
//    pero nunca se modifica la matriz recibida.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, while ni recursión no terminal.

import scala.annotation.tailrec

object FloodFillFunctionalScala213 {

  type Position = (Int, Int)

  /**
   * Aplica Flood Fill con conectividad de 4 direcciones.
   *
   * No modifica la matriz original.
   * Devuelve una nueva matriz con los colores actualizados.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] = {

    // Representación interna inmutable.
    val matrix: Vector[Vector[Int]] =
      grid.iterator.map(_.toVector).toVector

    val originalColor = matrix(row)(col)

    // Si el color coincide, no hay cambios.
    if (originalColor == newColor) {
      return grid
    }

    val rows = matrix.length

    // Cuatro direcciones.
    val directions: Vector[Position] = Vector(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    // Obtener vecinos válidos.
    def neighbors(position: Position): Set[Position] = {

      val (r, c) = position

      directions
        .map { case (dr, dc) =>
          (r + dr, c + dc)
        }
        .filter { case (nr, nc) =>
          nr >= 0 &&
          nr < rows &&
          nc >= 0 &&
          nc < matrix(nr).length &&
          matrix(nr)(nc) == originalColor
        }
        .toSet
    }

    // Expansión funcional mediante recursión de cola.
    @tailrec
    def expand(
        frontier: Set[Position],
        visited: Set[Position]
    ): Set[Position] = {

      if (frontier.isEmpty) {
        visited
      } else {

        // Reunir vecinos mediante flatMap().
        val adjacent: Set[Position] =
          frontier.flatMap(neighbors)

        // Acumular nuevas posiciones mediante foldLeft().
        val nextFrontier: Set[Position] =
          adjacent.foldLeft(Set.empty[Position]) {
            (acc, position) =>
              if (visited.contains(position)) acc
              else acc + position
          }

        // Recursión terminal.
        expand(
          nextFrontier,
          visited ++ nextFrontier
        )
      }
    }

    // Posición inicial.
    val start: Position = (row, col)

    val initial: Set[Position] = Set(start)

    // Encontrar toda la región conectada.
    val connected: Set[Position] =
      expand(initial, initial)

    // Construir una nueva matriz sin mutar la original.
    val result: Vector[Vector[Int]] =
      matrix.zipWithIndex.map {
        case (values, r) =>
          values.zipWithIndex.map {
            case (value, c) =>
              if (connected.contains((r, c))) {
                newColor
              } else {
                value
              }
          }
      }

    // Convertir al tipo de retorno solicitado.
    result.map(_.toArray).toArray
  }

  def main(args: Array[String]): Unit = {

    // TC01: Ejemplo del enunciado.
    val grid1 = Array(
      Array(1, 1, 1),
      Array(1, 1, 0),
      Array(1, 0, 1)
    )

    val result1 = floodFill(grid1, 1, 1, 2)

    assert(
      result1.deep == Array(
        Array(2, 2, 2),
        Array(2, 2, 0),
        Array(2, 0, 1)
      ).deep,
      "TC01 fallo"
    )

    // TC02: Relleno completo.
    val grid2 = Array(
      Array(0, 0, 0),
      Array(0, 0, 0)
    )

    assert(
      floodFill(grid2, 0, 0, 1).deep == Array(
        Array(1, 1, 1),
        Array(1, 1, 1)
      ).deep,
      "TC02 fallo"
    )

    // TC03: El color original ya es el nuevo.
    val grid3 = Array(
      Array(1, 1, 1),
      Array(1, 1, 0),
      Array(1, 0, 1)
    )

    assert(
      floodFill(grid3, 1, 1, 1).deep == Array(
        Array(1, 1, 1),
        Array(1, 1, 0),
        Array(1, 0, 1)
      ).deep,
      "TC03 fallo"
    )

    println("All tests passed")
  }
}