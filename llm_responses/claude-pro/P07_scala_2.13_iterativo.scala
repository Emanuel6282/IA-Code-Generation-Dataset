import scala.collection.mutable

// Scala 2.13 — paradigma iterativo (var, while, colecciones mutables; sin map/filter/
// foldLeft ni recursión).
//
// Características nuevas de Scala 2.13 que se usan:
// 1. mutable.ArrayDeque (nuevo en 2.13): búfer circular sobre un arreglo, con
//    append/removeLast en O(1) amortizado. Es la pila del recorrido; sustituye a
//    mutable.Stack, que en 2.13 se reimplementó sobre ArrayDeque.
// 2. removeLastOption (2.13): saca el último elemento devolviendo un Option, así el bucle
//    de la pila no necesita comprobar nonEmpty y luego extraer por separado.
// 3. mutable.ListBuffer con `+=` para acumular los casos de prueba y los fallos.
object FloodFill {

  /** Desplazamientos arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
  private val RowOffsets: Array[Int] = Array(-1, 1, 0, 0)
  private val ColOffsets: Array[Int] = Array(0, 0, -1, 1)

  /** Rellena con `newColor` la región conectada (4 direcciones) que contiene (row, col).
    *
    * La matriz se modifica en el lugar y se retorna la misma referencia. Si la celda ya
    * tiene `newColor`, se retorna sin cambios.
    *
    * Se recorre con una pila explícita y un bucle while, sin recursión: un DFS recursivo
    * sobre una región grande desbordaría la pila de la JVM. Cada celda se pinta al
    * apilarla, así que no se visita dos veces. O(filas * columnas) en tiempo. Admite
    * matrices irregulares: los límites se comprueban por fila.
    *
    * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
    */
  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    if (row < 0 || row >= grid.length || col < 0 || col >= grid(row).length)
      throw new IndexOutOfBoundsException(s"posición ($row, $col) fuera de la matriz")

    val original = grid(row)(col)
    // Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación el
    // bucle no terminaría, porque las celdas pintadas seguirían teniendo el color original.
    if (original == newColor) return grid

    val stack = mutable.ArrayDeque.empty[(Int, Int)]
    grid(row)(col) = newColor
    stack.append((row, col))

    var next = stack.removeLastOption()
    while (next.isDefined) {
      val (r, c) = next.get
      var i = 0
      while (i < RowOffsets.length) {
        val nr = r + RowOffsets(i)
        val nc = c + ColOffsets(i)
        if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid(nr).length && grid(nr)(nc) == original) {
          grid(nr)(nc) = newColor
          stack.append((nr, nc))
        }
        i += 1
      }
      next = stack.removeLastOption()
    }
    grid
  }

  /** Compara dos matrices celda a celda con bucles (Array compara por referencia). */
  private def sameContents(a: Array[Array[Int]], b: Array[Array[Int]]): Boolean = {
    var equal = a.length == b.length
    var r = 0
    while (equal && r < a.length) {
      equal = a(r).sameElements(b(r))
      r += 1
    }
    equal
  }

  private def show(grid: Array[Array[Int]]): String = {
    val rows = mutable.ListBuffer.empty[String]
    var r = 0
    while (r < grid.length) {
      rows += grid(r).mkString("[", ", ", "]")
      r += 1
    }
    rows.mkString("[", ", ", "]")
  }

  def main(args: Array[String]): Unit = {
    val testCases = mutable.ListBuffer.empty[(String, Array[Array[Int]], Int, Int, Int, Array[Array[Int]])]
    // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
    testCases += (("ejemplo del enunciado",
      Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 2,
      Array(Array(2, 2, 2), Array(2, 2, 0), Array(2, 0, 1))))
    // Caso 2: el color ya es el nuevo -> la matriz no cambia
    testCases += (("color ya es el nuevo",
      Array(Array(0, 0, 0), Array(0, 1, 1)), 1, 1, 1,
      Array(Array(0, 0, 0), Array(0, 1, 1))))
    // Caso 3: no se rellena en diagonal
    testCases += (("sin diagonales",
      Array(Array(1, 0), Array(0, 1)), 0, 0, 3,
      Array(Array(3, 0), Array(0, 1))))

    val failures = mutable.ListBuffer.empty[String]
    var i = 0
    while (i < testCases.length) {
      val (name, grid, row, col, newColor, expected) = testCases(i)
      val result = floodFill(grid, row, col, newColor)
      if (!sameContents(result, expected))
        failures += s"Caso '$name': esperado ${show(expected)}, obtenido ${show(result)}"
      if (!(result eq grid))
        failures += s"Caso '$name': debe modificar y retornar la misma matriz"
      i += 1
    }

    // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
    val badPositions = Array((2, 0), (-1, 0), (0, 3))
    var j = 0
    while (j < badPositions.length) {
      val (r, c) = badPositions(j)
      var threw = false
      try floodFill(Array(Array(1, 1, 1), Array(1, 1, 1)), r, c, 5)
      catch { case _: IndexOutOfBoundsException => threw = true }
      if (!threw) failures += s"($r, $c) debería lanzar IndexOutOfBoundsException"
      j += 1
    }

    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}