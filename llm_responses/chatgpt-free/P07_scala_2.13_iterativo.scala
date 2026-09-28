/*

* 2D Flood Fill — Scala 2.13
* Paradigma: estrictamente iterativo
*
* Características utilizadas:
* 1. var para mantener el estado mutable del algoritmo.
* 2. mutable.ListBuffer para implementar la cola del BFS.
* 3. mutable.Map para representar las cuatro direcciones.
* 4. while para controlar el recorrido sin utilizar métodos
* funcionales como map(), filter() o foldLeft().
*
* No se utilizan:
* * map()
* * filter()
* * foldLeft()
* * foreach()
* * recursion
    */

import scala.collection.mutable

object FloodFill {

def floodFill(
grid: Array[Array[Int]],
row: Int,
col: Int,
newColor: Int
): Array[Array[Int]] = {

val originalColor = grid(row)(col)

if (originalColor == newColor) {
  return grid
}

val rows = grid.length
val cols = grid(0).length

val queue = mutable.ListBuffer[(Int, Int)]()
queue += ((row, col))

grid(row)(col) = newColor

val directions = mutable.Map[Int, (Int, Int)](
  0 -> (-1, 0), // arriba
  1 -> (1, 0),  // abajo
  2 -> (0, -1), // izquierda
  3 -> (0, 1)   // derecha
)

var queueIndex = 0

while (queueIndex < queue.length) {

  val (currentRow, currentCol) = queue(queueIndex)
  queueIndex += 1

  var directionIndex = 0

  while (directionIndex < directions.size) {

    val (rowOffset, colOffset) = directions(directionIndex)

    val nextRow = currentRow + rowOffset
    val nextCol = currentCol + colOffset

    if (
      nextRow >= 0 &&
      nextRow < rows &&
      nextCol >= 0 &&
      nextCol < cols &&
      grid(nextRow)(nextCol) == originalColor
    ) {
      grid(nextRow)(nextCol) = newColor
      queue += ((nextRow, nextCol))
    }

    directionIndex += 1
  }
}

grid
}

def main(args: Array[String]): Unit = {

// TC01: fill clásico
val grid1 = Array(
  Array(1, 1, 1),
  Array(1, 1, 0),
  Array(1, 0, 1)
)

val result1 = floodFill(grid1, 1, 1, 2)

assert(
  result1.deep == Array(
    Array(2, 2, 2),
    Array(2, 2, 0),
    Array(2, 0, 1)
  ).deep
)

// TC02: fill completo
val grid2 = Array(
  Array(0, 0),
  Array(0, 0)
)

assert(
  floodFill(grid2, 0, 0, 3).deep == Array(
    Array(3, 3),
    Array(3, 3)
  ).deep
)

// TC03: mismo color, sin cambios
val grid3 = Array(
  Array(1, 1, 1),
  Array(1, 1, 0),
  Array(1, 0, 1)
)

assert(
  floodFill(grid3, 1, 1, 1).deep == Array(
    Array(1, 1, 1),
    Array(1, 1, 0),
    Array(1, 0, 1)
  ).deep
)

println("All tests passed")
}
}

/*

* Cambios y decisiones:
*
* 1. Scala 2.13:
* Se utiliza exclusivamente la biblioteca estándar disponible
* en Scala 2.13.
*
* 2. var:
* queueIndex y directionIndex son variables mutables necesarias
* para controlar los bucles iterativos.
*
* 3. mutable.ListBuffer:
* Se utiliza como estructura mutable para almacenar las celdas
* pendientes de procesamiento durante el BFS.
*
* 4. mutable.Map:
* Representa las cuatro direcciones de movimiento mediante
* pares (fila, columna).
*
* 5. while:
* Todos los recorridos se realizan mediante while, evitando
* abstracciones funcionales como map(), filter() y foldLeft().
*
* 6. Sin recursion:
* El BFS se implementa completamente de forma iterativa.
*
* 7. Mutación in-place:
* Cada celda se cambia a newColor cuando se agrega a la cola.
* Esto evita agregar varias veces la misma celda.
*
* 8. Conectividad:
* Solo se consideran las cuatro direcciones requeridas:
* arriba, abajo, izquierda y derecha.
*
* 9. Se conserva el comportamiento solicitado:
* Si originalColor == newColor, la matriz se retorna sin cambios.
*
* 10. Complejidad:
* ```
  Tiempo: O(filas * columnas).
* ```
  Espacio adicional: O(filas * columnas) en el peor caso.

*/
