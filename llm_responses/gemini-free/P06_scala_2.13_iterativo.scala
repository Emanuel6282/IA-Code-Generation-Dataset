// Scala 2.13 - Paradigma Iterativo Estricto
// Características utilizadas:
// 1. Uso explícito de bucles 'while' y variables 'var' mutables locales.
// 2. Colecciones mutables de la biblioteca estándar (mutable.ListBuffer) para gestión de secuencias locales si se requiere.
// 3. Exclusión absoluta de métodos funcionales de orden superior como map, filter, foldLeft y recursión.

object MaxSubarraySumIterative213 {

  /**
   * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane)
   * utilizando estrictamente el paradigma iterativo con bucles while y variables mutables.
   *
   * @param arr Arreglo de enteros (puede contener negativos).
   * @return La suma máxima encontrada.
   * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
   */
  def maxSubarraySum(arr: Array[Int]): Int = {
    if (arr == null || arr.length == 0) {
      throw new IllegalArgumentException("El arreglo no puede ser nulo o estar vacío (debe tener al menos 1 elemento).")
    }

    // Variables mutables con 'var' para el estado imperativo
    var currentMax: Int = arr(0)
    var globalMax: Int = arr(0)
    var i: Int = 1

    // Bucle imperativo tradicional 'while'
    while (i < arr.length) {
      val num = arr(i)

      // Lógica iterativa de Kadane: extender la suma previa o reiniciar en el elemento actual
      if (currentMax + num > num) {
        currentMax = currentMax + num
      } else {
        currentMax = num
      }

      // Actualización del máximo global
      if (currentMax > globalMax) {
        globalMax = currentMax
      }

      i += 1
    }

    globalMax
  }

  // ==========================================
  // CASOS DE PRUEBA (ASSERTS)
  // ==========================================
  def main(args: Array[String]): Unit = {
    // CASO 1: Ejemplo del enunciado con números mixtos
    // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
    val tc1 = Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
    assert(maxSubarraySum(tc1) == 6, s"Falló Caso 1: esperado 6, obtenido ${maxSubarraySum(tc1)}")

    // CASO 2: Todos los elementos son negativos
    // Subarreglo óptimo: [-1] -> retorna el menos negativo
    val tc2 = Array(-5, -3, -8, -1, -4)
    assert(maxSubarraySum(tc2) == -1, s"Falló Caso 2: esperado -1, obtenido ${maxSubarraySum(tc2)}")

    // CASO 3: Arreglo de un solo elemento positivo
    // Subarreglo óptimo: [50] -> suma = 50
    val tc3 = Array(50)
    assert(maxSubarraySum(tc3) == 50, s"Falló Caso 3: esperado 50, obtenido ${maxSubarraySum(tc3)}")

    println("¡Todos los casos de prueba iterativos en Scala 2.13 pasaron correctamente!")
  }
}