// P02: Binary Search — Scala 2.13 (paradigma funcional)
// Solo biblioteca estandar de Scala.

import scala.annotation.tailrec

object BinarySearch {

  /**
   * Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
   * retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
   * retorna -1.
   *
   * Implementacion estrictamente funcional: recursion de cola anotada con
   * @tailrec, sin var ni colecciones mutables. Los limites left/right se
   * pasan como parametros inmutables que se recalculan en cada llamada
   * recursiva, nunca se reasignan variables externas.
   *
   * @param arr Arreglo de enteros ordenado ascendentemente (usado solo para
   *            lectura indexada; no se muta en ningun momento).
   * @param target Valor entero a buscar.
   * @return El indice donde se encuentra target dentro de arr, o -1 si
   *         target no esta presente.
   */
  def binarySearch(arr: Array[Int], target: Int): Int = {

    @tailrec
    def loop(left: Int, right: Int): Int =
      if (left > right) {
        -1
      } else {
        val mid = left + (right - left) / 2
        if (arr(mid) == target) mid
        else if (arr(mid) < target) loop(mid + 1, right)
        else loop(left, mid - 1)
      }

    loop(0, arr.length - 1)
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado, objetivo presente en el medio
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Caso 2: ejemplo del enunciado, objetivo ausente
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Caso 3: arreglo vacio -> siempre -1
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("Todos los casos de prueba pasaron correctamente.")
  }
}