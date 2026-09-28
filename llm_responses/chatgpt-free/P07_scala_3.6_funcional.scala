/*
 * P07: 2D Flood Fill — Scala 3.6
 * Paradigma: estrictamente funcional
 *
 * Características utilizadas:
 *
 * 1. Colecciones inmutables:
 *    Vector y Set representan la matriz, las posiciones visitadas
 *    y la frontera del recorrido sin mutación.
 *
 * 2. map():
 *    Se utiliza para transformar posiciones y reconstruir la matriz.
 *
 * 3. filter():
 *    Se utiliza para conservar únicamente los vecinos válidos y
 *    pertenecientes a la región del color original.
 *
 * 4. foldLeft():
 *    Se utiliza para acumular posiciones y construir el siguiente
 *    estado del recorrido sin utilizar variables mutables.
 *
 * 5. flatMap():
 *    Se utiliza para obtener los vecinos de todas las posiciones
 *    de la frontera actual.
 *
 * 6. @tailrec:
 *    Se utiliza para recorrer iterativamente la región mediante
 *    recursión de cola, permitiendo una implementación funcional.
 *
 * 7. Sintaxis de Scala 3.6:
 *    Se utiliza la sintaxis moderna de Scala 3.6 manteniendo
 *    colecciones y valores inmutables.
 *
 * Nota:
 * Scala 3.6 incorpora características como mejoras de sintaxis,
 * extension methods y otras capacidades del lenguaje. Ninguna
 * característica exclusiva de 3.6 mejora materialmente este
 * algoritmo bajo las restricciones funcionales solicitadas, por
 * lo que no se utilizan características nuevas artificialmente.
 *
 * No se utilizan:
 * - var
 * - mutable.*
 * - while
 * - foreach()
 * - mutación de la matriz
 */

import scala.annotation.tailrec

object P07FloodFill_v36 {

  type Position = (Int, Int)

  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] = {

    val immutableGrid: Vector[Vector[Int]] =
      grid.map(row => row.toVector).toVector

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

    /*
     * Obtiene los vecinos válidos de una posición.
     *
     * Se utilizan map() y filter() para generar las cuatro
     * posiciones y conservar únicamente aquellas que:
     * - están dentro de la matriz;
     * - tienen el color original.
     */
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

    /*
     * Obtiene todos los vecinos de la frontera actual.
     *
     * flatMap() combina los vecinos obtenidos de cada posición.
     */
    def frontierNeighbors(
        frontier: List[Position],
        visited: Set[Position]
    ): List[Position] = {

      frontier
        .flatMap(validNeighbors)
        .filter(position => !visited.contains(position))
        .distinct
    }

    /*
     * Recorre la región conectada utilizando recursión de cola.
     *
     * frontier contiene las posiciones pendientes de procesar.
     * visited contiene todas las posiciones ya procesadas.
     */
    @tailrec
    def fill(
        frontier: List[Position],
        visited: Set[Position]
    ): Set[Position] = {

      frontier match {

        case Nil =>
          visited

        case current :: remaining =>

          if (visited.contains(current)) {
            fill(remaining, visited)
          } else {

            val newVisited =
              visited + current

            val newPositions =
              frontierNeighbors(
                remaining,
                newVisited
              )

            fill(
              remaining ++ newPositions,
              newVisited
            )
          }
      }
    }

    /*
     * foldLeft() se utiliza para construir el estado inicial
     * de manera funcional.
     */
    val initialState =
      List((row, col))
        .foldLeft(
          (List.empty[Position], Set.empty[Position])
        ) {
          case ((frontier, visited), position) =>
            (frontier :+ position, visited)
        }

    val filledPositions =
      fill(
        initialState._1,
        initialState._2
      )

    /*
     * Reconstrucción funcional de la matriz.
     *
     * zipWithIndex permite conservar correctamente las coordenadas
     * incluso cuando existen filas idénticas.
     */
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
          .toArray
      }
      .toArray
  }

  // ============================================================
  // TESTS
  // ============================================================

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

  // TC02: toda la matriz pertenece a la misma región
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

/*
 * ============================================================
 * CAMBIOS Y DECISIONES
 * ============================================================
 *
 * 1. La matriz Array[Array[Int]] recibida se convierte a
 *    Vector[Vector[Int]] para trabajar funcionalmente con
 *    colecciones inmutables.
 *
 * 2. Se utilizan exclusivamente val para mantener el estado.
 *
 * 3. El recorrido de Flood Fill utiliza @tailrec en lugar de
 *    while, evitando estructuras iterativas imperativas.
 *
 * 4. map() y filter() se utilizan para generar y validar vecinos.
 *
 * 5. flatMap() combina los vecinos de las posiciones de la frontera.
 *
 * 6. foldLeft() se utiliza para construir el estado inicial de
 *    forma funcional.
 *
 * 7. Set[Position] mantiene las posiciones visitadas sin utilizar
 *    una colección mutable.
 *
 * 8. No se modifica la matriz original durante el procesamiento.
 *
 * 9. La reconstrucción utiliza zipWithIndex para preservar
 *    correctamente las coordenadas de cada celda.
 *
 * 10. La conectividad considera únicamente cuatro direcciones:
 *     arriba, abajo, izquierda y derecha.
 *
 * 11. Si el color original coincide con newColor, se retorna
 *     inmediatamente la matriz sin realizar modificaciones.
 *
 * 12. Se incluyen exactamente tres casos de prueba mediante assert.
 *
 * Complejidad:
 *
 * Tiempo: O(R * C) en términos del recorrido de las celdas.
 * Espacio: O(R * C) para visited y frontier.
 */