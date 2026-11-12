
// P07: Flood Fill — Scala 3.6
// Migración del código base a Scala 3.6.
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite eliminar llaves en objetos, métodos y bloques.
//    Mejora la legibilidad del código.
//
// 2. Condicionales con then:
//    Hacen explícita la separación entre condición y cuerpo.
//
// 3. Bucles con do:
//    Permiten expresar while y for con sintaxis moderna.
//
// 4. Desestructuración de tuplas:
//    Facilita el manejo de coordenadas (fila, columna).
//
// 5. mutable.Queue:
//    Mantiene operaciones FIFO eficientes para BFS.
//
// 6. Scala 3.6:
//    Se utiliza la sintaxis estable de Scala 3.
//    No se introducen características experimentales
//    ni exclusivas de 3.6 que no aporten ventajas.
//
// 7. Biblioteca estándar:
//    No se necesitan dependencias externas.
//
// Nota: then, do y la sintaxis de indentación
// pertenecen a Scala 3 y no debutaron en Scala 3.6.

import scala.collection.mutable

object P07FloodFill_v36:

  /**
   * Aplica Flood Fill en cuatro direcciones.
   *
   * Modifica y devuelve la matriz original.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] =

    val originalColor = grid(row)(col)

    // Si ya tiene el nuevo color, no modificar.
    if originalColor == newColor then
      return grid

    val rows = grid.length
    val cols = grid(0).length

    // Cola FIFO para BFS.
    val queue = mutable.Queue[(Int, Int)]()

    // Insertar la posición inicial.
    queue.enqueue((row, col))

    // Marcarla antes de procesar sus vecinos.
    grid(row)(col) = newColor

    // Arriba, abajo, izquierda, derecha.
    val directions = List(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    // Recorrido BFS iterativo.
    while queue.nonEmpty do

      val (r, c) = queue.dequeue()

      // Examinar las cuatro direcciones.
      for (dr, dc) <- directions do

        val nr = r + dr
        val nc = c + dc

        // Verificar límites y color original.
        if nr >= 0 && nr < rows &&
           nc >= 0 && nc < cols &&
           grid(nr)(nc) == originalColor then

          // Cambiar el color antes de encolar.
          grid(nr)(nc) = newColor

          queue.enqueue((nr, nc))

    grid

  def main(args: Array[String]): Unit =

    // TC01: fill clasico
    val r1 = floodFill(
      Array(
        Array(1, 1, 1),
        Array(1, 1, 0),
        Array(1, 0, 1)
      ),
      1, 1, 2
    )

    assert(
      r1.deep == Array(
        Array(2, 2, 2),
        Array(2, 2, 0),
        Array(2, 0, 1)
      ).deep
    )

    // TC02: grilla completa
    val r2 = floodFill(
      Array(
        Array(0, 0, 0),
        Array(0, 0, 0)
      ),
      0, 0, 1
    )

    assert(
      r2.deep == Array(
        Array(1, 1, 1),
        Array(1, 1, 1)
      ).deep
    )

    // TC03: mismo color
    val r3 = floodFill(
      Array(
        Array(1, 1, 1),
        Array(1, 1, 0),
        Array(1, 0, 1)
      ),
      1, 1, 1
    )

    assert(
      r3.deep == Array(
        Array(1, 1, 1),
        Array(1, 1, 0),
        Array(1, 0, 1)
      ).deep
    )

    println("All tests passed")

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN A SCALA 3.6
 *
 * 1. Se renombró el objeto:
 *    P07FloodFill_v213 -> P07FloodFill_v36.
 *    El nombre identifica la versión de destino.
 *
 * 2. Se adoptó la sintaxis de indentación:
 *    Se eliminaron las llaves innecesarias de los bloques.
 *    Es una característica estable de Scala 3.
 *
 * 3. Se modernizaron los condicionales:
 *    if (condicion) { ... }
 *    pasó a:
 *    if condicion then ...
 *
 * 4. Se modernizó el bucle while:
 *    while (condicion) { ... }
 *    pasó a:
 *    while condicion do ...
 *
 * 5. Se modernizó el bucle for:
 *    for ((dr, dc) <- directions) { ... }
 *    pasó a:
 *    for (dr, dc) <- directions do ...
 *
 * 6. Se conservó mutable.Queue:
 *    Es apropiada para BFS porque mantiene
 *    el orden FIFO con operaciones eficientes.
 *
 * 7. Se conservó exactamente el nombre floodFill:
 *    No cambia la interfaz pública del algoritmo.
 *
 * 8. Se mantuvo la modificación in-place:
 *    La matriz original se modifica y se devuelve.
 *
 * 9. Se mantuvo el retorno anticipado:
 *    Si originalColor == newColor,
 *    no se realiza ninguna modificación.
 *
 * 10. Se conservó la conectividad de cuatro direcciones:
 *     No se consideran conexiones diagonales.
 *
 * 11. Se conservaron los tres casos de prueba:
 *     TC01: fill clásico.
 *     TC02: grilla completa.
 *     TC03: mismo color.
 *
 * 12. Se mantuvo BFS estrictamente iterativo:
 *     No se introdujo recursión.
 *
 * 13. Se preservó la complejidad:
 *     Tiempo: O(m * n).
 *     Espacio auxiliar: O(m * n).
 *
 * 14. Se utiliza únicamente la biblioteca estándar:
 *     No se agregaron dependencias de terceros.
 */
