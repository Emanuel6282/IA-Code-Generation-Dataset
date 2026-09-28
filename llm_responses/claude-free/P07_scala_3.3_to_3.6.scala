// Caracteristicas nuevas de Scala 3.6 utilizadas y por que:
// 1. Sintaxis significativa por indentacion (optional braces), el estilo
//    por defecto y recomendado en la linea 3.x (incluida 3.6) para reducir
//    ruido sintactico frente a las llaves tradicionales.
// 2. `then`/`do` en if y while, requeridos por la sintaxis sin llaves para
//    separar claramente la condicion del cuerpo.
// 3. `@main` en lugar de un metodo `main(args: Array[String])` dentro de un
//    `object`, forma idiomatica y estable en Scala 3 para declarar un punto
//    de entrada ejecutable de forma concisa.
// 4. Definiciones de nivel superior (top-level definitions), disponibles
//    desde Scala 3 y vigentes en 3.6, que evitan el boilerplate de envolver
//    la funcion en un `object` cuando no se necesita un namespace.
// 5. Se reemplazo `Array(...).deep == Array(...).deep` (API de Scala 2,
//    eliminada en la linea 3.x) por una comparacion explicita convirtiendo
//    a `List` anidadas, la forma portable y correcta en Scala 3.6 para
//    comparar el contenido de arreglos multidimensionales.
// 6. Se mantiene el paradigma iterativo original (BFS con mutable.Queue y
//    actualizacion in-place), ya que esta es una migracion de version, no
//    un cambio de paradigma.

import scala.collection.mutable

def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
  val originalColor = grid(row)(col)
  if originalColor == newColor then
    grid
  else
    val rows = grid.length
    val cols = grid(0).length
    val queue = mutable.Queue[(Int, Int)]()
    queue.enqueue((row, col))
    grid(row)(col) = newColor

    val directions = List((-1, 0), (1, 0), (0, -1), (0, 1))

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
  val r1 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 2)
  assert(r1.map(_.toList).toList == Array(Array(2, 2, 2), Array(2, 2, 0), Array(2, 0, 1)).map(_.toList).toList)

  // TC02: grilla completa
  val r2 = floodFill(Array(Array(0, 0, 0), Array(0, 0, 0)), 0, 0, 1)
  assert(r2.map(_.toList).toList == Array(Array(1, 1, 1), Array(1, 1, 1)).map(_.toList).toList)

  // TC03: mismo color
  val r3 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 1)
  assert(r3.map(_.toList).toList == Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)).map(_.toList).toList)

  println("All tests passed")

// Cambios realizados y justificacion:
// 1. Se agrego el comentario inicial documentando las caracteristicas de
//    Scala 3.6 consideradas y su aporte a la legibilidad del codigo.
// 2. Se elimino el `object P07FloodFill_v213` envolvente y se paso a
//    definiciones de nivel superior, evitando el boilerplate de un objeto
//    contenedor innecesario.
// 3. Se removieron las llaves `{}` en favor de la sintaxis basada en
//    indentacion, el estilo idiomatico por defecto en la linea 3.x.
// 4. Se reemplazo `def main(args: Array[String]): Unit = { ... }` por
//    `@main def runTests(): Unit = ...`, la forma idiomatica en Scala 3 de
//    declarar un punto de entrada, sin necesidad de gestionar `args`.
// 5. Se reemplazo la comparacion `.deep == .deep` (eliminada en la linea 3.x)
//    por `.map(_.toList).toList ==`, la forma portable y correcta de
//    comparar arreglos anidados por contenido en Scala 3.6.
// 6. La logica del algoritmo (BFS con mutable.Queue, actualizacion in-place,
//    misma condicion de color y mismo recorrido en 4 direcciones) se
//    mantuvo exactamente igual, preservando comportamiento y resultados
//    identicos a la version 3.3.
// 7. Se conservaron los 3 casos de prueba originales (TC01, TC02, TC03) con
//    `assert`, sin alterar los valores esperados.
// 8. No se introdujeron colecciones inmutables, `foldLeft`, `@tailrec` ni
//    otras caracteristicas funcionales, ya que el codigo base es
//    explicitamente iterativo y la migracion debe preservar ese paradigma,
//    no cambiarlo.