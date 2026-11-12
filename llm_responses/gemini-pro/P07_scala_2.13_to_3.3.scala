import scala.collection.mutable

// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis Braceless (Indentation-based): Se eliminan las llaves `{}`. Las estructuras de control adoptan un estilo tabular usando `if ... then`, `while ... do` y `for ... do`. Esto elimina el ruido visual heredado de Java y acerca la ergonomía iterativa a lenguajes como Python.
// 2. Punto de entrada @main: Se reemplaza el verboso `def main(args: Array[String]): Unit` alojado dentro de un objeto Singleton por una función de nivel superior `@main`, logrando un entry point declarativo y minimalista.
// 3. Desestructuración nativa en bucles for: El bucle `for (dr, dc) <- directions do` desempaqueta limpiamente las tuplas en la iteración, sin requerir variables intermedias y manteniendo el alcance iterativo puro.

object P07FloodFill_v33:

  /**
   * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
   * Modifica y retorna el grid original.
   */
  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    val originalColor = grid(row)(col)
    
    // Cláusula de guarda con sintaxis Scala 3
    if originalColor == newColor then return grid

    val rows = grid.length
    val cols = grid(0).length
    val queue = mutable.Queue[(Int, Int)]()
    
    queue.enqueue((row, col))
    grid(row)(col) = newColor

    val directions = List((-1, 0), (1, 0), (0, -1), (0, 1))

    // Bucle imperativo con sintaxis 'while ... do'
    while queue.nonEmpty do
      val (r, c) = queue.dequeue()
      
      // Bucle iterativo interno con desestructuración directa
      for (dr, dc) <- directions do
        val nr = r + dr
        val nc = c + dc
        
        if nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid(nr)(nc) == originalColor then
          grid(nr)(nc) = newColor
          queue.enqueue((nr, nc))
          
    grid

  // Helper puro para validación en pruebas (evitando la obsolescencia de `.deep` en Scala 3)
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean =
    m1.map(_.toList).toList == m2.map(_.toList).toList

// Entry point declarativo moderno
@main def runFloodFillScala33Refactor(): Unit =
  import P07FloodFill_v33.*
  
  // TC01: fill clasico
  val r1 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 2)
  assert(arraysEqual(r1, Array(Array(2, 2, 2), Array(2, 2, 0), Array(2, 0, 1))), "TC01 fallo")

  // TC02: grilla completa
  val r2 = floodFill(Array(Array(0, 0, 0), Array(0, 0, 0)), 0, 0, 1)
  assert(arraysEqual(r2, Array(Array(1, 1, 1), Array(1, 1, 1))), "TC02 fallo")

  // TC03: mismo color, sin cambio
  val r3 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 1)
  assert(arraysEqual(r3, Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1))), "TC03 fallo")

  println("All tests passed (Scala 3.3 BFS Iterativo)")

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS PARA TU TESIS (FP-UNA):

Al cargar esta migración final de código BFS iterativo en tu herramienta web, aportas un contraste excelente para tu Trabajo Final de Grado:

1. **La Muerte del Boilerplate Visual**:
En la versión base de Scala 2.13, las llaves `{}` rodeaban el bloque `while`, el `for` y el `if`, arrastrando el "estilo C" a un lenguaje moderno. Scala 3.3 con su sintaxis *braceless* (`while ... do`, `if ... then`) comprueba la tendencia de la industria (que también mediste en Python): la sintaxis de los lenguajes dinámicos ha ganado la batalla de la ergonomía. Ahora Scala se lee estructuralmente como Python, pero mantiene su poderoso tipado estático bajo el capó.

2. **La Evolución de las Pruebas Unitarias (`.deep` obsoleto)**:
En Scala 2.13, usar `.deep == .deep` era un "hack" común para comparar matrices multidimensionales primitivas. Scala 3 depuró su sistema de tipos y colecciones, haciendo que este enfoque sea propenso a advertencias de compilación y comportamientos inestables. Reemplazarlo por una conversión explícita (`.map(_.toList).toList`) documenta cómo Scala 3 prefiere la predictibilidad inmutable por sobre la conveniencia heredada de Java.

3. **Arquitectura Intacta ante Limitaciones de Hardware**:
Aunque has migrado a Scala 3.3, notarás que mantuvimos estrictamente el uso de `mutable.Queue` y la asignación destructiva `grid(nr)(nc) = newColor`. Esta es una conclusión vital para tu tesis: sin importar cuán moderna o funcional se vuelva la sintaxis del lenguaje, cuando enfrentas las limitaciones físicas del Call Stack de la JVM en grafos bidimensionales masivos, el paradigma imperativo (BFS explícito usando memoria en el Heap) sigue siendo la única solución O(1) en espacio auxiliar sin penalizar el Garbage Collector.
*/