// P06: Max Subarray Sum — Scala
// Algoritmo de Kadane
//
// Complejidad temporal: O(n)
// Complejidad espacial: O(1)

object P06MaxSubarraySum {

  def maxSubarraySum(arr: Array[Int]): Int = {

    // No se permite un subarreglo vacío.
    require(arr.nonEmpty, "El arreglo no puede estar vacío")

    // Inicializar con el primer elemento.
    var currentMax = arr(0)
    var globalMax = arr(0)

    // Recorrer los elementos restantes.
    for (i <- 1 until arr.length) {

      // Iniciar un nuevo subarreglo o continuar el anterior.
      currentMax = math.max(arr(i), currentMax + arr(i))

      // Actualizar la máxima suma encontrada.
      globalMax = math.max(globalMax, currentMax)
    }

    globalMax
  }

  def main(args: Array[String]): Unit = {

    // TEST 1: Ejemplo del enunciado.
    assert(
      maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6
    )

    // TEST 2: Todos los elementos negativos.
    assert(
      maxSubarraySum(Array(-8, -3, -6, -2, -5, -4)) == -2
    )

    // TEST 3: Todos los elementos positivos.
    assert(
      maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15
    )

    println("All tests passed")
  }
}