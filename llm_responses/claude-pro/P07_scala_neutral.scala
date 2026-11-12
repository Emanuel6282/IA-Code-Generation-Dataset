import scala.collection.mutable

object FloodFill {

  /** Desplazamientos arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
  private val Directions: List[(Int, Int)] = List((-1, 0), (1, 0), (0, -1), (0, 1))

  /** Rellena con `newColor` la región conectada (4 direcciones) que contiene (row, col):
    * todas las celdas alcanzables desde ahí moviéndose arriba, abajo, izquierda o derecha
    * por celdas del mismo color original.
    *
    * La matriz se modifica en el lugar y se retorna la misma referencia. Si la celda ya
    * tiene `newColor`, se retorna sin cambios.
    *
    * Se recorre con una pila explícita (mutable.ArrayDeque) en lugar de recursión: un DFS
    * recursivo sobre una región grande desbordaría la pila de la JVM. Cada celda se pinta
    * al apilarla, así que no se visita dos veces. O(filas * columnas) en tiempo. Admite
    * matrices irregulares: los límites se comprueban por fila.
    *
    * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
    */
  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    def inBounds(r: Int, c: Int): Boolean =
      r >= 0 && r < grid.length && c >= 0 && c < grid(r).length

    if (!inBounds(row, col))
      throw new IndexOutOfBoundsException(s"posición ($row, $col) fuera de la matriz")

    val original = grid(row)(col)
    // Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación el
    // bucle no terminaría, porque las celdas pintadas seguirían teniendo el color original.
    if (original != newColor) {
      val stack = mutable.ArrayDeque((row, col))
      grid(row)(col) = newColor
      while (stack.nonEmpty) {
        val (r, c) = stack.removeLast()
        for ((dr, dc) <- Directions) {
          val nr = r + dr
          val nc = c + dc
          if (inBounds(nr, nc) && grid(nr)(nc) == original) {
            grid(nr)(nc) = newColor
            stack.append((nr, nc))
          }
        }
      }
    }
    grid
  }

  /** Copia profunda como listas, para comparar matrices por contenido en los asserts. */
  private def contents(grid: Array[Array[Int]]): List[List[Int]] = grid.map(_.toList).toList

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
    val grid1 = Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1))
    val result1 = floodFill(grid1, 1, 1, 2)
    assert(contents(result1) == List(List(2, 2, 2), List(2, 2, 0), List(2, 0, 1)), contents(result1))
    assert(result1 eq grid1, "debe modificar y retornar la misma matriz")

    // Caso 2: el color ya es el nuevo -> la matriz no cambia
    val grid2 = Array(Array(0, 0, 0), Array(0, 1, 1))
    assert(contents(floodFill(grid2, 1, 1, 1)) == List(List(0, 0, 0), List(0, 1, 1)), contents(grid2))

    // Caso 3: no se rellena en diagonal
    val grid3 = Array(Array(1, 0), Array(0, 1))
    assert(contents(floodFill(grid3, 0, 0, 3)) == List(List(3, 0), List(0, 1)), contents(grid3))

    // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
    for ((r, c) <- List((2, 0), (-1, 0), (0, 3)))
      assert(
        scala.util.Try(floodFill(Array(Array(1, 1, 1), Array(1, 1, 1)), r, c, 5))
          .failed.toOption.exists(_.isInstanceOf[IndexOutOfBoundsException]),
        s"($r, $c) debería lanzar IndexOutOfBoundsException")

    println("Todas las pruebas pasaron.")
  }
}