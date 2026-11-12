import scala.collection.mutable

object BinarySearch {

  /** Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está. */
  def binarySearch(arr: Array[Int], target: Int): Int = {
    var low = 0
    var high = arr.length - 1
    var result = -1
    // Se usa una variable de resultado en lugar de `return`: en Scala, `return`
    // dentro de un bucle es poco idiomático y el bucle termina igual al encontrarlo.
    while (result == -1 && low <= high) {
      val mid = low + (high - low) / 2 // evita el desbordamiento de Int de (low + high) / 2
      if (arr(mid) == target) result = mid
      else if (arr(mid) < target) low = mid + 1 // el objetivo está en la mitad derecha
      else high = mid - 1                        // el objetivo está en la mitad izquierda
    }
    result
  }

  def main(args: Array[String]): Unit = {
    val testCases = mutable.ListBuffer.empty[(String, Array[Int], Int, Int)]
    // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
    testCases += (("objetivo presente", Array(1, 3, 5, 7, 9), 5, 2))
    // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
    testCases += (("objetivo ausente", Array(1, 3, 5, 7, 9), 4, -1))
    // Caso 3: arreglo vacío
    testCases += (("arreglo vacío", Array.empty[Int], 1, -1))

    var i = 0
    while (i < testCases.length) {
      val (name, arr, target, expected) = testCases(i)
      val result = binarySearch(arr, target)
      assert(
        result == expected,
        s"Caso '$name': arr=${arr.mkString("[", ", ", "]")}, target=$target, esperado $expected, obtenido $result"
      )
      i += 1
    }

    println("Todas las pruebas pasaron.")
  }
}