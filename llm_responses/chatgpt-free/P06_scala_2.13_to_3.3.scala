// P06: Max Subarray Sum (Kadane) — Scala 3.3
//
// Características de Scala 3.3 utilizadas:
// 1. Sintaxis simplificada de Scala 3: se mantiene una estructura clara y concisa
//    sin necesidad de construcciones adicionales de Scala 2.
// 2. Inferencia de tipos: las variables locales conservan sus tipos inferidos,
//    reduciendo código redundante sin cambiar la lógica.
// 3. Sintaxis moderna de Scala 3: el código utiliza la sintaxis compatible con
//    Scala 3.3 manteniendo el enfoque iterativo solicitado.
//
// El algoritmo continúa utilizando Kadane con acumuladores mutables,
// porque el objetivo es conservar el paradigma iterativo del código original.

object P06MaxSubarray_v33 {

  def maxSubarraySum(arr: Array[Int]): Int = {
    var currentMax = arr(0)
    var globalMax = arr(0)
    var i = 1

    while i < arr.length do
      currentMax =
        if currentMax + arr(i) > arr(i) then
          currentMax + arr(i)
        else
          arr(i)

      if currentMax > globalMax then
        globalMax = currentMax

      i += 1

    globalMax
  }

  def main(args: Array[String]): Unit = {
    assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
    assert(maxSubarraySum(Array(1)) == 1)
    assert(maxSubarraySum(Array(-2, -1)) == -1)
    assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)

    println("All tests passed")
  }
}

/*
 * Explicación de los cambios:
 *
 * 1. Versión:
 *    El código está adaptado a Scala 3.3.
 *
 * 2. Sintaxis de while:
 *    Scala 3 permite escribir:
 *
 *        while condicion do
 *          ...
 *
 *    en lugar de la sintaxis con paréntesis y llaves de Scala 2.
 *    Esto reduce ruido visual y es idiomático en Scala 3.
 *
 * 3. Expresión if:
 *    Scala 3 permite utilizar if/then/else como expresiones de forma
 *    natural. El cálculo de currentMax se expresa directamente como:
 *
 *        currentMax =
 *          if currentMax + arr(i) > arr(i) then
 *            currentMax + arr(i)
 *          else
 *            arr(i)
 *
 * 4. Algoritmo:
 *    Se conserva exactamente el algoritmo de Kadane:
 *    - currentMax representa la máxima suma de un subarreglo que termina
 *      en la posición actual.
 *    - globalMax representa la máxima suma encontrada hasta ese momento.
 *
 * 5. Comportamiento:
 *    Los resultados son idénticos al código original, incluyendo el
 *    comportamiento para arreglos cuyos elementos son todos negativos.
 *
 * 6. Complejidad:
 *    Tiempo: O(n)
 *    Espacio adicional: O(1)
 *
 * 7. Tests:
 *    Se conservaron los cuatro casos de prueba originales.
 */