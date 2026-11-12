//> using scala 3.3.6

/*
 * P02: Binary Search — Scala 3.3
 *
 * CARACTERÍSTICAS UTILIZADAS:
 *
 * 1. Sintaxis por indentación:
 *    Elimina llaves innecesarias y mejora la legibilidad.
 *
 * 2. if ... then ... else:
 *    Utiliza la sintaxis moderna de Scala 3.
 *
 * 3. Recursión de cola con @tailrec:
 *    Sustituye el bucle while por una función recursiva
 *    optimizada, sin crecimiento de la pila.
 *
 * 4. Inmutabilidad mediante val:
 *    Elimina las variables mutables left y right.
 *
 * 5. Inferencia de tipos:
 *    Mantiene el código conciso sin perder seguridad
 *    de tipos.
 *
 * 6. Compatibilidad con Scala 3.3:
 *    Conserva Array[Int] y la interfaz original.
 */

import scala.annotation.tailrec

object P02BinarySearch_v33:

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
 * RESUMEN DE CAMBIOS — SCALA 2.13 A SCALA 3.3
 *
 * 1. Se actualizó el nombre del objeto:
 *    P02BinarySearch_v213 -> P02BinarySearch_v33.
 *    Motivo: identificar la versión de destino.
 *
 * 2. Se reemplazó el bucle while por la función search.
 *    Motivo: expresar el algoritmo mediante recursión
 *    de cola, una alternativa idiomática en Scala.
 *
 * 3. Se eliminaron las variables mutables var.
 *    Motivo: utilizar parámetros inmutables para
 *    representar los límites de búsqueda.
 *
 * 4. Se incorporó @tailrec.
 *    Motivo: garantizar en compilación que las llamadas
 *    recursivas sean optimizables.
 *
 * 5. Se adoptó la sintaxis por indentación.
 *    Motivo: reducir elementos sintácticos innecesarios.
 *
 * 6. Se utilizaron if ... then ... else.
 *    Motivo: aprovechar la sintaxis de Scala 3.
 *
 * 7. Se conservó el nombre binarySearch.
 *    Motivo: mantener la interfaz pública original.
 *
 * 8. Se mantuvo el cálculo del índice central:
 *    left + (right - left) / 2.
 *    Motivo: evitar posibles desbordamientos.
 *
 * 9. Se conservaron los tres assert originales.
 *    Motivo: verificar un comportamiento idéntico.
 *
 * 10. Se mantuvieron las complejidades:
 *     Tiempo: O(log n).
 *     Espacio auxiliar: O(1), gracias a @tailrec.
 *
 * NOTA:
 * La anotación @tailrec ya existía en Scala 2.13.
 * La mejora específica de Scala 3 es principalmente
 * la nueva sintaxis. La recursión de cola constituye
 * una decisión de refactorización, no una característica
 * introducida por Scala 3.3.
 */