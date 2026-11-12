// P07: 2D Flood Fill — Scala 3.3
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite escribir bloques sin llaves obligatorias.
//
// 2. then y do:
//    Mejoran la legibilidad de if y while.
//
// 3. mutable.ListBuffer:
//    Almacena las posiciones pendientes del recorrido BFS.
//
// 4. mutable.Map:
//    Registra las posiciones visitadas.
//
// 5. var:
//    Se utiliza para índices y variables de control.
//
// 6. while:
//    Implementa BFS sin recursión.
//
// 7. Array[Array[Int]]:
//    Permite modificar la matriz original in-place.
//
// Biblioteca estándar únicamente.
// Sin map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object FloodFillScala33:

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

    // Arriba, abajo, izquierda, derecha.
    val directions = Array(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    // Lista mutable utilizada como cola FIFO.
    val queue = mutable.ListBuffer.empty[(Int, Int)]

    // Registro de posiciones visitadas.
    val visited = mutable.Map.empty[(Int, Int), Boolean]

    // Agregar posición inicial.
    queue += ((row, col))
    visited((row, col)) = true

    // Modificar el píxel inicial.
    grid(row)(col) = newColor

    // Índice de lectura.
    var head = 0

    // Recorrido BFS iterativo.
    while head < queue.length do

      val (r, c) = queue(head)
      head += 1

      // Examinar las cuatro direcciones.
      var i = 0

      while i < directions.length do

        val (dr, dc) = directions(i)

        val nr = r + dr
        val nc = c + dc

        // Verificar límites.
        if nr >= 0 && nr < rows &&
           nc >= 0 && nc < cols then

          val next = (nr, nc)

          // Verificar color y posición no visitada.
          if grid(nr)(nc) == originalColor &&
             !visited.contains(next) then

            // Marcar como visitada.
            visited(next) = true

            // Cambiar el color.
            grid(nr)(nc) = newColor

            // Agregar a la cola.
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