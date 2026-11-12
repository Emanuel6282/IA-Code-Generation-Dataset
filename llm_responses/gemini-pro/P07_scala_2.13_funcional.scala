import scala.annotation.tailrec

// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Tail Call Optimization (@tailrec): A diferencia de Python o Java, Scala permite garantizar en tiempo de compilación que una función recursiva no consumirá la pila de llamadas (Call Stack). Al pasar la lista de nodos por visitar (`stack`) como un acumulador, el compilador traduce esta recursividad pura en un bucle iterativo a nivel de bytecode (JVM).
// 2. Pattern Matching Funcional: Se utiliza `match` sobre la lista inmutable `stack` (`case Nil`, `case (r, c) :: tail`) para extraer y evaluar los estados sin reasignar variables.
// 3. Inmutabilidad Estricta (`Set`, `List`, `val`): Todo el estado espacial avanza mediante la instanciación de nuevas colecciones inmutables (`visited + ((r, c))`). No hay mutación de la matriz original.
// 4. Proyección Pura (`zipWithIndex.map`): La reconstrucción de la matriz final se realiza evaluando coordenada por coordenada de forma declarativa, sin usar bloques `for` imperativos ni asignaciones destructivas `grid(r)(c) = ...`.

object P18FloodFill_Scala213_Functional {

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] = {
    // Clausuras de guarda puras
    if (grid == null || grid.isEmpty || grid(0).isEmpty) return grid

    val originalColor = grid(row)(col)
    if (originalColor == newColor) return grid

    val rows = grid.length
    val cols = grid(0).length

    // Función recursiva de cola para acumular las coordenadas que deben ser modificadas.
    // El estado del recorrido se propaga a través de argumentos inmutables.
    @tailrec
    def getConnected(stack: List[(Int, Int)], visited: Set[(Int, Int)]): Set[(Int, Int)] = {
      stack match {
        // Caso base: no quedan nodos por explorar
        case Nil => visited
        
        // Extracción del nodo actual y el resto de la pila (tail)
        case (r, c) :: tail =>
          // Condiciones de poda referencial
          if (r < 0 || r >= rows || c < 0 || c >= cols || grid(r)(c) != originalColor || visited.contains((r, c))) {
            getConnected(tail, visited)
          } else {
            // Generación de nuevos estados inmutables
            val newVisited = visited + ((r, c))
            val neighbors = List((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
            
            // Llamada recursiva (optimizada a salto iterativo por el compilador)
            getConnected(neighbors ::: tail, newVisited)
          }
      }
    }

    // 1. Acumulación del estado espacial
    val pixelsToChange = getConnected(List((row, col)), Set.empty)

    // 2. Generación inmutable de la matriz resultante
    grid.zipWithIndex.map { case (rowArr, r) =>
      rowArr.zipWithIndex.map { case (v, c) =>
        if (pixelsToChange.contains((r, c))) newColor else v
      }
    }
  }

  // Helper puro para validación en tests
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean = {
    m1.map(_.toList).toList == m2.map(_.toList).toList
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

    // 2. Caso sin cambios (color igual)
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

    println("Todos los tests funcionales en Scala 2.13 pasaron correctamente.")
  }
}