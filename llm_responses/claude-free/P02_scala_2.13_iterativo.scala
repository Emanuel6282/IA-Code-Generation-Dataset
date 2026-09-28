// P02: Binary Search — Scala 2.13 (paradigma iterativo)
// Solo biblioteca estandar de Scala.

object BinarySearch {

  /**
   * Dado un arreglo de enteros ordenado ascendentemente y un valor objetivo,
   * retorna el indice del objetivo dentro del arreglo. Si no se encuentra,
   * retorna -1.
   *
   * Implementacion estrictamente iterativa: usa variables mutables (var)
   * left/right/mid y un bucle while, sin map(), filter(), foldLeft() ni
   * recursion. No usa ninguna funcion de busqueda de la biblioteca
   * estandar.
   *
   * @param arr Arreglo de enteros ordenado ascendentemente.
   * @param target Valor entero a buscar.
   * @return El indice donde se encuentra target dentro de arr, o -1 si
   *         target no esta presente.
   */
  def binarySearch(arr: Array[Int], target: Int): Int = {
    var left: Int = 0
    var right: Int = arr.length - 1
    var resultado: Int = -1
    var encontrado: Boolean = false

    while (left <= right && !encontrado) {
      val mid = left + (right - left) / 2
      if (arr(mid) == target) {
        resultado = mid
        encontrado = true
      } else if (arr(mid) < target) {
        left = mid + 1
      } else {
        right = mid - 1
      }
    }

    resultado
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