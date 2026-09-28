// Scala 2.13 - Paradigma Funcional Estricto
//
// Características funcionales utilizadas:
// 1. Inmutabilidad estricta: Uso exclusivo de 'val' y colecciones inmutables estándar de Scala.
// 2. Reducción de orden superior: Empleo de 'foldLeft' combinada con tuplas inmutables para propagar el estado (Kadane).
// 3. Ausencia total de efectos secundarios: Cero variables mutables ('var') y cero estructuras del paquete 'mutable'.
// 4. Validación declarativa previa mediante cláusulas de guarda ('require').

import scala.annotation.tailrec

object MaxSubarraySumFunctional213 {

  /**
   * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane)
   * utilizando estrictamente el paradigma funcional en Scala 2.13.
   *
   * @param arr Arreglo de enteros inmutable (puede contener negativos).
   * @return La suma máxima encontrada.
   * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
   */
  def maxSubarraySum(arr: Array[Int]): Int = {
    require(arr != null && arr.nonEmpty, "El arreglo no puede ser nulo o estar vacío (debe tener al menos 1 elemento).")

    // Estado inmutable inicial: (currentMax, globalMax)
    val initialState: (Int, Int) = (arr(0), arr(0))

    // Reducción funcional pura sobre el resto de la colección inmutable (.tail)
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
    // CASO 1: Ejemplo del enunciado con números mixtos
    // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
    val tc1 = Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
    assert(maxSubarraySum(tc1) == 6, s"Falló Caso 1: esperado 6, obtenido ${maxSubarraySum(tc1)}")

    // CASO 2: Todos los elementos son negativos
    // Subarreglo óptimo: [-1] -> retorna el menos negativo
    val tc2 = Array(-5, -3, -8, -1, -4)
    assert(maxSubarraySum(tc2) == -1, s"Falló Caso 2: esperado -1, obtenido ${maxSubarraySum(tc2)}")

    // CASO 3: Arreglo de un solo elemento positivo
    // Subarreglo óptimo: [100] -> suma = 100
    val tc3 = Array(100)
    assert(maxSubarraySum(tc3) == 100, s"Falló Caso 3: esperado 100, obtenido ${maxSubarraySum(tc3)}")

    println("¡Todos los casos de prueba funcionales en Scala 2.13 pasaron correctamente!")
  }
}