// Scala - Solución idiomática para Max Subarray Sum (Algoritmo de Kadane)
// Emplea colecciones inmutables y pliegues funcionales (foldLeft).

object MaxSubarraySumScala {

  /**
   * Calcula la suma máxima de cualquier subarreglo contiguo no vacío.
   *
   * @param arr Arreglo de enteros (puede contener negativos).
   * @return La suma máxima encontrada.
   * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
   */
  def maxSubarraySum(arr: Array[Int]): Int = {
    require(arr != null && arr.nonEmpty, "El arreglo no puede ser nulo o estar vacío (debe tener al menos 1 elemento).")

    // Estado inmutable inicial: (currentMax, globalMax)
    val initialState = (arr(0), arr(0))

    // Reducción funcional pura sobre el resto del arreglo utilizando foldLeft
    val (_, globalMax) = arr.tail.foldLeft(initialState) { case ((currentMax, globalMax), x) =>
      val newCurrent = math.max(x, currentMax + x)
      val newGlobal = math.max(globalMax, newCurrent)
      (newCurrent, newGlobal)
    }

    globalMax
  }

  // ==========================================
  // CASOS DE PRUEBA (ASSERTS)
  // ==========================================
  def main(args: Array[String]): Unit = {
    // CASO 1: Ejemplo del enunciado con números positivos y negativos
    // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
    val tc1 = Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
    assert(maxSubarraySum(tc1) == 6, s"Falló Caso 1: esperado 6, obtenido ${maxSubarraySum(tc1)}")

    // CASO 2: Todos los elementos son negativos
    // Subarreglo óptimo: [-1] -> retorna el menos negativo
    val tc2 = Array(-5, -3, -8, -1, -4)
    assert(maxSubarraySum(tc2) == -1, s"Falló Caso 2: esperado -1, obtenido ${maxSubarraySum(tc2)}")

    // CASO 3: Arreglo de un solo elemento positivo
    // Subarreglo óptimo: [42] -> suma = 42
    val tc3 = Array(42)
    assert(maxSubarraySum(tc3) == 42, s"Falló Caso 3: esperado 42, obtenido ${maxSubarraySum(tc3)}")

    println("¡Todos los casos de prueba en Scala pasaron correctamente!")
  }
}