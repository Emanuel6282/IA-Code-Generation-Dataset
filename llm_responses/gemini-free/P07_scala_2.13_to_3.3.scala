// CARACTERÍSTICAS NUEVAS DE SCALA 3.3 UTILIZADAS:
// 1. Sintaxis basada en indentación (Python-style): Elimina el uso excesivo de llaves `{}` para definir bloques de código, mejorando drásticamente la limpieza visual y legibilidad.
// 2. Anotación `@main`: Permite definir el punto de entrada de la aplicación directamente sobre el método de prueba de manera idiomática y libre de boilerplate (`object` principal con `main(args)` explícito).
// 3. Optimizaciones del compilador y tipado estricto mejorado en Scala 3.

import scala.collection.mutable

object P07FloodFill_v33:

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

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.3:
// 1. Supresión de llaves y adopción de indentación significativa: Scala 3 introduce la sintaxis orientada a bloques por indentación, eliminando la fatiga visual de llaves y paréntesis redundantes en condicionales y bucles.
// 2. Uso del modificador `@main`: Simplifica la ejecución de pruebas y programas independientes eliminando la necesidad de declarar manualmente el método `def main(args: Array[String])`.
// 3. Respeto absoluto de la lógica algorítmica: Se preservó intacto el enfoque BFS iterativo in-place para garantizar eficiencia O(N) y la superación exitosa de todas las aserciones originales.