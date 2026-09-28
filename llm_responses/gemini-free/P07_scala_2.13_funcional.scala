// Scala 2.13 - Paradigma Funcional Estricto
//
// Características funcionales utilizadas:
// 1. Inmutabilidad estricta: Empleo exclusivo de colecciones inmutables (`Vector`, `Set`, `List`) y `val`, evitando por completo variables `var` o estructuras `mutable.*`.
// 2. Funciones de orden superior y métodos declarativos: Uso intensivo de combinadores funcionales como `map`, `filter`, `flatMap` y `foldLeft`.
// 3. Recursión de cola optimizada con `@tailrec`: Garantiza la seguridad frente a desbordamientos de pila mediante el procesamiento declarativo por lotes (fronteras).

import scala.annotation.tailrec

object FloodFillScala213Functional {

  /**
   * Implementa el algoritmo Flood Fill de forma estrictamente funcional en Scala 2.13
   * utilizando inmutabilidad, combinadores de colecciones y recursión de cola.
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

    // Conversión inicial de la matriz mutable a una estructura inmutable (Vector de Vectores)
    val immutableGrid = grid.map(_.toVector).toVector

    @tailrec
    def explore(currentGrid: Vector[Vector[Int]], frontier: Set[(Int, Int)], visited: Set[(Int, Int)]): Vector[Vector[Int]] = {
      if (frontier.isEmpty) {
        currentGrid
      } else {
        // Generación funcional de vecinos adyacentes usando flatMap y map
        val directions = List((-1, 0), (1, 0), (0, -1), (0, 1))
        
        val validNeighbors = frontier.toList.flatMap { case (r, c) =>
          directions.map { case (dr, dc) => (r + dr, c + dc) }
        }.filter { case (r, c) =>
          r >= 0 && r < rows && c >= 0 && c < cols && currentGrid(r)(c) == originalColor
        }.filterNot(visited.contains).toSet

        // Actualización inmutable de la matriz usando foldLeft
        val updatedGrid = frontier.foldLeft(currentGrid) { case (acc, (r, c)) =>
          acc.updated(r, acc(r).updated(c, newColor))
        }

        val newVisited = visited ++ frontier
        explore(updatedGrid, validNeighbors, newVisited)
      }
    }

    val finalGrid = explore(immutableGrid, Set((row, col)), Set.empty)

    // Conversión de retorno al formato Array[Array[Int]] requerido
    finalGrid.map(_.toArray).toArray
  }

  // ==========================================
  // CASOS DE PRUEBA (ASSERTS)
  // ==========================================
  def main(args: Array[String]): Unit = {
    // Habilitar aserciones ejecutando con: scala -Xassertions FloodFillScala213Functional

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
    val grid3 = Array(Array(7))
    val expected3 = Array(Array(3))
    val result3 = floodFill(grid3, 0, 0, 3)
    assert(result3.deep == expected3.deep, "Falló Caso 3")

    println("¡Todos los casos de prueba funcionales en Scala 2.13 pasaron correctamente!")
  }
}