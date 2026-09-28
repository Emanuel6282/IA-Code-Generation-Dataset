// Scala 3.6 - Paradigma Funcional Estricto (@tailrec)
//
// Características de Scala 3.6 utilizadas:
// 1. Sintaxis limpia con indentación significativa avanzada (característica central de Scala 3).
// 2. Recursión pura de cola optimizada mediante la anotación '@tailrec'.
// 3. Inmutabilidad estricta: Uso exclusivo de 'val', conversión a colecciones inmutables (List) y patrón de coincidencia (match).
// 4. Ausencia total de variables mutables ('var') y paquetes mutables ('mutable.*').

import scala.annotation.tailrec

object MaxSubarraySumFunctionalScala36:

  /**
   * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane)
   * utilizando estrictamente el paradigma funcional con recursión de cola en Scala 3.6.
   *
   * @param arr Arreglo de enteros inmutable (puede contener negativos).
   * @return La suma máxima encontrada.
   * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
   */
  def maxSubarraySum(arr: Array[Int]): Int =
    require(arr != null && arr.nonEmpty, "El arreglo no puede ser nulo o estar vacío (debe tener al menos 1 elemento).")

    @tailrec
    def loop(rest: List[Int], currentMax: Int, globalMax: Int): Int =
      rest match
        case Nil => globalMax
        case x :: xs =>
          val newCurrent = math.max(x, currentMax + x)
          val newGlobal = math.max(globalMax, newCurrent)
          loop(xs, newCurrent, newGlobal)

    val list = arr.toList
    loop(list.tail, list.head, list.head)

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
    // Subarreglo óptimo: [500] -> suma = 500
    val tc3 = Array(500)
    assert(maxSubarraySum(tc3) == 500, s"Falló Caso 3: esperado 500, obtenido ${maxSubarraySum(tc3)}")

    println("¡Todos los casos de prueba funcionales (@tailrec) en Scala 3.6 pasaron correctamente!")
  }