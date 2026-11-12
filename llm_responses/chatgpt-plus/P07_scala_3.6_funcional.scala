
// P07: 2D Flood Fill — Scala 3.6
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Bloques definidos sin llaves obligatorias.
//
// 2. then y else:
//    Expresiones condicionales legibles.
//
// 3. val:
//    Referencias locales inmutables.
//
// 4. Vector y Set inmutables:
//    Representación funcional de la matriz y las visitas.
//
// 5. map(), filter(), flatMap(), foldLeft():
//    Transformaciones declarativas sin bucles imperativos.
//
// 6. @tailrec:
//    El compilador verifica la recursión terminal
//    y la optimiza para evitar crecimiento de pila.
//
// 7. Scala 3.6:
//    Se aprovecha la sintaxis moderna de Scala 3.
//    No se requieren características exclusivas de 3.6.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.*, while ni for.

import scala.annotation.tailrec

object FloodFillFunctionalScala36:

  type Position = (Int, Int)

  /**
   * Aplica Flood Fill en cuatro direcciones.
   *
   * No modifica la matriz original.
   * Devuelve una nueva matriz con los colores actualizados.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] =

    // Convertir la entrada a una estructura inmutable.
    val matrix: Vector[Vector[Int]] =
      grid.iterator.map(_.toVector).toVector

    val originalColor = matrix(row)(col)

    // Si el color coincide, devolver sin cambios.
    if originalColor == newColor then
      grid
    else
      val rows = matrix.length

      // Arriba, abajo, izquierda, derecha.
      val directions: Vector[Position] = Vector(
        (-1, 0),
        (1, 0),
        (0, -1),
        (0, 1)
      )

      // Calcular los vecinos válidos.
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
          // Reunir vecinos de la frontera actual.
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

          // Recursión de cola.
          expand(
            nextFrontier,
            visited ++ nextFrontier
          )

      // Posición inicial.
      val start: Position = (row, col)
      val initial: Set[Position] = Set(start)

      // Encontrar toda la región conectada.
      val connected: Set[Position] =
        expand(initial, initial)

      // Construir el resultado sin modificar la entrada.
      val result: Vector[Vector[Int]] =
        matrix.zipWithIndex.map { case (values, r) =>
          values.zipWithIndex.map { case (value, c) =>
            if connected.contains((r, c)) then
              newColor
            else
              value
          }
        }

      // Mantener el tipo de retorno solicitado.
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

    // TC03: Mismo color, sin cambios.
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
