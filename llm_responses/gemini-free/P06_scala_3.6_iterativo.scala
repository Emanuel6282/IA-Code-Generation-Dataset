// Scala 3.6 - Paradigma Iterativo Estricto
//
// Características de Scala 3.6 utilizadas:
// 1. Sintaxis limpia con indentación significativa avanzada y mejoras en el tipado estricto.
// 2. Paradigma imperativo estricto: Uso exclusivo de variables mutables ('var') y bucles 'while'.
// 3. Ausencia total de métodos funcionales de orden superior (map, filter, foldLeft) y recursión.

object MaxSubarraySumIterativeScala36:

  /**
   * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane)
   * utilizando estrictamente el paradigma iterativo en Scala 3.6.
   *
   * @param arr Arreglo de enteros (puede contener negativos).
   * @return La suma máxima encontrada.
   * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
   */
  def maxSubarraySum(arr: Array[Int]): Int =
    if arr == null || arr.isEmpty then
      throw IllegalArgumentException("El arreglo no puede ser nulo o estar vacío (debe tener al menos 1 elemento).")

    // Variables mutables locales para el estado imperativo
    var currentMax: Int = arr(0)
    var globalMax: Int = arr(0)
    var i: Int = 1

    // Bucle imperativo tradicional 'while'
    while i < arr.length do
      val num = arr(i)

      if currentMax + num > num then
        currentMax = currentMax + num
      else
        currentMax = num

      if currentMax > globalMax then
        globalMax = currentMax

      i += 1

    globalMax

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
    // Subarreglo óptimo: [999] -> suma = 999
    val tc3 = Array(999)
    assert(maxSubarraySum(tc3) == 999, s"Falló Caso 3: esperado 999, obtenido ${maxSubarraySum(tc3)}")

    println("¡Todos los casos de prueba iterativos en Scala 3.6 pasaron correctamente!")
  }