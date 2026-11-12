object P16FloodFill_Base {

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    // Validación de matriz nula o vacía
    if (grid == null || grid.isEmpty || grid(0).isEmpty) {
      return grid
    }

    val originalColor = grid(row)(col)

    // Cláusula de guarda: si el color original es igual al nuevo, no hay cambios
    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    // Función recursiva local para Búsqueda en Profundidad (DFS)
    def dfs(r: Int, c: Int): Unit = {
      // Condiciones de poda: fuera de límites o color diferente
      if (r < 0 || r >= rows || c < 0 || c >= cols || grid(r)(c) != originalColor) {
        return
      }

      // Mutación del color actual
      grid(r)(c) = newColor

      // Llamadas recursivas en las 4 direcciones
      dfs(r - 1, c) // Arriba
      dfs(r + 1, c) // Abajo
      dfs(r, c - 1) // Izquierda
      dfs(r, c + 1) // Derecha
    }

    // Iniciar el algoritmo
    dfs(row, col)
    grid
  }

  def main(args: Array[String]): Unit = {
    // Helper para comparar matrices 2D en Scala de forma segura
    def isEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean = {
      m1.map(_.toList).toList == m2.map(_.toList).toList
    }

    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: reemplazo de unos conectados a partir de (1,1)
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
    assert(isEqual(floodFill(grid1, 1, 1, 2), expected1), "Error en el caso base 1")

    // 2. Caso sin cambios: el color inicial ya es igual al nuevo color
    val grid2 = Array(
      Array(0, 0, 0),
      Array(0, 0, 0)
    )
    val expected2 = Array(
      Array(0, 0, 0),
      Array(0, 0, 0)
    )
    assert(isEqual(floodFill(grid2, 0, 0, 0), expected2), "Error en el caso de no reemplazo")

    // 3. Caso de aislamiento: solo cambia el centro, los otros 1s no están conectados
    val grid3 = Array(
      Array(1, 0, 1),
      Array(0, 1, 0),
      Array(1, 0, 1)
    )
    val expected3 = Array(
      Array(1, 0, 1),
      Array(0, 3, 0),
      Array(1, 0, 1)
    )
    assert(isEqual(floodFill(grid3, 1, 1, 3), expected3), "Error en el caso de aislamiento")

    println("Todos los casos de prueba pasaron correctamente.")
  }
}