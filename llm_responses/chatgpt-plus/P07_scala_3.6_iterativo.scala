// P07: 2D Flood Fill — Scala 3.6
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite definir bloques sin llaves.
//
// 2. then y do:
//    Mejoran la legibilidad de if y while.
//
// 3. var:
//    Variables de control para los bucles.
//
// 4. mutable.ListBuffer:
//    Almacena las posiciones pendientes de BFS.
//
// 5. mutable.Map:
//    Registra las posiciones visitadas.
//
// 6. while:
//    Implementa todo el recorrido sin recursión.
//
// 7. Array[Array[Int]]:
//    Permite modificar directamente la matriz.
//
// Nota: ListBuffer, Map mutable y var no son novedades
// exclusivas de Scala 3.6; se usan porque el enunciado
// exige expresamente estas estructuras.
//
// Biblioteca estándar únicamente.

import scala.collection.mutable

object FloodFillScala36:

  /**
   * Aplica Flood Fill en cuatro direcciones.
   *
   * Modifica la matriz original y devuelve
   * la misma referencia.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] =

    val originalColor = grid(row)(col)

    // Si el color ya coincide, no modificar nada.
    if originalColor == newColor then
      return grid

    val rows = grid.length
    val cols = grid(0).length

    // Arriba, abajo, izquierda y derecha.
    val directions = Array(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    // Posiciones pendientes del recorrido BFS.
    val queue = mutable.ListBuffer.empty[(Int, Int)]

    // Posiciones visitadas.
    val visited = mutable.Map.empty[(Int, Int), Boolean]

    // Agregar y marcar la posición inicial.
    queue += ((row, col))
    visited((row, col)) = true
    grid(row)(col) = newColor

    // Índice de lectura.
    var head = 0

    // BFS estrictamente iterativo.
    while head < queue.length do

      val (r, c) = queue(head)
      head += 1

      var i = 0

      // Examinar las cuatro direcciones.
      while i < directions.length do

        val (dr, dc) = directions(i)

        val nr = r + dr
        val nc = c + dc

        // Verificar los límites de la matriz.
        if nr >= 0 && nr < rows &&
           nc >= 0 && nc < cols then

          val next = (nr, nc)

          // Solo visitar píxeles del color original.
          if grid(nr)(nc) == originalColor &&
             !visited.contains(next) then

            visited(next) = true
            grid(nr)(nc) = newColor

            // Agregar el vecino a la cola.
            queue += next

        i += 1

    grid

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