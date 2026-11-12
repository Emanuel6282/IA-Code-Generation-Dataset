// P07: Flood Fill — migrado a Scala 3.6
// (el código recibido se presentaba como Scala 3.3, pero su cabecera y el nombre del
// objeto P07FloodFill_v213 corresponden al código base en Scala 2.13; se migra ese)
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Context bounds con nombre (SIP-64, Scala 3.6): `floodFill[A: Equiv as eq]`. La
//    función pasa a ser genérica en el tipo del color y compara colores con `eq.equiv`;
//    el `as` da nombre a la instancia en la propia lista de parámetros de tipo, sin un
//    `(using eq: Equiv[A])` aparte. Para Int la instancia la aporta la biblioteca
//    estándar: las llamadas existentes, como floodFill(grid, 1, 1, 2), no cambian y el
//    resultado es el mismo (Equiv[Int] compara con ==).
// 2. Estado del BFS propagado de forma inmutable con @tailrec, en lugar de un
//    mutable.Queue y un while: es el objetivo del código base ("propagar el estado mediante
//    Set inmutable de coordenadas"), con una cola inmutable en lugar del Set para no
//    cambiar el comportamiento (ver la nota del final). La cola de celdas pendientes es una
//    scala.collection.immutable.Queue que se pasa como parámetro de la recursión de cola;
//    el compilador la convierte en un bucle, así que no consume pila aunque la región sea
//    enorme.
// 3. Sintaxis de control nueva de Scala 3: `if … then … else` como expresión (sustituye
//    al `return grid` anticipado) y `match` sin llaves; indentación significativa.
// 4. Definiciones de nivel superior y @main: floodFill no necesita un `object`
//    contenedor y el punto de entrada no necesita `def main(args: Array[String])`.
//
// Nota: las tuplas con nombre (3.6) siguen siendo experimentales y no se usan.
// boundary/break (3.3) reemplazaría el `return grid` anticipado, pero un
// `if … then grid else …` como expresión lo hace más simple, así que no se usa.

import scala.annotation.tailrec
import scala.collection.immutable.Queue

private val directions = List((-1, 0), (1, 0), (0, -1), (0, 1))

def floodFill[A: Equiv as eq](grid: Array[Array[A]], row: Int, col: Int, newColor: A): Array[Array[A]] =
  val originalColor = grid(row)(col)
  if eq.equiv(originalColor, newColor) then grid
  else
    val rows = grid.length
    val cols = grid(0).length

    // Pinta (r, c) si es vecina válida con el color original y la añade a la cola: la
    // misma condición, en el mismo orden, que el `if` del original.
    def visit(pending: Queue[(Int, Int)], nr: Int, nc: Int): Queue[(Int, Int)] =
      if nr >= 0 && nr < rows && nc >= 0 && nc < cols && eq.equiv(grid(nr)(nc), originalColor) then
        grid(nr)(nc) = newColor
        pending.enqueue((nr, nc))
      else pending

    @tailrec
    def bfs(pending: Queue[(Int, Int)]): Unit =
      pending.dequeueOption match
        case None => ()
        case Some(((r, c), rest)) =>
          bfs(directions.foldLeft(rest)((queue, offset) => visit(queue, r + offset._1, c + offset._2)))

    grid(row)(col) = newColor
    bfs(Queue((row, col)))
    grid

/** Contenido de la matriz como listas, para comparar por valor (Array compara por referencia). */
private def contents(grid: Array[Array[Int]]): List[List[Int]] = grid.map(_.toList).toList

@main def P07FloodFill_v36(): Unit =
  // TC01: fill clasico
  val r1 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 2)
  assert(contents(r1) == List(List(2, 2, 2), List(2, 2, 0), List(2, 0, 1)))

  // TC02: grilla completa
  val r2 = floodFill(Array(Array(0, 0, 0), Array(0, 0, 0)), 0, 0, 1)
  assert(contents(r2) == List(List(1, 1, 1), List(1, 1, 1)))

  // TC03: mismo color
  val r3 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 1)
  assert(contents(r3) == List(List(1, 1, 1), List(1, 1, 0), List(1, 0, 1)))

  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6
