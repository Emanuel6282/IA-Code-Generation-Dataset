// Scala - Algoritmo Flood Fill (Relleno por inundación)
// Paradigma: Iterativo BFS (Breadth-First Search) con mutación in-place

import scala.collection.mutable

object FloodFillScala {

  /**
   * Implementa el algoritmo Flood Fill sobre una matriz 2D de enteros de forma iterativa (BFS).
   *
   * @param grid      Matriz 2D de enteros a modificar.
   * @param row       Fila de la posición inicial.
   * @param col       Columna de la posición inicial.
   * @param newColor  Nuevo color a propagar.
   * @return La matriz modificada.
   */
  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    if (grid == null || grid.isEmpty || grid(0).isEmpty) {
      return grid
    }

    val originalColor = grid(row)(col)
    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    // Cola mutable para el recorrido por anchura (BFS)
    val queue = mutable.Queue[(Int, Int)]()
    queue.enqueue((row, col))
    grid(row)(col) = newColor

    // Direcciones en 4 sentidos: arriba, abajo, izquierda, derecha
    val directions = Array((-1, 0), (1, 0), (0, -1), (0, 1))

    while (queue.nonEmpty) {
      val (r, c) = queue.dequeue()

      for ((dr, dc) <- directions) {
        val nr = r + dr
        val nc = c + dc

        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid(nr)(nc) == originalColor) {
          grid(nr)(nc) = newColor
          queue.enqueue((nr, nc))
        }
      }
    }

    grid
  }

  // ==========================================
  // CASOS DE PRUEBA (ASSERTS)
  // ==========================================
  def main(args: Array[String]): Unit = {
    // Habilitar aserciones ejecutando con: scalac y scala -Xassertions

    // CASO 1: Ejemplo clásico del enunciado
    val grid1 = Array(
      Array(1, 1, 1),
      Array(1, 1, 0),
      Array(1, 0, 1)
    )
    val expected1 = Array(
      Array(2, 2, 2),
      Array(2, 2, 0),
      Array(2, 0, 1)
    )
    val result1 = floodFill(grid1, 1, 1, 2)
    assert(result1.deep == expected1.deep, "Falló Caso 1")

    // CASO 2: El color inicial ya es el nuevo color
    val grid2 = Array(
      Array(0, 0, 0),
      Array(0, 1, 1)
    )
    val expected2 = Array(
      Array(0, 0, 0),
      Array(0, 1, 1)
    )
    val result2 = floodFill(grid2, 1, 1, 1)
    assert(result2.deep == expected2.deep, "Falló Caso 2")

    // CASO 3: Matriz de un solo elemento
    val grid3 = Array(Array(4))
    val expected3 = Array(Array(9))
    val result3 = floodFill(grid3, 0, 0, 9)
    assert(result3.deep == expected3.deep, "Falló Caso 3")

    println("¡Todos los casos de prueba en Scala pasaron correctamente!")
  }
}