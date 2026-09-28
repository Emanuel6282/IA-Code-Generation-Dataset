object BinarySearchApp {

  def binarySearch(arr: Array[Int], target: Int): Int = {
    var left = 0
    var right = arr.length - 1

    while (left <= right) {
      val mid = left + (right - left) / 2

      if (arr(mid) == target) return mid
      else if (arr(mid) < target) left = mid + 1
      else right = mid - 1
    }

    -1
  }

  def main(args: Array[String]): Unit = {
    // --- Casos de prueba ---

    // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2, "Falló el Caso 1")

    // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1, "Falló el Caso 2")

    // Caso 3: Arreglo vacío y comprobación en extremos
    assert(binarySearch(Array(), 10) == -1, "Falló el Caso 3a (Arreglo vacío)")
    assert(binarySearch(Array(10, 20, 30, 40), 10) == 0, "Falló el Caso 3b (Primer elemento)")
    assert(binarySearch(Array(10, 20, 30, 40), 40) == 3, "Falló el Caso 3c (Último elemento)")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}