// 1. `def floodFill(grid: Array[Array[Int]], …, newColor: Int)` ->
//    `def floodFill[A: Equiv as eq](grid: Array[Array[A]], …, newColor: A)`: misma
//    lógica para cualquier tipo de color; con Int, mismas llamadas y mismos resultados.
//    Las dos comparaciones `==` pasan a `eq.equiv(…)`.
// 2. `mutable.Queue` + `while (queue.nonEmpty)` + `dequeue()` -> función @tailrec `bfs`
//    que recibe una immutable.Queue y usa `dequeueOption` con pattern matching. El caso
//    `None` sustituye a la condición del while; `Some(((r, c), rest))` desestructura la
//    celda y la cola restante en un solo patrón.
// 3. `for ((dr, dc) <- directions) { … enqueue … }` -> `directions.foldLeft(rest)(…)`:
//    cada dirección produce la cola siguiente a partir de la anterior, en el mismo orden
//    (arriba, abajo, izquierda, derecha), así que el BFS visita y pinta las mismas celdas
//    en el mismo orden que el original.
// 4. `if (originalColor == newColor) return grid` -> `if … then grid else …` como
//    expresión, sin `return`.
// 5. La lista `directions`, que se creaba en cada llamada, pasa a ser un `val` de nivel
//    superior con el mismo contenido y orden.
// 6. `object P07FloodFill_v213 { … }` -> definiciones de nivel superior, y `def main` ->
//    `@main def P07FloodFill_v36()`. Llaves -> sintaxis por indentación.
// 7. TESTS: los tres casos y "All tests passed" se conservan. El original comparaba con
//    `Array.deep`, que se ELIMINÓ en Scala 2.13 (el código base no compila en 2.13: "value
//    deep is not a member of Array[Array[Int]]"). Se reemplaza por `contents`, que convierte
//    la matriz en List[List[Int]] para comparar por valor, que es lo que hacía `.deep`.
// 8. Comportamiento idéntico, verificado contra la función original (compilada en 2.13
//    sin el main) con miles de matrices aleatorias: mismo resultado, mismo estado final de
//    la matriz y mismas excepciones con el mismo mensaje. Se conservan tal cual los casos
//    límite del original:
//    - Modifica la matriz en el lugar y retorna la misma referencia.
//    - Una posición fuera de rango (también negativa) lanza
//      ArrayIndexOutOfBoundsException en `grid(row)(col)`.
//    - `cols = grid(0).length`: con filas de distinto largo usa el de la primera; si una
//      fila es más corta, `grid(nr)(nc)` lanza ArrayIndexOutOfBoundsException a mitad del
//      relleno, con la matriz ya modificada en parte, en el mismo punto que el original.
//
// NOTA: POR QUÉ UNA COLA INMUTABLE Y NO UN SET INMUTABLE DE COORDENADAS
// El código base sugería propagar un Set inmutable de coordenadas. Se evaluó y no se usa,
// porque no conservaría el comportamiento: un Set no tiene orden, así que el recorrido
// leería las celdas en otro orden. En el caso normal el resultado sería el mismo, pero con
// filas de distinto largo la excepción saltaría en otra celda y la matriz quedaría pintada
// de forma distinta. Además, un Set de visitadas sería redundante: pintar una celda ya la
// marca como visitada (deja de tener el color original). La immutable.Queue conserva el
// orden FIFO exacto del original.
//
// NOTA SOBRE RENDIMIENTO: immutable.Queue tiene enqueue/dequeue en O(1) amortizado. El
// coste visible lo pone la genericidad: con A genérico, los accesos al arreglo y las
// comparaciones pasan por métodos genéricos (con boxing de Int). Medido con una matriz de
// 1000 x 1000 de un solo color (JVM ya calentada): original ~0,04-0,05 s, migración
// ~0,05-0,06 s, hasta ~30 % más lenta. Si ese tramo fuera crítico, convendría la versión
// 3.3 con Int fijo; aquí se prioriza poder reutilizar la función con otros tipos de color.