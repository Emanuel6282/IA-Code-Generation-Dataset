// P07: 2D Flood Fill — Scala 3.3
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite definir bloques sin llaves obligatorias.
//
// 2. then:
//    Mejora la legibilidad de las expresiones condicionales.
//
// 3. val:
//    Todas las referencias locales son inmutables.
//
// 4. Vector y Set inmutables:
//    Representan la matriz y las posiciones visitadas.
//
// 5. map(), filter(), flatMap() y foldLeft():
//    Procesan los datos de forma declarativa.
//
// 6. @tailrec:
//    Garantiza que la expansión recursiva sea
//    optimizada por el compilador.
//
// 7. Array[Array[Int]]:
//    Mantiene el tipo de entrada y salida solicitado.
//    La matriz recibida nunca se modifica.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, for ni while.

import scala.annotation.tailrec

object FloodFillFunctionalScala33:

  type Position = (Int, Int)

  /**
   * Aplica Flood Fill con conectividad de 4 direcciones.
   *
   * No modifica la matriz recibida.
   * Devuelve una matriz con los colores actualizados.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] =

    // Crear una representación interna inmutable.
    val matrix: Vector[Vector[Int]] =
      grid.iterator.map(_.toVector).toVector

    val originalColor = matrix(row)(col)

    if originalColor == newColor then
      grid
    else
      val rows = matrix.length

      // Arriba, abajo, izquierda y derecha.
      val directions: Vector[Position] = Vector(
        (-1, 0),
        (1, 0),
        (0, -1),
        (0, 1)
      )

      // Obtener vecinos válidos.
      def neighbors(position: Position): Set[Position] =
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

      // Expandir la región conectada.
      @tailrec
      def expand(
          frontier: Set[Position],
          visited: Set[Position]
      ): Set[Position] =

        if frontier.isEmpty then
          visited
        else
          // Obtener los vecinos de la frontera.
          val adjacent: Set[Position] =
            frontier.flatMap(neighbors)

          // Excluir posiciones ya visitadas.
          val nextFrontier: Set[Position] =
            adjacent.foldLeft(Set.empty[Position]) {
              (acc, position) =>
                if visited.contains(position) then
                  acc
                else
                  acc + position
            }

          // Recursión terminal.
          expand(
            nextFrontier,
            visited ++ nextFrontier
          )

      // Posición inicial.
      val start: Position = (row, col)

      val initial: Set[Position] = Set(start)

      // Identificar toda la región conectada.
      val connected: Set[Position] =
        expand(initial, initial)

      // Construir una nueva matriz.
      val result: Vector[Vector[Int]] =
        matrix.zipWithIndex.map { case (values, r) =>
          values.zipWithIndex.map { case (value, c) =>
            if connected.contains((r, c)) then
              newColor
            else
              value
          }
        }

      // Convertir al tipo de retorno solicitado.
      result.map(_.toArray).toArray

  def main(args: Array[String]): Unit =

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