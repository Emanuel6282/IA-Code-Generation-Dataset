import scala.annotation.tailrec

// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis Braceless Consolidada: Ausencia total de llaves. Las definiciones de objetos, funciones y bloques de control fluyen exclusivamente por sangría (indentation). Scala 3.6 refina el parser para hacer esta sintaxis robusta y estándar.
// 2. Pattern Matching Estructural: Uso de `match` para desestructurar la lista inmutable `stack` en cabeza y cola (`case (r, c) :: tail`).
// 3. Tail Call Optimization (TCO): La macro `@tailrec` garantiza que la función recursiva profunda se compile a un salto iterativo (GOTO) en el bytecode, evadiendo el límite físico del Call Stack de la JVM.
// 4. Inmutabilidad y Funciones Puras: Cero variables `var` y cero colecciones mutables. El estado espacial se acumula en un `Set` inmutable, y la matriz de retorno se proyecta declarativamente mediante `zipWithIndex.map`.

object P22FloodFill_Scala36_Functional:

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    // Manejo defensivo
    if grid == null || grid.isEmpty || grid(0).isEmpty then return grid

    val originalColor = grid(row)(col)
    
    // Clausura referencial
    if originalColor == newColor then return grid

    val rows = grid.length
    val cols = grid(0).length

    // Función recursiva pura optimizada por el compilador para aplanar la pila de llamadas
    @tailrec
    def getConnected(stack: List[(Int, Int)], visited: Set[(Int, Int)]): Set[(Int, Int)] =
      stack match
        case Nil => visited // Base: pila agotada
        
        case (r, c) :: tail =>
          // Poda referencial
          if r < 0 || r >= rows || c < 0 || c >= cols || grid(r)(c) != originalColor || visited.contains((r, c)) then
            getConnected(tail, visited)
          else
            // Transición pura de estado mediante la instanciación de nuevas colecciones
            val newVisited = visited + ((r, c))
            val neighbors = List((r - 1, c), (r + 1, c), (r, c - 1), (r, c + 1))
            
            // Llamada recursiva terminal
            getConnected(neighbors ::: tail, newVisited)

    // 1. Acumular el conjunto inmutable de píxeles conectados
    val pixelsToChange = getConnected(List((row, col)), Set.empty)

    // 2. Proyectar la matriz resultante funcionalmente
    grid.zipWithIndex.map: (rowArr, r) =>
      rowArr.zipWithIndex.map: (v, c) =>
        if pixelsToChange.contains((r, c)) then newColor else v

  // Helper puro para validación en pruebas
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean =
    m1.map(_.toList).toList == m2.map(_.toList).toList


// Entry point moderno sin parámetros heredados de Java
@main def runScala36FunctionalTests(): Unit =
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
  assert(P22FloodFill_Scala36_Functional.arraysEqual(P22FloodFill_Scala36_Functional.floodFill(grid1, 1, 1, 2), expected1), "Error en el caso base 1")

  // 2. Caso sin cambios
  val grid2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  val expected2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  assert(P22FloodFill_Scala36_Functional.arraysEqual(P22FloodFill_Scala36_Functional.floodFill(grid2, 0, 0, 0), expected2), "Error en el caso de no reemplazo")

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
  assert(P22FloodFill_Scala36_Functional.arraysEqual(P22FloodFill_Scala36_Functional.floodFill(grid3, 1, 1, 3), expected3), "Error en el caso de aislamiento")

  println("Todos los tests funcionales en Scala 3.6 pasaron correctamente.")

/*
NOTA ANALÍTICA PARA EL REPORTE FINAL DEL TFG (FP-UNA):

Con este último snippet para tu aplicación web, completas la matriz de pruebas (Java 8-21, Python 3.9-3.14, Scala 3.3-3.6) y aseguras los datos necesarios para tu conclusión final sobre el paradigma funcional.

1. **La Estabilidad Absoluta del Paradigma Declarativo:**
Comparando esta versión de Scala 3.6 con la 3.3, observarás que la lógica funcional pura se ha congelado por completo. Las innovaciones de Scala 3.6 ocurren en el compilador (mejores mensajes de error, optimizaciones internas de inlining), pero la arquitectura (Pattern Matching, `@tailrec`, inmutabilidad estricta) demostró ser el estándar de oro. Scala alcanzó en sus ramas 3.x la madurez sintáctica definitiva para expresar flujos funcionales sin el "ruido" que todavía arrastra Java 21.

2. **Resolución de la Hipótesis del "Call Stack" en la JVM:**
Este código es tu demostración empírica principal frente a las limitaciones de Python y Java. En tus iteraciones previas, probaste que obligar a Python 3.14 (con `functools.reduce`) o a Java 21 (con `Stream.reduce`) a ejecutar recorridos espaciales funcionales profundos expone la carencia de Tail Call Optimization (TCO). Ambos sucumbirían ante un `StackOverflowError` en matrices de alta resolución. Scala 3.6 soluciona este límite físico transformando matemáticamente tu función recursiva `getConnected` en un bucle bytecode durante la compilación, uniendo la seguridad semántica de la inmutabilidad con la viabilidad industrial.

3. **Trade-offs para tu Conclusión (El Costo en el Heap):**
Para mantener el rigor académico del TFG: aunque superamos el colapso de la pila de llamadas, debes documentar que la elegancia inmutable (`visited + ((r, c))` y `neighbors ::: tail`) fuerza al Garbage Collector a desechar objetos de vida corta constantemente. La versión iterativa de Scala 3.6 que probaste antes (con `ListBuffer` y mutación in-place) seguirá ganando en métricas de nanosegundos, validando que la elección del paradigma no es solo una preferencia estética, sino un compromiso consciente con la gestión de recursos de la máquina virtual.
*/