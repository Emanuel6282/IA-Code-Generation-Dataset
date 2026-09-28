/*

* P07: Flood Fill — Scala 3.3
* Paradigma: estrictamente iterativo
*
* Características utilizadas:
*
* 1. Sintaxis de Scala 3:
* Se utiliza la sintaxis moderna de Scala 3.3 manteniendo una
* implementación clara y compatible con el paradigma iterativo.
*
* 2. mutable.ListBuffer:
* Se utiliza como cola para implementar el recorrido BFS.
*
* 3. mutable.Map:
* Se utiliza para representar las cuatro direcciones posibles.
*
* 4. while:
* Controla el recorrido del algoritmo sin utilizar abstracciones
* funcionales como map(), filter() o foldLeft().
*
* Nota:
* Scala 3.3 incorpora características como enum, union types,
* extension methods y given/using, pero ninguna mejora
* materialmente este algoritmo iterativo. No se utilizan
* artificialmente.
  */

import scala.collection.mutable

object P07FloodFill_v3 {

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

  val current = queue(queueIndex)
  queueIndex += 1

  val currentRow = current._1
  val currentCol = current._2

  var directionIndex = 0

  while (directionIndex < directions.size) {

    val direction = directions(directionIndex)

    val nextRow = currentRow + direction._1
    val nextCol = currentCol + direction._2

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
  Array(0, 0, 0),
  Array(0, 0, 0)
)

assert(
  floodFill(grid2, 0, 0, 1).deep == Array(
    Array(1, 1, 1),
    Array(1, 1, 1)
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
* 1. Se actualiza la implementación a Scala 3.3.
*
* 2. Se utiliza mutable.ListBuffer[(Int, Int)] como cola BFS,
* permitiendo agregar posiciones sin utilizar una estructura
* externa.
*
* 3. Se utiliza mutable.Map[Int, (Int, Int)] para representar
* explícitamente las cuatro direcciones de movimiento.
*
* 4. El recorrido utiliza exclusivamente while. No se utilizan
* map(), filter(), foldLeft(), foreach() ni otras operaciones
* funcionales.
*
* 5. queueIndex permite recorrer el ListBuffer sin eliminar
* elementos desde el principio de la estructura.
*
* 6. Una celda se colorea en el momento en que se agrega a la cola.
* Esto evita que una misma celda sea agregada varias veces.
*
* 7. Se mantiene la modificación in-place de la matriz, igual que
* en las versiones anteriores.
*
* 8. Se conserva la condición originalColor == newColor para
* retornar inmediatamente la matriz sin modificaciones.
*
* 9. Se conservan exactamente los tres escenarios de prueba:
* TC01: caso clásico.
* TC02: región completamente conectada.
* TC03: nuevo color igual al color original.
*
* 10. Complejidad:
* ```
  Tiempo: O(filas * columnas).
  ```
* ```
  Espacio adicional: O(filas * columnas) en el peor caso.
  ```

*/
