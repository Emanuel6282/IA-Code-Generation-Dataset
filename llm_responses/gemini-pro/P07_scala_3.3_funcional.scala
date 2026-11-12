import scala.annotation.tailrec

// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis Braceless (Indentation-based): Reducción drástica del "boilerplate" visual mediante el uso estricto de sangrías. Se elimina el ruido de las llaves `{}` en funciones, condicionales (`if ... then ... else`) y en el pattern matching (`match ... case`), logrando una limpieza declarativa que compite directamente con Python.
// 2. Tail Call Optimization (@tailrec): Scala mantiene su hegemonía en la JVM garantizando que la recursividad pura de nuestra búsqueda en profundidad (DFS) se aplane a un bucle seguro a nivel de bytecode, evadiendo el temido StackOverflowError.
// 3. Pattern Matching Moderno: Desestructuración inmutable limpia sobre la pila de coordenadas (`case (r, c) :: tail`).
// 4. Paradigma Funcional Estricto: Cero variables `var` o colecciones mutables. El estado (los píxeles modificados) fluye generacionalmente a través de `Set` inmutables, y la matriz de salida se proyecta usando cierres puros (`grid.zipWithIndex.map`).

object P20FloodFill_Scala33_Functional:

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    // Manejo defensivo declarativo
    if grid == null || grid.isEmpty || grid(0).isEmpty then return grid

    val originalColor = grid(row)(col)
    
    // Clausura de guarda
    if originalColor == newColor then return grid

    val rows = grid.length
    val cols = grid(0).length

    // Motor recursivo puro optimizado por el compilador para el recorrido de grafos.
    // Propaga el estado inmutable a través de argumentos en lugar de mutar el Heap local.
    @tailrec
    def getConnected(stack: List[(Int, Int)], visited: Set[(Int, Int)]): Set[(Int, Int)] =
      stack match
        case Nil => visited // Condición de corte: árbol agotado
        
        case (r, c) :: tail =>
          // Poda referencial declarativa
          if r < 0 || r >= rows || c < 0 || c >= cols || grid(r)(c) != originalColor || visited.contains((r, c)) then
            getConnected(tail, visited)
          else
            // Transición pura de estado
            val newVisited = visited + ((r, c))
            val neighbors = List((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
            
            // Llamada recursiva terminal
            getConnected(neighbors ::: tail, newVisited)

    // 1. Acumular funcionalmente la lista matemática de píxeles conectados
    val pixelsToChange = getConnected(List((row, col)), Set.empty)

    // 2. Proyectar la nueva matriz puramente, evitando iteradores imperativos
    grid.zipWithIndex.map: (rowArr, r) =>
      rowArr.zipWithIndex.map: (v, c) =>
        if pixelsToChange.contains((r, c)) then newColor else v

  // Helper puro para validación sin iteradores imperativos
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean =
    m1.map(_.toList).toList == m2.map(_.toList).toList

// Entry point declarativo, eliminando el boilerplate de los métodos main de Java/Scala 2
@main def runScala3FunctionalTests(): Unit =
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
  assert(P20FloodFill_Scala33_Functional.arraysEqual(P20FloodFill_Scala33_Functional.floodFill(grid1, 1, 1, 2), expected1), "Error en el caso base 1")

  // 2. Caso sin cambios
  val grid2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  val expected2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  assert(P20FloodFill_Scala33_Functional.arraysEqual(P20FloodFill_Scala33_Functional.floodFill(grid2, 0, 0, 0), expected2), "Error en el caso de no reemplazo")

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
  assert(P20FloodFill_Scala33_Functional.arraysEqual(P20FloodFill_Scala33_Functional.floodFill(grid3, 1, 1, 3), expected3), "Error en el caso de aislamiento")

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")

/*
NOTA ANALÍTICA PARA EL REPORTE FINAL DEL TFG (FP-UNA):

Al cargar esta última versión en la web de evaluación, cierras de manera contundente la experimentación funcional en Scala y completas el arco narrativo de la tesis respecto a la "muerte del boilerplate".

1. **La Cúspide de la Ergonomía y la Seguridad:**
Scala 3.3 demuestra empíricamente cómo fusionar los dos mundos en pugna de tu estudio. Por un lado, la sintaxis "braceless" (`if ... then`, `map:`, `@main`) logra un código casi tan conciso y tabular como las versiones iterativas de Python 3.14. Por otro, conserva la robustez arquitectónica estática de Java 21, sumando la garantía matemática de que el compilador manejará la memoria de la recursión (`@tailrec`).

2. **Resolución del Dilema del Call Stack:**
Como verificaste exhaustivamente a lo largo del estudio comparativo, obligar a Java (Streams/reduce) y Python (`functools.reduce`) a ejecutar recorridos espaciales funcionales profundos expone una limitación física en sus máquinas virtuales: la ausencia de "Tail Call Optimization" (TCO) nativa. Esta implementación funcional en Scala 3.3 es el único bloque de la experimentación declarativa que podría absorber una matriz de 10,000x10,000 píxeles en producción sin lanzar un `StackOverflowError`, aplanando silenciosamente tu clausura matemática en un salto de bytecodes de alto rendimiento en el JIT.

3. **Trade-offs del Paradigma Funcional (GC Overhead):**
El documento final debe destacar con rigor científico que, aunque el TCO protege el Call Stack, el precio de la inmutabilidad absoluta recae sobre el Heap. La proyección mediante cierres `grid.zipWithIndex.map` y la concatenación de listas inmutables (`neighbors ::: tail`) fuerza al Garbage Collector de la JVM a trabajar inmensamente más duro que las implementaciones iterativas de mutación in-place (`grid(r)(c) = newColor`), validando que la elección del paradigma no se trata solo de elegancia sintáctica, sino de entender el costo de los recursos computacionales.
*/