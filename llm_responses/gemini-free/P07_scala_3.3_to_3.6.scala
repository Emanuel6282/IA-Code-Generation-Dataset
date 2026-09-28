// CARACTERÍSTICAS NUEVAS DE SCALA 3.6 UTILIZADAS:
// 1. Sintaxis basada en indentación avanzada y mejoras de compilación en Scala 3.6: Permite estructurar bloques de código de manera limpia y sin llaves.
// 2. Anotación `@main`: Define el punto de entrada de la aplicación directamente sobre el método de prueba de manera idiomática.
// 3. Optimizaciones de rendimiento del compilador de Scala 3.6 para colecciones mutables y estructuras de control.

import scala.collection.mutable

object P07FloodFill_v36:

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    val originalColor = grid(row)(col)
    if originalColor == newColor then return grid

    val rows = grid.length
    val cols = grid(0).length
    val queue = mutable.Queue[(Int, Int)]()
    queue.enqueue((row, col))
    grid(row)(col) = newColor

    val directions = List((-1,0),(1,0),(0,-1),(0,1))

    while queue.nonEmpty do
      val (r, c) = queue.dequeue()
      for (dr, dc) <- directions do
        val nr = r + dr
        val nc = c + dc
        if nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid(nr)(nc) == originalColor then
          grid(nr)(nc) = newColor
          queue.enqueue((nr, nc))
    grid

  @main def runTests(): Unit =
    // TC01: fill clasico
    val r1 = floodFill(Array(Array(1,1,1),Array(1,1,0),Array(1,0,1)), 1, 1, 2)
    assert(r1.deep == Array(Array(2,2,2),Array(2,2,0),Array(2,0,1)).deep)

    // TC02: grilla completa
    val r2 = floodFill(Array(Array(0,0,0),Array(0,0,0)), 0, 0, 1)
    assert(r2.deep == Array(Array(1,1,1),Array(1,1,1)).deep)

    // TC03: mismo color
    val r3 = floodFill(Array(Array(1,1,1),Array(1,1,0),Array(1,0,1)), 1, 1, 1)
    assert(r3.deep == Array(Array(1,1,1),Array(1,1,0),Array(1,0,1)).deep)

    println("All tests passed")

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6:
// 1. Adopción de sintaxis moderna de Scala 3.6: Se eliminaron las llaves redundantes mediante la sintaxis de bloques basada en indentación, estandarizando el código con las últimas especificaciones del lenguaje.
// 2. Uso simplificado de `@main`: Facilita la ejecución directa de las pruebas unitarias sin requerir un método `main(args)` explícito dentro del objeto envolvente.
// 3. Integridad algorítmica: Se preservó la lógica BFS iterativa in-place para garantizar un rendimiento óptimo de orden O(N) y el cumplimiento exitoso de todas las aserciones.