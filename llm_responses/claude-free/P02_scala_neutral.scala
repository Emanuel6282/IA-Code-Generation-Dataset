// P02: Word Frequency companion — Binary Search
// Solo biblioteca estandar de Scala.

import scala.annotation.tailrec

object BinarySearch {

  /**
   * Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
   * retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
   * retorna -1.
   *
   * Implementacion manual del algoritmo de busqueda binaria mediante
   * recursion de cola (@tailrec), sin usar ninguna funcion de busqueda de
   * la biblioteca estandar (como java.util.Arrays.binarySearch o metodos
   * equivalentes de las colecciones de Scala).
   *
   * @param arr Arreglo de enteros ordenado ascendentemente.
   * @param target Valor entero a buscar.
   * @return El indice donde se encuentra target dentro de arr, o -1 si
   *         target no esta presente.
   */
  def binarySearch(arr: Array[Int], target: Int): Int = {

    @tailrec
    def loop(left: Int, right: Int): Int = {
      if (left > right) {
        -1
      } else {
        val mid = left + (right - left) / 2
        if (arr(mid) == target) mid
        else if (arr(mid) < target) loop(mid + 1, right)
        else loop(left, mid - 1)
      }
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