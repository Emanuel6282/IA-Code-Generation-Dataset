//> using scala 3.6.4

/*
 * P02: Binary Search — Scala 3.6
 *
 * CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
 *
 * 1. Sintaxis por indentación:
 *    Reduce las llaves y mejora la legibilidad.
 *
 * 2. Condicionales if ... then ... else:
 *    Utiliza la sintaxis moderna de Scala 3.
 *
 * 3. Recursión de cola con @tailrec:
 *    Reemplaza el bucle while y permite al compilador
 *    optimizar las llamadas recursivas.
 *
 * 4. Inmutabilidad mediante val:
 *    Evita variables mutables para los límites
 *    de búsqueda.
 *
 * 5. Directiva de versión de Scala CLI:
 *    Permite seleccionar explícitamente Scala 3.6.4.
 *
 * NOTA:
 * La sintaxis por indentación y @tailrec ya estaban
 * disponibles antes de Scala 3.6. Se utilizan porque
 * son apropiadas para este refactoring.
 * No se introducen características exclusivas de 3.6
 * que compliquen innecesariamente el algoritmo.
 */

import scala.annotation.tailrec

object P02BinarySearch_v36:

  def binarySearch(arr: Array[Int], target: Int): Int =

    @tailrec
    def search(left: Int, right: Int): Int =
      if left > right then
        -1
      else
        val mid = left + (right - left) / 2

        if arr(mid) == target then
          mid
        else if arr(mid) < target then
          search(mid + 1, right)
        else
          search(left, mid - 1)

    search(0, arr.length - 1)

  def main(args: Array[String]): Unit =

    // Test 1: Objetivo encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Test 2: Objetivo no encontrado
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Test 3: Arreglo vacío
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("All tests passed")

/*
 * RESUMEN DE CAMBIOS — SCALA 3.3 A SCALA 3.6
 *
 * 1. Se actualizó el nombre del objeto:
 *    P02BinarySearch_v213 -> P02BinarySearch_v36.
 *    Motivo: identificar la versión de destino.
 *
 * 2. Se reemplazó el bucle while por search().
 *    Motivo: expresar la búsqueda mediante recursión
 *    de cola, una alternativa idiomática en Scala.
 *
 * 3. Se eliminaron las variables mutables var.
 *    Motivo: representar los límites mediante
 *    parámetros inmutables.
 *
 * 4. Se incorporó @tailrec.
 *    Motivo: verificar en compilación que la recursión
 *    sea optimizable y no aumente la pila.
 *
 * 5. Se adoptó la sintaxis por indentación.
 *    Motivo: mejorar la claridad del código.
 *
 * 6. Se utilizaron if ... then ... else.
 *    Motivo: seguir el estilo de Scala 3.
 *
 * 7. Se conservó el nombre binarySearch.
 *    Motivo: mantener la interfaz pública.
 *
 * 8. Se mantuvo el cálculo del índice central:
 *    left + (right - left) / 2.
 *    Motivo: reducir el riesgo de desbordamiento.
 *
 * 9. Se conservaron exactamente los tres assert.
 *    Motivo: garantizar los mismos resultados.
 *
 * 10. Se mantuvo la complejidad:
 *     Tiempo: O(log n).
 *     Espacio auxiliar: O(1) con recursión de cola.
 *
 * 11. Se añadió la directiva de Scala CLI.
 *     Motivo: facilitar la ejecución con Scala 3.6.
 */