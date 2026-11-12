
// P07: Flood Fill — Scala 3.3
// Migración desde Scala 2.13
// Estilo: BFS iterativo con mutable.Queue y modificación in-place.
//
// CARACTERÍSTICAS DE SCALA 3 UTILIZADAS:
//
// 1. Sintaxis de indentación significativa:
//    Permite definir objetos, funciones y bloques sin llaves.
//    Mejora la legibilidad y reduce el ruido sintáctico.
//
// 2. Palabras clave then y do:
//    Hacen más explícitas las condiciones y los bucles.
//
// 3. Sintaxis moderna de for:
//    Permite recorrer las direcciones sin paréntesis ni llaves.
//
// 4. Desestructuración de tuplas:
//    Se mantiene val (r, c) para extraer coordenadas.
//    Es compatible con Scala 3.3 y facilita la lectura.
//
// 5. mutable.Queue:
//    Se conserva porque es eficiente para BFS.
//    No es una característica nueva de Scala 3.3.
//
// 6. Compatibilidad con Scala 3.3:
//    Se mantiene la biblioteca estándar, sin dependencias.
//
// No se utilizan características experimentales.
// Se preservan la lógica y los resultados originales.

import scala.collection.mutable

object P07FloodFill_v33:

  /**
   * Aplica Flood Fill en cuatro direcciones.
   *
   * Modifica la matriz original y devuelve
   * la misma referencia.
   */
  def floodFill(
      grid: Array[Array[Int]],
      row: Int,
      col: Int,
      newColor: Int
  ): Array[Array[Int]] =

    val originalColor = grid(row)(col)

    // Si el color ya coincide, no modificar nada.
    if originalColor == newColor then
      return grid

    val rows = grid.length
    val cols = grid(0).length

    // Cola FIFO para BFS.
    val queue = mutable.Queue[(Int, Int)]()

    queue.enqueue((row, col))
    grid(row)(col) = newColor

    // Arriba, abajo, izquierda y derecha.
    val directions = List(
      (-1, 0),
      (1, 0),
      (0, -1),
      (0, 1)
    )

    // Recorrido BFS iterativo.
    while queue.nonEmpty do

      val (r, c) = queue.dequeue()

      // Examinar los cuatro vecinos.
      for (dr, dc) <- directions do

        val nr = r + dr
        val nc = c + dc

        // Verificar límites y color original.
        if nr >= 0 && nr < rows &&
           nc >= 0 && nc < cols &&
           grid(nr)(nc) == originalColor then

          // Marcar antes de encolar para evitar duplicados.
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
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN A SCALA 3.3
 *
 * 1. Nombre del objeto:
 *    P07FloodFill_v213 -> P07FloodFill_v33.
 *    Identifica la versión de destino.
 *
 * 2. Sintaxis de indentación:
 *    Se reemplazaron las llaves de los bloques
 *    por la sintaxis de indentación de Scala 3.
 *
 * 3. Condicionales:
 *    if (condicion) { ... }
 *    se transformó en:
 *    if condicion then ...
 *
 * 4. Bucles while:
 *    while (condicion) { ... }
 *    se transformó en:
 *    while condicion do ...
 *
 * 5. Bucle for:
 *    for ((dr, dc) <- directions) { ... }
 *    se transformó en:
 *    for (dr, dc) <- directions do ...
 *
 * 6. Se conservó mutable.Queue:
 *    Mantiene el comportamiento FIFO original
 *    con operaciones eficientes.
 *
 * 7. Se conservó la firma:
 *    floodFill(
 *      grid: Array[Array[Int]],
 *      row: Int,
 *      col: Int,
 *      newColor: Int
 *    ): Array[Array[Int]]
 *
 * 8. Se conservó la modificación in-place:
 *    No se crean copias de la matriz original.
 *
 * 9. Se mantuvo el retorno anticipado:
 *    Si originalColor == newColor,
 *    la matriz se devuelve sin modificaciones.
 *
 * 10. Se mantuvo la conectividad de cuatro direcciones:
 *     Arriba, abajo, izquierda y derecha.
 *
 * 11. Se conservaron exactamente los tres tests:
 *     TC01: fill clásico.
 *     TC02: grilla completa.
 *     TC03: mismo color.
 *
 * 12. No se introdujo recursión:
 *     BFS continúa siendo iterativo.
 *
 * 13. Complejidad preservada:
 *     Tiempo: O(m * n).
 *     Espacio auxiliar: O(m * n).
 *
 * 14. No se agregaron dependencias externas:
 *     Solo se utiliza la biblioteca estándar.
 */
