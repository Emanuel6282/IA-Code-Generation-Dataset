import scala.annotation.tailrec

object BinarySearch {

  /** Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está. */
  def binarySearch(arr: Array[Int], target: Int): Int = {
    // @tailrec: el compilador garantiza que la recursión es de cola y la
    // convierte en un bucle, así que no consume pila.
    @tailrec
    def search(low: Int, high: Int): Int =
      if (low > high) -1
      else {
        val mid = low + (high - low) / 2 // evita el desbordamiento de Int de (low + high) / 2
        if (arr(mid) == target) mid
        else if (arr(mid) < target) search(mid + 1, high) // está en la mitad derecha
        else search(low, mid - 1)                          // está en la mitad izquierda
      }

    search(0, arr.length - 1)
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

    // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

    // Caso 3: arreglo vacío
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("Todas las pruebas pasaron.")
  }
}