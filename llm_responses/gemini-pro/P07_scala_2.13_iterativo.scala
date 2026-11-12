import scala.collection.mutable.ListBuffer

// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Paradigma Imperativo Puro: Se destierra por completo la recursión y las comprensiones (for-loops en Scala son azúcar sintáctico para `foreach`/`map`). En su lugar, se utilizan variables mutables (`var`) y estructuras de control primitivas (`while`) para garantizar que no existan lambdas ocultas ni cierres (closures) bajo el capó.
// 2. Gestión Manual de Memoria en el Heap: Se emplea `scala.collection.mutable.ListBuffer` para emular una pila (Stack) LIFO, trasladando el control del recorrido de la matriz fuera del Call Stack de la JVM.
// 3. Mutación In-Place: Modificación destructiva del arreglo multidimensional `grid(r)(c) = newColor`. A diferencia del enfoque inmutable estándar de Scala, mutar arreglos primitivos directamente es la forma más rápida de ejecutar algoritmos espaciales en la JVM.

object P17FloodFill_Scala213_Iterative {

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    // Manejo defensivo
    if (grid == null || grid.isEmpty || grid(0).isEmpty) {
      return grid
    }

    val originalColor = grid(row)(col)

    // Cláusula de guarda: si no hay cambio de color, abortar para evitar bucles infinitos
    if (originalColor == newColor) {
      return grid
    }

    val rows = grid.length
    val cols = grid(0).length

    // Uso explícito de colecciones mutables para DFS iterativo
    val stack = ListBuffer[(Int, Int)]()
    stack.append((row, col))

    // Arreglo nativo de direcciones
    val directions = Array((-1, 0), (1, 0), (0, -1), (0, 1))

    // Bucle iterativo primario
    while (stack.nonEmpty) {
      // Extracción LIFO destructiva simulando un pop()
      val current = stack.remove(stack.length - 1)
      val r = current._1
      val c = current._2

      // Evaluación y mutación de estado
      if (grid(r)(c) == originalColor) {
        grid(r)(c) = newColor

        // Bucle while anidado puro (evitando for/foreach que generarían lambdas en Scala)
        var i = 0
        while (i < directions.length) {
          val nr = r + directions(i)._1
          val nc = c + directions(i)._2

          // Validación de límites antes de apilar
          if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
            stack.append((nr, nc))
          }
          i += 1
        }
      }
    }

    grid
  }

  // Helper iterativo puro para comparar matrices sin usar map(), filter() o fold()
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean = {
    if (m1.length != m2.length) return false
    var i = 0
    while (i < m1.length) {
      if (m1(i).length != m2(i).length) return false
      var j = 0
      while (j < m1(i).length) {
        if (m1(i)(j) != m2(i)(j)) return false
        j += 1
      }
      i += 1
    }
    true
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo
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
    assert(arraysEqual(floodFill(grid1, 1, 1, 2), expected1), "Error en el caso base 1")

    // 2. Caso sin cambios
    val grid2 = Array(
      Array(0, 0, 0),
      Array(0, 0, 0)
    )
    val expected2 = Array(
      Array(0, 0, 0),
      Array(0, 0, 0)
    )
    assert(arraysEqual(floodFill(grid2, 0, 0, 0), expected2), "Error en el caso de no reemplazo")

    // 3. Caso de aislamiento
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
    assert(arraysEqual(floodFill(grid3, 1, 1, 3), expected3), "Error en el caso de aislamiento")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}