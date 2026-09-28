// Scala 3.3 - Paradigma Iterativo Estricto
//
// Características de Scala 3.3 utilizadas:
// 1. Sintaxis limpia con indentación opcional nativa de Scala 3 para mejorar la legibilidad del código.
// 2. Paradigma iterativo estricto: Empleo exclusivo de bucles `while`, variables mutables `var` y `mutable.ListBuffer`, evitando por completo funciones como `map`, `filter`, `foldLeft` y la recursión.

import scala.collection.mutable.ListBuffer

object FloodFillScala33:

  /**
   * Implementa el algoritmo Flood Fill sobre una matriz 2D de enteros de forma iterativa (BFS)
   * utilizando construcciones imperativas en Scala 3.3.
   *
   * @param grid      Matriz 2D de enteros a modificar.
   * @param row       Fila de la posición inicial.
   * @param col       Columna de la posición inicial.
   * @param newColor  Nuevo color a propagar.
   * @return La matriz modificada.
   */
  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    if grid == null || grid.length == 0 || grid(0).length == 0 then
      return grid

    val originalColor = grid(row)(col)
    if originalColor == newColor then
      return grid

    val rows = grid.length
    val cols = grid(0).length

    // Cola mutable iterativa usando mutable.ListBuffer con un puntero de cabeza
    val queue = new ListBuffer[(Int, Int)]()
    queue.append((row, col))
    grid(row)(col) = newColor

    var head = 0
    val directions = Array((-1, 0), (1, 0), (0, -1), (0, 1))

    while head < queue.length do
      val current = queue(head)
      head += 1
      val r = current._1
      val c = current._2

      var i = 0
      while i < directions.length do
        val dir = directions(i)
        val nr = r + dir._1
        val nc = c + dir._2

        if nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid(nr)(nc) == originalColor then
          grid(nr)(nc) = newColor
          queue.append((nr, nc))
        
        i += 1

    grid

  // ==========================================
  // CASOS DE PRUEBA (ASSERTS)
  // ==========================================
  @main def runTests(): Unit =
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
    val grid3 = Array(Array(3))
    val expected3 = Array(Array(5))
    val result3 = floodFill(grid3, 0, 0, 5)
    assert(result3.deep == expected3.deep, "Falló Caso 3")

    println("¡Todos los casos de prueba iterativos en Scala 3.3 pasaron correctamente!")